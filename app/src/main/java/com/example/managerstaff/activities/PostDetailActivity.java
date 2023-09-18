package com.example.managerstaff.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.managerstaff.R;
import com.example.managerstaff.adapter.CommentAdapter;
import com.example.managerstaff.adapter.ImageAdapter;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.ActivityMainBinding;
import com.example.managerstaff.databinding.ActivityPostDetailBinding;
import com.example.managerstaff.models.Comment;
import com.example.managerstaff.models.Post;
import com.example.managerstaff.models.User;
import com.example.managerstaff.models.responses.PostResponse;
import com.example.managerstaff.models.responses.UserResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PostDetailActivity extends AppCompatActivity {

    ActivityPostDetailBinding binding;
    private int IdUser;
    private int IdPost;
    private Post post;
    private User user,userAdmin;
    private ImageAdapter adapter;
    private ImageView imgCancel,imgSend;
    private CommentAdapter commentAdapter;
    private List<Comment> mListComment;
    private RecyclerView rcvComment;
    private EditText edtContentComment;
    private boolean isEnterContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mListComment=new ArrayList<>();
        IdUser = getIntent().getIntExtra("id_user", 0);
        IdPost = getIntent().getIntExtra("id_post", 0);
        userAdmin=new User();
        post=new Post();
        user=new User();
        isEnterContent=false;
        adapter=new ImageAdapter(this);
        adapter.setIdUser(IdUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        binding.rcvListImage.setLayoutManager(linearLayoutManager);
        onEventClick();
        clickCallApiGetPostDetail();
        clickCallApiGetUserDetail();

    }

    private void onEventClick(){
        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.btnQuestionAnswer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogQuestionAnswer();
            }
        });

    }

    private void showDialogQuestionAnswer() {
        View viewDialog = getLayoutInflater().inflate(R.layout.dialog_question_answer, null);
        initViewComment(viewDialog);
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(viewDialog);
        bottomSheetDialog.show();
        bottomSheetDialog.setCancelable(true);
        bottomSheetDialog.getWindow().setDimAmount(0.3f);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        this.rcvComment.setLayoutManager(linearLayoutManager);
        this.mListComment=post.getListComments();
        commentAdapter=new CommentAdapter(this,user,userAdmin);
        commentAdapter.setData(mListComment);
        this.rcvComment.setAdapter(commentAdapter);
        edtContentComment.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(edtContentComment.getText().toString().length()>0){
                    isEnterContent=true;
                    imgSend.setImageDrawable(getDrawable(R.drawable.icon_send_blue));
                }else{
                    imgSend.setImageDrawable(getDrawable(R.drawable.icon_send_gray));
                    isEnterContent=false;
                }
            }
        });
        imgSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEnterContent){

                }
            }
        });
    }

    private void initViewComment(View view) {
        imgCancel=view.findViewById(R.id.imv_cancelx);
        edtContentComment=view.findViewById(R.id.edt_comment);
        imgSend=view.findViewById(R.id.send);
        rcvComment=view.findViewById(R.id.rcv_list_comment);
    }

    private void clickCallApiGetUserDetail() {
        ApiService.apiService.getUserDetail(IdUser).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if(userResponse.getCode()==200){
                        user=userResponse.getUser();
                    }else{
                        Toast.makeText(PostDetailActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(PostDetailActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(PostDetailActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickCallApiAddComment() {
        ApiService.apiService.getUserDetail(IdUser).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if(userResponse.getCode()==200){
                        user=userResponse.getUser();
                    }else{
                        Toast.makeText(PostDetailActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(PostDetailActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(PostDetailActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
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
                        binding.txtTypePost.setText(post.getTypePost());
                        binding.txtHeaderPost.setText(post.getHeaderPost());
                        binding.txtTimeCreatePost.setText(post.getTimePost());
                        binding.txtBodyPost.setText(post.getContent());
                        adapter.setData(post.getListImages());
                        binding.rcvListImage.setAdapter(adapter);
                        clickCallApiGetUserDetail(post.getIdUser());
                    }else{
                        Toast.makeText(PostDetailActivity.this,getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(PostDetailActivity.this,getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Toast.makeText(PostDetailActivity.this,getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickCallApiGetUserDetail(int IdAdmin) {
        ApiService.apiService.getUserDetail(IdAdmin).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if(userResponse.getCode()==200){
                        userAdmin=userResponse.getUser();
                        binding.txtNameUser.setText(userAdmin.getFullName());
                        if(userAdmin.getAvatar().length()>0){
                            Glide.with(PostDetailActivity.this).load(userAdmin.getAvatar())
                                    .error(R.drawable.icon_user_gray)
                                    .placeholder(R.drawable.icon_user_gray)
                                    .into(binding.imgAvatarUser);
                        }
                    }else{
                        Toast.makeText(PostDetailActivity.this,getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(PostDetailActivity.this,getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(PostDetailActivity.this,getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}