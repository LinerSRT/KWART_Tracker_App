package com.liner.familytracker.Utils.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.liner.familytracker.DatabaseModels.DeviceStatus;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.R;
import com.liner.familytracker.Services.ControllerServiceUtils;
import com.liner.familytracker.Utils.PrefHelper;
import com.mlsdev.animatedrv.AnimatedRecyclerView;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class SyncMemberAdapter extends Adapter<SyncMemberAdapter.ViewHolder> implements ChildEventListener {
    private Context context;
    private List<UserModel> usersModels;
    private PrefHelper prefHelper;
    private List<String> syncUsersList;

    public SyncMemberAdapter(Context context, List<String> users) {
        this.context = context;
        prefHelper = new PrefHelper(context);
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
        DatabaseReference allUsersDatabase = firebaseDatabase.getReference("Users");
        usersModels = new ArrayList<>();
        syncUsersList = users;
        boolean found = false;
        for(String user:users){
            if(prefHelper.isUserExist(user)){
                usersModels.add(prefHelper.getUser(user));
            }
        }
        //if(!found){
        //    users.add(FirebaseAuth.getInstance().getCurrentUser().getUid());
        //}
        allUsersDatabase.addChildEventListener(this);
    }

    @Override
    public int getItemCount() {
        return usersModels.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
       UserModel user = usersModels.get(position);
       DeviceStatus deviceStatus = user.getDeviceStatus();
       if(user.getPhotoUrl() != null)
           Picasso.with(context).load(user.getPhotoUrl()).into(holder.memberPhoto);
       holder.memberName.setText(user.getUserName());
       if(deviceStatus != null) {
           holder.memberBattery.setText(deviceStatus.getBatteryLevel()+"%");
           holder.memberLocationAdress.setText(ControllerServiceUtils.getPlace(context, deviceStatus.getLocationLat(), deviceStatus.getLocationLon()));
           holder.networkStatus.setText(deviceStatus.getNetworkType());
           holder.lastLocationUpdate.setText(new SimpleDateFormat("HH:mm dd/MM", Locale.US).format(deviceStatus.getLocationTime()));
       }
    }

    @Override
    public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        //for(String user:syncUsersList){
        //    if(dataSnapshot.child("uid").getValue().toString().equals(user)){
        //        usersModels.add(fillUser(dataSnapshot));
        //        notifyAdapter();
        //    }
        //}
    }

    @Override
    public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
        for(String user:syncUsersList){
            if(dataSnapshot.child("uid").getValue().toString().equals(user)){
                UserModel userModel = fillUser(dataSnapshot);
                if(getIndex(userModel) != -1) {
                    usersModels.set(getIndex(userModel), userModel);
                    notifyAdapter();
                }
            }
        }

    }

    @Override
    public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
        for(String user:syncUsersList){
            if(dataSnapshot.child("uid").getValue().toString().equals(user)){
                UserModel userModel = fillUser(dataSnapshot);
                if(getIndex(userModel) != -1) {
                    usersModels.remove(getIndex(userModel));
                    notifyAdapter();
                }
            }
        }

    }

    @Override
    public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {

    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView memberPhoto;
        TextView memberName, memberLocationAdress, memberBattery, networkStatus, lastLocationUpdate;
        ImageButton moveMap;
        ViewHolder(final View view) {
            super(view);
            memberPhoto = view.findViewById(R.id.memberPhoto);
            memberName = view.findViewById(R.id.memberName);
            memberLocationAdress = view.findViewById(R.id.memberLocationAddress);
            memberBattery = view.findViewById(R.id.memberBatteryLevel);
            networkStatus = view.findViewById(R.id.networkStatus);
            lastLocationUpdate = view.findViewById(R.id.lastLocationUpdateTime);
            moveMap = view.findViewById(R.id.moveMap);
        }
    }

    private int getIndex(UserModel userModel){
        if(usersModels != null && userModel != null)
        for (int i = 0; i < usersModels.size(); i++) {
            if(usersModels.get(i).getUid().equals(userModel.getUid())){
                return i;
            }
        }
        return -1;
    }
    private UserModel fillUser(DataSnapshot dataSnapshot){
        UserModel user = new UserModel();
        if (dataSnapshot.hasChild("uid"))
            user.setUid(dataSnapshot.child("uid").getValue().toString());
        if (dataSnapshot.hasChild("phoneNumber"))
            user.setPhoneNumber(dataSnapshot.child("phoneNumber").getValue().toString());
        if (dataSnapshot.hasChild("userName"))
            user.setUserName(dataSnapshot.child("userName").getValue().toString());
        if (dataSnapshot.hasChild("userEmail"))
            user.setUserEmail(dataSnapshot.child("userEmail").getValue().toString());
        if (dataSnapshot.hasChild("inviteCode"))
            user.setInviteCode(dataSnapshot.child("inviteCode").getValue().toString());
        if (dataSnapshot.hasChild("photoUrl"))
            user.setPhotoUrl(dataSnapshot.child("photoUrl").getValue().toString());
        List<String> usersSynced = new ArrayList<>();
        if (dataSnapshot.hasChild("synchronizedUsers")) {
            for (DataSnapshot item : dataSnapshot.child("synchronizedUsers").getChildren()) {
                usersSynced.add(item.getValue().toString());
            }
        }
        user.setSynchronizedUsers(usersSynced);
        DeviceStatus deviceStatus = new DeviceStatus();
        if(dataSnapshot.hasChild("deviceStatus")){
            deviceStatus = dataSnapshot.child("deviceStatus").getValue(DeviceStatus.class);
        }
        user.setDeviceStatus(deviceStatus);
        return user;
    }

    private void notifyAdapter(){
        notifyDataSetChanged();
    }
}
