package com.example.managerstaff.activities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Toast;

import com.example.managerstaff.R;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.ActivityLoginBinding;
import com.example.managerstaff.models.responses.UserResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    private boolean isShowPass=false,checkUsername=false,checkPassword=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        if(binding.edtPassword.getText().toString().length()>0){
            checkPassword=true;
        }

        if(binding.edtUsername.getText().toString().length()>0){
            checkUsername=true;
        }

        if(checkUsername && checkPassword){
            setButtonLight();
        }else{
            setButtonShadow();
        }

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

        binding.edtUsername.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.txtUsername.setTextColor(getColor(R.color.black));
                binding.edtUsername.setBackgroundDrawable(getDrawable(R.drawable.border_outline_round));
                binding.txtWarning.setVisibility(View.GONE);
                if(binding.edtUsername.getText().toString().length()>0){
                    checkUsername=true;
                    if(checkUsername && checkPassword){
                        setButtonLight();
                    }
                }else{
                    checkUsername=false;
                    setButtonShadow();
                }
            }
        });

        binding.edtPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.txtPassword.setTextColor(getColor(R.color.black));
                binding.edtPassword.setBackgroundDrawable(getDrawable(R.drawable.border_outline_round));
                binding.txtWarning1.setVisibility(View.GONE);
                if(binding.edtPassword.getText().toString().length()>0){
                    checkPassword=true;
                    if(checkPassword && checkUsername){
                        setButtonLight();
                    }
                }else{
                    checkPassword=false;
                    setButtonShadow();
                }
            }
        });

        binding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkPassword && checkUsername) {
                    setDetailEditText();
                    if (!binding.edtUsername.getText().toString().equals("") && !binding.edtPassword.getText().toString().equals("")) {
                        clickCallApiLogin(binding.edtUsername.getText().toString(), binding.edtPassword.getText().toString());
                    } else {
                        Toast.makeText(LoginActivity.this, R.string.username_or_password_not_empty, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void setDetailEditText(){
        binding.edtUsername.setTextColor(getColor(R.color.black));
        binding.edtUsername.setBackgroundDrawable(getDrawable(R.drawable.border_outline_round));
        binding.txtUsername.setTextColor(getColor(R.color.black));
        binding.edtPassword.setTextColor(getColor(R.color.black));
        binding.edtPassword.setBackgroundDrawable(getDrawable(R.drawable.border_outline_round));
        binding.txtPassword.setTextColor(getColor(R.color.black));
    }

    private void setButtonShadow(){
        binding.btnLogin.setTextColor(getColor(R.color.gray));
        binding.btnLogin.setBackgroundDrawable(getDrawable(R.drawable.button_cus_dark_shadow));
    }

    private void setButtonLight(){
        binding.btnLogin.setTextColor(getColor(R.color.white));
        binding.btnLogin.setBackgroundDrawable(getDrawable(R.drawable.button_cus));
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
                        if(userResponse.getMessage().equals(getString(R.string.not_exist))){
                            binding.txtUsername.setTextColor(getColor(R.color.red));
                            binding.edtUsername.setBackgroundDrawable(getDrawable(R.drawable.border_outline_round_red));
                            binding.txtWarning.setVisibility(View.VISIBLE);
                            binding.txtWarning.setText(getString(R.string.not_exist_account));
                        }else{
                            binding.txtPassword.setTextColor(getColor(R.color.red));
                            binding.edtPassword.setBackgroundDrawable(getDrawable(R.drawable.border_outline_round_red));
                            binding.txtWarning1.setVisibility(View.VISIBLE);
                            binding.txtWarning1.setText(getString(R.string.wrong_password));
                        }
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