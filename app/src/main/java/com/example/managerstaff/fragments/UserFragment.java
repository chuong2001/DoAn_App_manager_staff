package com.example.managerstaff.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.managerstaff.R;
import com.example.managerstaff.activities.InfoUserActivity;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.FragmentUserBinding;
import com.example.managerstaff.models.User;
import com.example.managerstaff.models.responses.UserResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserFragment extends Fragment {

    FragmentUserBinding binding;
    private int IdUser;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUserBinding.inflate(inflater, container, false);
        IdUser=getArguments().getInt("id_user");
        user=new User();
        eventClick();
        clickCallApiGetUserDetail();
        return binding.getRoot();
    }

    private void eventClick(){
        binding.cvFeatureInfoUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(requireActivity(), InfoUserActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right,R.anim.slide_out_left).toBundle();
                intent.putExtra("id_user",IdUser);
                startActivity(intent,bndlanimation);
            }
        });

        binding.cvFeatureLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(requireActivity(), InfoUserActivity.class);
                startActivity(intent);
            }
        });

    }

    private void clickCallApiGetUserDetail() {
        ApiService.apiService.getUserDetail(IdUser).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if(userResponse.getCode()==200){
                        user=userResponse.getUser();
                        binding.txtFullName.setText(user.getFullName());
                        binding.txtPosition.setText(user.getPosition().getNamePosition());
                        if(user.getAvatar().length()>0){
                            Glide.with(getContext()).load(user.getAvatar())
                                    .error(R.drawable.item1)
                                    .placeholder(R.drawable.item1)
                                    .into(binding.imgAvatarUser);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}