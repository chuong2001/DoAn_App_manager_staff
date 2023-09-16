package com.example.managerstaff.adapter;

import android.app.Activity;
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
import com.example.managerstaff.models.Post;
import com.example.managerstaff.models.TimeIn;
import com.example.managerstaff.models.TimeOut;

import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {
    private Activity mActivity;

    private List<Post> listPosts;

    public PostAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setData(List<Post> listPosts){
        this.listPosts=listPosts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PostAdapter.PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout_post_slider,parent,false);
        return new PostAdapter.PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position) {
        Post post=listPosts.get(position);
        if(post!=null){
            String img="";
            if(post.getListImages().size()>0){
                img=post.getListImages().get(0).getImage();
            }
            Glide.with(mActivity).load(img)
                    .error(R.drawable.img_notify)
                    .placeholder(R.drawable.img_notify)
                    .into(holder.imgPost);
            holder.txtTitlePost.setText(post.getTypePost());
            holder.txtBodyPost.setText(post.getHeaderPost());
            holder.txtTimePost.setText(post.getTimePost());
            holder.layoutItemPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }

    }


    @Override
    public int getItemCount() {
        if(listPosts!=null) return listPosts.size();
        return 0;
    }
    public class  PostViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTitlePost,txtBodyPost,txtTimePost;
        private ImageView imgPost;
        private ConstraintLayout layoutItemPost;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);
            imgPost = itemView.findViewById(R.id.img_post);
            txtTitlePost = itemView.findViewById(R.id.txt_title_post);
            txtBodyPost = itemView.findViewById(R.id.txt_body_post);
            txtTimePost = itemView.findViewById(R.id.txt_day_create_post);
            layoutItemPost=itemView.findViewById(R.id.layout_item_post);

        }
    }
}