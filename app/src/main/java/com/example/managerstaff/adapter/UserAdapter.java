package com.example.managerstaff.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.managerstaff.R;
import com.example.managerstaff.activities.ChangePasswordActivity;
import com.example.managerstaff.activities.InfoUserActivity;
import com.example.managerstaff.activities.PostDetailActivity;
import com.example.managerstaff.activities.TimeKeepingActivity;
import com.example.managerstaff.models.Post;
import com.example.managerstaff.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.PostViewHolder> {
    private Activity mActivity;

    private List<User> listUsers;

    private int IdUser;

    public UserAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setIdUser(int IdUser){
        this.IdUser=IdUser;
    }

    public void setData(List<User> listUsers){
        this.listUsers=listUsers;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new UserAdapter.PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        User user=listUsers.get(position);
        if(user!=null && !user.getAccount().getUsername().equalsIgnoreCase("admin")){
            Glide.with(mActivity).load(user.getAvatar())
                    .error(R.drawable.icon_user_gray)
                    .placeholder(R.drawable.icon_user_gray)
                    .into(holder.imgAvatar);
            holder.txtNameUser.setText(user.getFullName());
            holder.txtPosition.setText(user.getPosition().getNamePosition());
            holder.layoutItemUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(mActivity, TimeKeepingActivity.class);
                    Bundle bndlanimation = ActivityOptions.makeCustomAnimation(mActivity, R.anim.slide_in_right,R.anim.slide_out_left).toBundle();
                    intent.putExtra("id_user",IdUser);
                    mActivity.startActivity(intent,bndlanimation);
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        if(listUsers!=null) return listUsers.size();
        return 0;
    }
    public class  PostViewHolder extends RecyclerView.ViewHolder{
        private TextView txtNameUser,txtPosition;
        private ImageView imgAvatar;
        private ConstraintLayout layoutItemUser;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.img_avatar_user);
            txtNameUser = itemView.findViewById(R.id.txt_name_user);
            txtPosition = itemView.findViewById(R.id.txt_position);
            layoutItemUser=itemView.findViewById(R.id.layout_item_user);

        }
    }
}