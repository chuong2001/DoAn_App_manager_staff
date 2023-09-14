package com.example.managerstaff.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.managerstaff.R;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.ActivityInfoUserBinding;
import com.example.managerstaff.models.User;
import com.example.managerstaff.models.responses.UserResponse;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class InfoUserActivity extends AppCompatActivity {

    ActivityInfoUserBinding binding;
    private List<String> listGenders;
    private int IdUser;
    private User user;
    private int REQUEST_CODE_FOLDER=456;
    private String AvatarNew;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityInfoUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        IdUser = getIntent().getIntExtra("id_user", 0);
        user=new User();
        listGenders=new ArrayList<>();
        listGenders.add(getString(R.string.Male));
        listGenders.add(getString(R.string.Female));
        ArrayAdapter<String> adapterGender=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,listGenders);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spGender.setAdapter(adapterGender);
        setEditTextDefault();
        onEventClick();
        clickCallApiGetUserDetail();
    }

    private void setEditTextDefault(){
        binding.imgCamera.setVisibility(View.GONE);
        binding.edtUsername.setEnabled(false);
        binding.edtAddress.setEnabled(false);
        binding.edtEmail.setEnabled(false);
        binding.edtFullname.setEnabled(false);
        binding.edtPhone.setEnabled(false);
        binding.edtUsername.setTextColor(getColor(R.color.gray));
        binding.edtAddress.setTextColor(getColor(R.color.gray));
        binding.edtEmail.setTextColor(getColor(R.color.gray));
        binding.edtFullname.setTextColor(getColor(R.color.gray));
        binding.edtPhone.setTextColor(getColor(R.color.gray));
    }

    private void setEditTextUpdate(){
        binding.imgCamera.setVisibility(View.VISIBLE);
        binding.edtUsername.setEnabled(true);
        binding.edtAddress.setEnabled(true);
        binding.edtEmail.setEnabled(true);
        binding.edtFullname.setEnabled(true);
        binding.edtPhone.setEnabled(true);
        binding.edtUsername.setTextColor(getColor(R.color.black));
        binding.edtAddress.setTextColor(getColor(R.color.black));
        binding.edtEmail.setTextColor(getColor(R.color.black));
        binding.edtFullname.setTextColor(getColor(R.color.black));
        binding.edtPhone.setTextColor(getColor(R.color.black));
    }

    private void onEventClick(){
        binding.txtEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.txtEdit.getText().toString().equals(getString(R.string.edit))) {
                    setEditTextUpdate();
                    binding.txtEdit.setText(getString(R.string.save));
                }else{
                    clickCallApiUpdateUser();
                    setEditTextDefault();
                    binding.txtEdit.setText(getString(R.string.edit));
                }
            }
        });

        binding.icGetTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(binding.txtEdit.getText().toString().equals(getString(R.string.save))){
                    Calendar selectedDate = Calendar.getInstance();
                    DatePickerDialog datePickerDialog = new DatePickerDialog(
                            InfoUserActivity.this,
                            new DatePickerDialog.OnDateSetListener() {
                                @Override
                                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                    selectedDate.set(Calendar.YEAR, year);
                                    selectedDate.set(Calendar.MONTH, month);
                                    selectedDate.set(Calendar.DAY_OF_MONTH, day);

                                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                                    String dateformat1 = sdf.format(selectedDate.getTime());
                                    String[] parts = dateformat1.split("/");
                                    if (parts.length == 3) {
                                        String formattedDate = parts[0] + "-" + parts[1] + "-" + parts[2];
                                        binding.txtBirthday.setText(formattedDate);
                                    }
                                }
                            },
                            selectedDate.get(Calendar.YEAR),
                            selectedDate.get(Calendar.MONTH),
                            selectedDate.get(Calendar.DAY_OF_MONTH)
                    );
                    datePickerDialog.show();

                }
            }
        });

        binding.imgCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent,REQUEST_CODE_FOLDER);
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode==REQUEST_CODE_FOLDER&& resultCode==RESULT_OK&&data!=null){
            Uri uri=data.getData();
            AvatarNew = uri.toString();
            Glide.with(InfoUserActivity.this).load(AvatarNew)
                    .error(R.drawable.item1)
                    .placeholder(R.drawable.item1)
                    .into(binding.imgAvatarUser);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void clickCallApiGetUserDetail() {
        ApiService.apiService.getUserDetail(IdUser).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if(userResponse.getCode()==200){
                        user=userResponse.getUser();
                        AvatarNew=user.getAvatar();
                        if(user.getAvatar().length()>0){
                            Glide.with(InfoUserActivity.this).load(user.getAvatar())
                                    .error(R.drawable.item1)
                                    .placeholder(R.drawable.item1)
                                    .into(binding.imgAvatarUser);
                        }

                        binding.edtUsername.setText(user.getAccount().getUsername());
                        binding.edtFullname.setText(user.getFullName());
                        binding.txtPart.setText(user.getPart().getNamePart());
                        binding.txtPosition.setText(user.getPosition().getNamePosition());
                        binding.edtPhone.setText(user.getPhone());
                        binding.edtEmail.setText(user.getEmail());
                        binding.txtBirthday.setText(user.getBirthday());
                        binding.edtAddress.setText(user.getAddress());
                        if(user.getGender().equals(getString(R.string.Male))){
                            binding.spGender.setSelection(0);
                        }else{
                            binding.spGender.setSelection(1);
                        }
                        binding.txtWage.setText(user.getWage()+" "+getString(R.string.VND));

                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(InfoUserActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickCallApiUpdateUser() {
        ApiService.apiService.updateUser(IdUser,AvatarNew,binding.edtFullname.getText().toString(),
                binding.txtBirthday.getText().toString(),binding.spGender.getSelectedItem().toString(),
                binding.edtAddress.getText().toString(),binding.edtEmail.getText().toString(),binding.edtPhone.getText().toString(),
                Double.parseDouble(binding.txtWage.getText().toString().substring(0,binding.txtWage.getText().toString().length()-4))).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if(userResponse.getCode()==200){
                        user=userResponse.getUser();
                        Toast.makeText(InfoUserActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                    }else{
                        if(user.getAvatar().length()>0){
                            Glide.with(InfoUserActivity.this).load(user.getAvatar())
                                    .error(R.drawable.item1)
                                    .placeholder(R.drawable.item1)
                                    .into(binding.imgAvatarUser);
                        }

                        binding.edtUsername.setText(user.getAccount().getUsername());
                        binding.edtFullname.setText(user.getFullName());
                        binding.txtPart.setText(user.getPart().getNamePart());
                        binding.txtPosition.setText(user.getPosition().getNamePosition());
                        binding.edtPhone.setText(user.getPhone());
                        binding.edtEmail.setText(user.getEmail());
                        binding.txtBirthday.setText(user.getBirthday());
                        binding.edtAddress.setText(user.getAddress());
                        binding.txtWage.setText(user.getWage()+" "+getString(R.string.VND));
                        Toast.makeText(InfoUserActivity.this, getString(R.string.update_success), Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(InfoUserActivity.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}