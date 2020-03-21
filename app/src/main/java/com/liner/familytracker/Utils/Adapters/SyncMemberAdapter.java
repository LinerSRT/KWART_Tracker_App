package com.liner.familytracker.Utils.Adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.Adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.auth.User;
import com.liner.familytracker.Application;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.R;
import com.liner.familytracker.Utils.Helper;
import com.liner.familytracker.Utils.HelperListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SyncMemberAdapter extends Adapter<SyncMemberAdapter.ViewHolder> {
    private Context context;
    private List<UserModel> users;

    public SyncMemberAdapter(Context context, List<UserModel> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.member_recycler_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
       UserModel user = users.get(position);
       if(user.getPhotoUrl() != null)
        Picasso.with(context).load(user.getPhotoUrl()).into(holder.memberPhoto);
       holder.memberName.setText(user.getUserName());
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView memberPhoto;
        TextView memberName, memberLocationAdress, memberBattery;
        ViewHolder(final View view) {
            super(view);
            memberPhoto = view.findViewById(R.id.memberPhoto);
            memberName = view.findViewById(R.id.memberName);
            memberLocationAdress = view.findViewById(R.id.memberLocationAddress);
            memberBattery = view.findViewById(R.id.memberBatteryLevel);
        }
    }


}
