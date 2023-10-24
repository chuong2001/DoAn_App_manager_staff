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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.managerstaff.R;
import com.example.managerstaff.activities.ChangePasswordActivity;
import com.example.managerstaff.activities.InfoUserActivity;
import com.example.managerstaff.activities.PostDetailActivity;
import com.example.managerstaff.activities.ShowImageActivity;
import com.example.managerstaff.activities.TimeKeepingActivity;
import com.example.managerstaff.models.Post;
import com.example.managerstaff.models.User;

import java.util.ArrayList;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private Activity mActivity;

    private List<User> listUsers;
    private String action;

    private int IdUser,IdAdmin;

    public UserAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setIdUser(int IdUser){
        this.IdUser=IdUser;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setIdAdmin(int idAdmin) {
        IdAdmin = idAdmin;
    }

    public void setData(List<User> listUsers){
        this.listUsers=listUsers;
        notifyDataSetChanged();
    }

    public void addAllData(List<User> listUsers){
        this.listUsers.addAll(listUsers);
        notifyDataSetChanged();
    }

    public void removeData(User user){
        this.listUsers.remove(user);
        notifyDataSetChanged();
    }

    public List<User> getListUsers() {
        return listUsers;
    }

    public void resetData(){
        this.listUsers=new ArrayList<>();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user,parent,false);
        return new UserAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user=listUsers.get(position);
        if(user!=null){
            Glide.with(mActivity).load(user.getAvatar())
                    .error(R.drawable.icon_user_gray)
                    .placeholder(R.drawable.icon_user_gray)
                    .into(holder.imgAvatar);
            holder.txtNameUser.setText(user.getFullName());
            holder.txtPosition.setText(user.getPosition().getNamePosition());
            holder.imgAvatar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent=new Intent(mActivity, ShowImageActivity.class);
                    intent.putExtra("position",0);
                    intent.putExtra("action","show");
                    intent.putExtra("uri_avatar",user.getAvatar());
                    mActivity.startActivity(intent);
                }
            });
            holder.layoutItemUser.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (action.equals("time")) {
                        Intent intent = new Intent(mActivity, TimeKeepingActivity.class);
                        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(mActivity, R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
                        intent.putExtra("id_user", IdUser);
                        intent.putExtra("id_user_watch", user.getIdUser());
                        intent.putExtra("id_admin", IdAdmin);
                        mActivity.startActivity(intent, bndlanimation);
                    }else{
                        Intent intent=new Intent(mActivity, InfoUserActivity.class);
                        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(mActivity, R.anim.slide_in_right,R.anim.slide_out_left).toBundle();
                        intent.putExtra("id_user",IdUser);
                        intent.putExtra("id_user_watch", user.getIdUser());
                        intent.putExtra("action", action);
                        intent.putExtra("id_admin", IdAdmin);
                        mActivity.startActivity(intent, bndlanimation);
                    }
                }
            });
        }

    }


    @Override
    public int getItemCount() {
        if(listUsers!=null) return listUsers.size();
        return 0;
    }
    public class  UserViewHolder extends RecyclerView.ViewHolder{
        public View layoutData;
        private TextView txtNameUser,txtPosition;
        private ImageView imgAvatar;
        private CardView layoutItemUser;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            imgAvatar = itemView.findViewById(R.id.img_avatar_user);
            txtNameUser = itemView.findViewById(R.id.txt_name_user);
            txtPosition = itemView.findViewById(R.id.txt_position);
            layoutItemUser=itemView.findViewById(R.id.layout_item_user);
            layoutData=itemView.findViewById(R.id.layout_data);

        }
    }
}