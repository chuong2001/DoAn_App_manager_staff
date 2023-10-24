package com.example.managerstaff.adapter;

import android.app.Activity;
import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
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
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.models.Comment;
import com.example.managerstaff.models.Post;
import com.example.managerstaff.models.User;
import com.example.managerstaff.models.responses.ObjectResponse;
import com.example.managerstaff.models.responses.UserResponse;
import com.example.managerstaff.supports.Support;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

    private Activity mActivity;
    private List<Post> listPosts;
    private int idUser,idAdmin;

    public ChatAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setData(List<Post> list) {
        this.listPosts = list;
        notifyDataSetChanged();
    }

    public void setIdAdmin(int idAdmin) {
        this.idAdmin = idAdmin;
    }

    public void removeData(Post post){
        this.listPosts.remove(post);
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
            if((idAdmin==idUser && post.getListComments().size()>0)||(Support.checkCommentOfUser(post,idUser))){
                clickCallApiCheckReadComment(idUser,post,holder);
                holder.layoutChat.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        clickCallApiReadComment(post);
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

    private void clickCallApiCheckReadComment(int ID,Post post, @NonNull ChatAdapter.ChatViewHolder holder) {
        ApiService.apiService.checkReadComment(Support.getAuthorization(mActivity),ID,post.getListComments().get(post.getListComments().size()-1).getIdComment()).enqueue(new Callback<ObjectResponse>() {
            @Override
            public void onResponse(Call<ObjectResponse> call, Response<ObjectResponse> response) {
                ObjectResponse objectResponse = response.body();
                if (objectResponse != null) {
                    if(objectResponse.getCode()==200){
                        holder.timeComment.setText(post.getListComments().get(post.getListComments().size()-1).getTime_cmt());
                        holder.headerPost.setText(post.getHeaderPost());
                        holder.imgIsRead.setVisibility(View.GONE);
                        clickCallApiGetUserDetail(post.getListComments().get(post.getListComments().size()-1),true,holder);
                    }else{
                        holder.timeComment.setText(Html.fromHtml("<b>"+post.getListComments().get(post.getListComments().size()-1).getTime_cmt()+"</b>"));
                        holder.headerPost.setText(Html.fromHtml("<b>"+post.getHeaderPost()+"</b>"));
                        holder.imgIsRead.setVisibility(View.VISIBLE);
                        clickCallApiGetUserDetail(post.getListComments().get(post.getListComments().size()-1),false,holder);
                    }
                }else{
                    holder.timeComment.setText(Html.fromHtml("<b>"+post.getListComments().get(post.getListComments().size()-1).getTime_cmt()+"</b>"));
                    holder.headerPost.setText(Html.fromHtml("<b>"+post.getHeaderPost()+"</b>"));
                    holder.imgIsRead.setVisibility(View.VISIBLE);
                    clickCallApiGetUserDetail(post.getListComments().get(post.getListComments().size()-1),false,holder);
                }
            }

            @Override
            public void onFailure(Call<ObjectResponse> call, Throwable t) {
                Toast.makeText(mActivity, mActivity.getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickCallApiReadComment(Post post) {
        ApiService.apiService.readComment(Support.getAuthorization(mActivity),idUser, post.getIdPost()).enqueue(new Callback<ObjectResponse>() {
            @Override
            public void onResponse(Call<ObjectResponse> call, Response<ObjectResponse> response) {
                ObjectResponse objectResponse = response.body();
            }

            @Override
            public void onFailure(Call<ObjectResponse> call, Throwable t) {
                Toast.makeText(mActivity, mActivity.getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickCallApiGetUserDetail(Comment comment,boolean isRead, @NonNull ChatAdapter.ChatViewHolder holder) {
        ApiService.apiService.getUserDetail(Support.getAuthorization(mActivity),comment.getIdUser()).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if(userResponse.getCode()==200){
                        User user=userResponse.getUser();
                        if(!isRead) {
                            holder.commentLast.setText(Html.fromHtml("<b>"+((user.getIdUser()==idUser)?mActivity.getString(R.string.you):user.getFullName())+": " + comment.getContent() + "</b>"));
                        }else{
                            holder.commentLast.setText(((user.getIdUser()==idUser)?mActivity.getString(R.string.you):user.getFullName())+": " +comment.getContent());
                        }
                    }else{
                        if(userResponse.getCode()==401){
                            Support.showDialogWarningExpiredAu(mActivity);
                        }else{
                            Toast.makeText(mActivity, mActivity.getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(mActivity, mActivity.getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(mActivity, mActivity.getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
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
        public View layoutData;

        public ChatViewHolder(@NonNull View itemView) {
            super(itemView);
            layoutChat=itemView.findViewById(R.id.layout_item_chat);
            headerPost=itemView.findViewById(R.id.txt_header_post);
            commentLast=itemView.findViewById(R.id.txt_message);
            timeComment=itemView.findViewById(R.id.txt_time_send);
            imgIsRead=itemView.findViewById(R.id.img_is_read);
            layoutData=itemView.findViewById(R.id.layout_chat);
        }
    }
}