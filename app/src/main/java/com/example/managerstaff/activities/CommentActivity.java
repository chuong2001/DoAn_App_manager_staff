package com.example.managerstaff.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.managerstaff.R;
import com.example.managerstaff.adapter.ChatAdapter;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.ActivityCommentBinding;
import com.example.managerstaff.models.Post;
import com.example.managerstaff.models.responses.ListPostResponse;
import com.example.managerstaff.supports.Support;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CommentActivity extends AppCompatActivity {

    ActivityCommentBinding binding;
    private int IdUser;
    private int isAdmin;
    private List<Post> listPosts;
    private ChatAdapter chatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCommentBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        IdUser = getIntent().getIntExtra("id_user", 0);
        isAdmin = getIntent().getIntExtra("is_admin", 0);
        listPosts=new ArrayList<>();
        chatAdapter=new ChatAdapter(this);
        chatAdapter.setIdUser(IdUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(CommentActivity.this);
        binding.rcvListComment.setLayoutManager(linearLayoutManager);
        clickCallApiGetListPosts();
        chatAdapter.setData(listPosts);
        binding.rcvListComment.setAdapter(chatAdapter);
    }

    private void clickCallApiGetListPosts() {
        ApiService.apiService.getAllPost().enqueue(new Callback<ListPostResponse>() {
            @Override
            public void onResponse(Call<ListPostResponse> call, Response<ListPostResponse> response) {
                ListPostResponse listPostResponse = response.body();
                if (listPostResponse != null) {
                    if(listPostResponse.getCode()==200){
                        List<Post> list=listPostResponse.getListPosts();
                        listPosts.clear();
                        for(int i=0;i<list.size();i++){
                            if(Support.checkCommentOfUser(list.get(i),IdUser)){
                                listPosts.add(list.get(i));
                            }
                        }
                        if(listPosts.size()==0){
                            binding.txtNoData.setVisibility(View.VISIBLE);
                            binding.imgNoData.setVisibility(View.VISIBLE);
                        }else{
                            binding.txtNoData.setVisibility(View.GONE);
                            binding.imgNoData.setVisibility(View.GONE);
                        }
                        chatAdapter.setData(listPosts);
                    }else{
                        Toast.makeText(CommentActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(CommentActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListPostResponse> call, Throwable t) {
                Toast.makeText(CommentActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

}