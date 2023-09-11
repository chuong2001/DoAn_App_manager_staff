package com.example.managerstaff.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.managerstaff.R;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.ActivityChangePasswordBinding;
import com.example.managerstaff.models.User;
import com.example.managerstaff.models.responses.UserResponse;
import com.example.managerstaff.supports.Support;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends AppCompatActivity {

    ActivityChangePasswordBinding binding;
    private int IdUser;
    private User user;
    private boolean isShowPass=false,isShowPassNew=false,isShowPassConfirm=false,checkEmptyPassOld=false,checkEmptyPassNew=false,checkEmptyConfirm=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        IdUser = getIntent().getIntExtra("id_user", 0);
        user=new User();
        clickCallApiGetUserDetail();
        onEventClick();

    }

    private void setButtonShadow(){
        binding.btnChangePassword.setTextColor(getColor(R.color.gray));
        binding.btnChangePassword.setBackgroundDrawable(getDrawable(R.drawable.button_cus_dark_shadow));
    }

    private void setButtonLight(){
        binding.btnChangePassword.setTextColor(getColor(R.color.white));
        binding.btnChangePassword.setBackgroundDrawable(getDrawable(R.drawable.button_cus));
    }

    private void onEventClick(){

        binding.icSeePassword1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShowPass==false) {
                    binding.icSeePassword1.setImageDrawable(getResources().getDrawable(R.drawable.icon_show));
                    binding.edtPasswordOld.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowPass=true;
                }else{
                    binding.edtPasswordOld.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.icSeePassword1.setImageDrawable(getResources().getDrawable(R.drawable.icon_hide));
                    isShowPass=false;
                }
            }
        });

        binding.icSeePassword2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShowPassNew==false) {
                    binding.icSeePassword2.setImageDrawable(getResources().getDrawable(R.drawable.icon_show));
                    binding.edtPasswordNew.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowPassNew=true;
                }else{
                    binding.edtPasswordNew.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.icSeePassword2.setImageDrawable(getResources().getDrawable(R.drawable.icon_hide));
                    isShowPassNew=false;
                }
            }
        });

        binding.icSeePassword3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isShowPassConfirm==false) {
                    binding.icSeePassword3.setImageDrawable(getResources().getDrawable(R.drawable.icon_show));
                    binding.edtConfirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    isShowPassConfirm=true;
                }else{
                    binding.edtConfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    binding.icSeePassword3.setImageDrawable(getResources().getDrawable(R.drawable.icon_hide));
                    isShowPassConfirm=false;
                }
            }
        });

        binding.edtPasswordOld.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(binding.edtPasswordOld.getText().toString().length()>0){
                    checkEmptyPassOld=true;
                    if(checkEmptyConfirm && checkEmptyPassOld && checkEmptyPassNew){
                        setButtonLight();
                    }
                }else{
                    checkEmptyPassOld=false;
                    setButtonShadow();
                }
                binding.txtWarning1.setVisibility(View.GONE);
                binding.edtPasswordOld.setBackgroundDrawable(getDrawable(R.drawable.border_outline_round));
                binding.txtTitle1.setTextColor(getColor(R.color.black));
            }
        });

        binding.edtPasswordNew.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (binding.edtPasswordNew.getText().toString().trim().length() > 0) {
                    if (Support.checkPassValidate(binding.edtPasswordNew.getText().toString().trim())) {
                        checkEmptyPassNew = true;
                        if (checkEmptyConfirm && checkEmptyPassOld && checkEmptyPassNew) {
                            setButtonLight();
                        }
                        binding.txtWarning2.setVisibility(View.GONE);
                        binding.txtTitle2.setTextColor(getColor(R.color.black));
                        binding.edtPasswordNew.setBackgroundDrawable(getDrawable(R.drawable.border_outline_round));
                    } else {
                        binding.txtWarning2.setVisibility(View.VISIBLE);
                        binding.txtWarning2.setText(getString(R.string.warning_password_new));
                        binding.txtTitle2.setTextColor(getColor(R.color.red));
                        binding.edtPasswordNew.setBackgroundDrawable(getDrawable(R.drawable.border_outline_round_red));
                        checkEmptyPassNew = false;
                        setButtonShadow();
                    }
                }else{
                    binding.txtWarning2.setVisibility(View.GONE);
                    binding.txtTitle2.setTextColor(getColor(R.color.black));
                    binding.edtPasswordNew.setBackgroundDrawable(getDrawable(R.drawable.border_outline_round));
                    checkEmptyPassNew = false;
                    setButtonShadow();
                }
            }
        });

        binding.edtConfirmPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(binding.edtConfirmPassword.getText().toString().length()>0){
                    if(binding.edtConfirmPassword.getText().toString().equals(binding.edtPasswordNew.getText().toString())) {
                        checkEmptyConfirm = true;
                        if (checkEmptyConfirm && checkEmptyPassOld && checkEmptyPassNew) {
                            setButtonLight();
                        }
                        binding.txtWarning3.setVisibility(View.GONE);
                        binding.txtTitle8.setTextColor(getColor(R.color.black));
                        binding.edtConfirmPassword.setBackgroundDrawable(getDrawable(R.drawable.border_outline_round));
                    }else{
                        binding.txtWarning3.setVisibility(View.VISIBLE);
                        binding.txtWarning3.setText(getString(R.string.warning_password_confirm));
                        binding.txtTitle8.setTextColor(getColor(R.color.red));
                        binding.edtConfirmPassword.setBackgroundDrawable(getDrawable(R.drawable.border_outline_round_red));
                        checkEmptyConfirm = false;
                        setButtonShadow();
                    }
                }else{
                    binding.edtConfirmPassword.setBackgroundDrawable(getDrawable(R.drawable.border_outline_round));
                    binding.txtWarning3.setVisibility(View.GONE);
                    binding.txtTitle8.setTextColor(getColor(R.color.black));
                    checkEmptyConfirm=false;
                    setButtonShadow();
                }
            }
        });

        binding.btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkEmptyConfirm && checkEmptyPassOld && checkEmptyPassNew){
                    if(binding.edtPasswordOld.getText().toString().equals(user.getAccount().getPassword())){
                        binding.pbLoad.setVisibility(View.VISIBLE);
                        clickCallApiChangePassword();
                    }else{
                        binding.txtWarning1.setVisibility(View.VISIBLE);
                        binding.edtPasswordOld.setBackgroundDrawable(getDrawable(R.drawable.border_outline_round_red));
                        binding.txtTitle1.setTextColor(getColor(R.color.red));
                    }
                }
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void clickCallApiGetUserDetail() {
        ApiService.apiService.getUserDetail(IdUser).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                binding.pbLoad.setVisibility(View.GONE);
                if (userResponse != null) {
                    if(userResponse.getCode()==200){
                        user=userResponse.getUser();
                        binding.txtFullName.setText(user.getFullName());
                    }else{
                        Toast.makeText(ChangePasswordActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                binding.pbLoad.setVisibility(View.GONE);
                Toast.makeText(ChangePasswordActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickCallApiChangePassword() {
        ApiService.apiService.changePassword(IdUser,binding.edtPasswordNew.getText().toString()).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if(userResponse.getCode()==200){
                        user=userResponse.getUser();
                        Toast.makeText(ChangePasswordActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                        finish();
                    }else{
                        Toast.makeText(ChangePasswordActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ChangePasswordActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}