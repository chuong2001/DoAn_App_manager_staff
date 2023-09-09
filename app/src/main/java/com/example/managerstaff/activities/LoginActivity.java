package com.example.managerstaff.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import com.example.managerstaff.R;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.ActivityLoginBinding;
import com.example.managerstaff.models.responses.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private boolean isShowPass=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.icSeePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShowPass==false) {
                    binding.icSeePassword.setImageDrawable(getResources().getDrawable(R.drawable.icon_show));
                    binding.edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowPass=true;
                }else{
                    binding.edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.icSeePassword.setImageDrawable(getResources().getDrawable(R.drawable.icon_hide));
                    isShowPass=false;
                }
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!binding.edtUsername.getText().toString().equals("") && !binding.edtPassword.getText().toString().equals("")){
                    clickCallApiLogin(binding.edtUsername.getText().toString(),binding.edtPassword.getText().toString());
                }else{
                    Toast.makeText(LoginActivity.this, R.string.username_or_password_not_empty, Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void clickCallApiLogin(String username,String password) {
        ApiService.apiService.loginUser(username,password).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if(userResponse.getCode()==200){
                        Intent intent=new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("id_user",userResponse.getUser().getId());
                        startActivity(intent);
                        finish();
                    }else{
                        binding.txtWarning.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}