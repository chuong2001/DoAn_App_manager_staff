package com.example.managerstaff.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
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
import com.example.managerstaff.activities.ChatActivity;
import com.example.managerstaff.activities.InfoUserActivity;
import com.example.managerstaff.models.Comment;
import com.example.managerstaff.models.Post;
import com.example.managerstaff.models.User;
import com.example.managerstaff.supports.Support;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Activity mActivity;
    private List<Post> listPosts;
    private int idUser;

    public ChatAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setData(List<Post> list) {
        this.listPosts = list;
        notifyDataSetChanged();
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }


    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat, parent, false);
        return new ChatViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        int p=position;
        Post post = listPosts.get(position);
        if(post!=null){
            if(Support.checkCommentOfUser(post,idUser)){
                holder.commentLast.setText(post.getListComments().get(post.getListComments().size()-1).getContent());
                holder.timeComment.setText(post.getListComments().get(post.getListComments().size()-1).getTime_cmt());
                holder.headerPost.setText(post.getHeaderPost());
                holder.layoutChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(mActivity, ChatActivity.class);
                        Bundle bndlanimation = ActivityOptions.makeCustomAnimation(mActivity, R.anim.slide_in_right,R.anim.slide_out_left).toBundle();
                        intent.putExtra("id_user",idUser);
                        intent.putExtra("id_post",post.getIdPost());
                        intent.putExtra("id_admin",post.getIdUser());
                        mActivity.startActivity(intent,bndlanimation);
                    }
                });
            }
        }


    }




    @Override
    public int getItemCount() {
        if (listPosts != null) return listPosts.size();
        return 0;
    }

    public class ChatViewHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout layoutChat;
        private TextView headerPost,commentLast,timeComment;
        private ImageView imgIsRead;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutChat=itemView.findViewById(R.id.layout_item_chat);
            headerPost=itemView.findViewById(R.id.txt_header_post);
            commentLast=itemView.findViewById(R.id.txt_message);
            timeComment=itemView.findViewById(R.id.txt_time_send);
            imgIsRead=itemView.findViewById(R.id.img_is_read);

        }
    }
}