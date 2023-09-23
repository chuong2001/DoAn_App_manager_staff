package com.example.managerstaff.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.managerstaff.R;
import com.example.managerstaff.adapter.CommentAdapter;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.ActivityChatBinding;
import com.example.managerstaff.models.Comment;
import com.example.managerstaff.models.Post;
import com.example.managerstaff.models.User;
import com.example.managerstaff.models.responses.CommentResponse;
import com.example.managerstaff.models.responses.PostResponse;
import com.example.managerstaff.models.responses.UserResponse;
import com.example.managerstaff.supports.Support;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChatActivity extends AppCompatActivity {

    ActivityChatBinding binding;

    private List<Comment> listComments;
    private User user, admin;
    private Post post;
    private int IdUser,IdAdmin,IdPost;
    private CommentAdapter commentAdapter;
    private boolean isEnterContent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        IdUser = getIntent().getIntExtra("id_user", 0);
        IdAdmin = getIntent().getIntExtra("id_admin", 0);
        IdPost = getIntent().getIntExtra("id_post", 0);
        user=new User();
        post=new Post();
        admin=new User();
        isEnterContent=false;
        commentAdapter=new CommentAdapter(this);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rcvListComment.setLayoutManager(linearLayoutManager);
        listComments=new ArrayList<>();
        clickCallApiGetUserDetail(IdUser,0);
        clickCallApiGetUserDetail(IdAdmin,1);
        clickCallApiGetPostDetail();
        commentAdapter.setData(listComments);
        binding.rcvListComment.setAdapter(commentAdapter);
        binding.edtComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(binding.edtComment.getText().toString().length()>0){
                    isEnterContent=true;
                    binding.send.setImageDrawable(getDrawable(R.drawable.icon_send_blue));
                }else{
                    binding.send.setImageDrawable(getDrawable(R.drawable.icon_send_gray));
                    isEnterContent=false;
                }
            }
        });

        binding.btnShowPostDetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(ChatActivity.this, PostDetailActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(ChatActivity.this, R.anim.slide_in_right,R.anim.slide_out_left).toBundle();
                intent.putExtra("id_user",IdUser);
                intent.putExtra("id_post",IdPost);
                startActivity(intent,bndlanimation);
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEnterContent){
                    clickCallApiAddComment();
                }
            }
        });

    }

    private void clickCallApiGetUserDetail(int id, int isAdmin) {
        ApiService.apiService.getUserDetail(id).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if(userResponse.getCode()==200){
                        if(isAdmin==0){
                            user=userResponse.getUser();
                            commentAdapter.setUser(user);
                        }else{
                            admin=userResponse.getUser();
                            commentAdapter.setUserAdmin(admin);
                        }
                    }else{
                        Toast.makeText(ChatActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ChatActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ChatActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickCallApiAddComment() {
        ApiService.apiService.addComment(IdUser,IdPost, Support.getTimeNow(),binding.edtComment.getText().toString()).enqueue(new Callback<CommentResponse>() {
            @Override
            public void onResponse(Call<CommentResponse> call, Response<CommentResponse> response) {
                CommentResponse commentResponse = response.body();
                if (commentResponse != null) {
                    if(commentResponse.getCode()==200){
                        Comment comment =commentResponse.getComment();
                        commentAdapter.addComment(comment);
                        binding.edtComment.setText("");
                    }else{
                        Toast.makeText(ChatActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ChatActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CommentResponse> call, Throwable t) {
                Toast.makeText(ChatActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickCallApiGetPostDetail() {
        ApiService.apiService.getPostDetail(IdPost).enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                PostResponse postResponse = response.body();
                if (postResponse != null) {
                    if(postResponse.getCode()==200){
                        post=postResponse.getPost();
                        listComments=post.getListComments();
                        commentAdapter.setData(listComments);
                        binding.txtHeaderPost.setText(post.getHeaderPost());
                        binding.txtTypePost.setText(post.getTypePost());
                    }else{
                        Toast.makeText(ChatActivity.this,getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(ChatActivity.this,getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(ChatActivity.this,getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}