package com.example.managerstaff.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.managerstaff.R;
import com.example.managerstaff.models.Comment;
import com.example.managerstaff.models.User;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Activity mActivity;
    private List<Comment> mListComment;
    private int idUser;
    private User user, userAdmin;

    public CommentAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setUserAdmin(User userAdmin) {
        this.userAdmin = userAdmin;
    }

    public CommentAdapter(Activity mActivity, User user, User userAdmin) {
        this.mActivity = mActivity;
        this.user=user;
        this.userAdmin=userAdmin;
    }

    public void setData(List<Comment> list) {
        this.mListComment = list;
        notifyDataSetChanged();
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public void addComment(Comment comment) {
        this.mListComment.add(comment);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_comment, parent, false);
        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        int p=position;
        Comment comment = mListComment.get(position);
        if(comment!=null){
            if(comment.getIdUser()==user.getId()){
                holder.cvUserSender.setVisibility(View.VISIBLE);
                holder.layoutBodySender.setVisibility(View.VISIBLE);
                holder.cvUserReceiver.setVisibility(View.GONE);
                holder.layoutBodyReceiver.setVisibility(View.GONE);

                holder.commentBodySender.setText(comment.getContent());
                Glide.with(mActivity).load(user.getAvatar())
                        .error(R.drawable.icon_user_gray)
                        .placeholder(R.drawable.icon_user_gray)
                        .into(holder.imgUserSender);

            }else{
                if(comment.getIdUser()==userAdmin.getId()){
                    holder.cvUserSender.setVisibility(View.GONE);
                    holder.layoutBodySender.setVisibility(View.GONE);
                    holder.cvUserReceiver.setVisibility(View.VISIBLE);
                    holder.layoutBodyReceiver.setVisibility(View.VISIBLE);

                    holder.commentBodyReceiver.setText(comment.getContent());
                    Glide.with(mActivity).load(userAdmin.getAvatar())
                            .error(R.drawable.img_notify)
                            .placeholder(R.drawable.img_notify)
                            .into(holder.imgUserReceiver);
                }
            }
        }


    }

    public void showConfirmationDialog(int position) {
        Comment comment=mListComment.get(position);
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("Do you want to delete this comment?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        mListComment.remove(comment);
                        notifyDataSetChanged();
                        Toast.makeText(mActivity, "Success!", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }


    @Override
    public int getItemCount() {
        if (mListComment != null) return mListComment.size();
        return 0;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private ImageView imgUserReceiver,imgUserSender;
        private CardView cvUserReceiver,cvUserSender;
        private ConstraintLayout layoutBodyReceiver,layoutBodySender;
        private TextView commentBodyReceiver,commentBodySender;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);
            imgUserReceiver=itemView.findViewById(R.id.ivuser_receive);
            imgUserSender=itemView.findViewById(R.id.ivuser_send);
            cvUserReceiver=itemView.findViewById(R.id.cardView1);
            cvUserSender=itemView.findViewById(R.id.cardView2);
            layoutBodyReceiver=itemView.findViewById(R.id.layout_receiver);
            layoutBodySender=itemView.findViewById(R.id.layout_sender);
            commentBodyReceiver=itemView.findViewById(R.id.body_commnet_receive);
            commentBodySender=itemView.findViewById(R.id.body_commnet_send);

        }
    }
}