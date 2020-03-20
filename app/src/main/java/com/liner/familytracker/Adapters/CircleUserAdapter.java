package com.liner.familytracker.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.liner.familytracker.DatabaseModels.CircleModel;
import com.liner.familytracker.DatabaseModels.UserJoinedCircles;
import com.liner.familytracker.DatabaseModels.UserModel;
import com.liner.familytracker.R;

import java.util.List;

public class CircleUserAdapter extends RecyclerView.Adapter<CircleUserAdapter.ViewHolder> {

    private Context context;
    private CircleModel circleModel;

    public CircleUserAdapter(CircleModel circleModel, Context context){
        this.circleModel = circleModel;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.joined_user_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        final UserModel userModel = circleModel.getJoinedUsers().get(position);
        holder.userName.setText(userModel.getUserName());
    }

    @Override
    public int getItemCount() {
        return circleModel.getJoinedUsers().size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{
        ImageView userPhoto;
        TextView userName;
        ViewHolder(final View itemView) {
            super(itemView);
            userPhoto = itemView.findViewById(R.id.userPhoto);
            userName = itemView.findViewById(R.id.userName);
        }
    }


}
