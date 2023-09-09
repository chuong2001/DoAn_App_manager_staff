package com.example.managerstaff.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.managerstaff.R;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.ActivityInfoUserBinding;
import com.example.managerstaff.models.User;
import com.example.managerstaff.models.responses.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InfoUserActivity extends AppCompatActivity {

    ActivityInfoUserBinding binding;
    private int IdUser;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        IdUser = getIntent().getIntExtra("id_user", 0);
        user=new User();
        clickCallApiGetUserDetail();
    }

    private void clickCallApiGetUserDetail() {
        ApiService.apiService.getUserDetail(IdUser).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if(userResponse.getCode()==200){
                        user=userResponse.getUser();

                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(InfoUserActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}