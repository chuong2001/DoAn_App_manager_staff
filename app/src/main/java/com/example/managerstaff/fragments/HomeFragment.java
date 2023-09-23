package com.example.managerstaff.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.managerstaff.R;
import com.example.managerstaff.adapter.SlidePostAdapter;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.FragmentHomeBinding;
import com.example.managerstaff.models.Post;
import com.example.managerstaff.models.User;
import com.example.managerstaff.models.responses.ListPostResponse;
import com.example.managerstaff.models.responses.UserResponse;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.List;
import java.util.TimerTask;
import java.util.Timer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    FragmentHomeBinding binding;
    private List<Post> listPosts;
    private boolean isFragmentActive = true;
    private OnFragmentInteractionListener mListener;
    private User user;
    private Timer timer;
    private int IdUser;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        IdUser=getArguments().getInt("id_user");
        clickCallApiGetListPosts();
        clickCallApiGetUserDetail();
        return binding.getRoot();
    }

    private void clickCallApiGetUserDetail() {
        ApiService.apiService.getUserDetail(IdUser).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if(userResponse.getCode()==200){
                        user=userResponse.getUser();
                        binding.txtNameUser.setText(user.getFullName());
                        binding.txtPosition.setText(user.getPosition().getNamePosition());
                        if(user.getAvatar().length()>0){
                            Glide.with(getContext()).load(user.getAvatar())
                                    .error(R.drawable.icon_user_gray)
                                    .placeholder(R.drawable.icon_user_gray)
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

    public interface OnFragmentInteractionListener {
        void onFragment1ButtonClicked(int position);
    }

    private void clickCallApiGetListPosts() {
        ApiService.apiService.getAllPost().enqueue(new Callback<ListPostResponse>() {
            @Override
            public void onResponse(Call<ListPostResponse> call, Response<ListPostResponse> response) {
                ListPostResponse listPostResponse = response.body();
                if (listPostResponse != null) {
                    if(listPostResponse.getCode()==200){
                        listPosts=listPostResponse.getListPosts();
                        SlidePostAdapter postAdapter = new SlidePostAdapter(getContext(), listPosts,IdUser);
                        binding.myPager.setAdapter(postAdapter);
                        binding.myTablayout.setupWithViewPager(binding.myPager,true);
                    }
                }
            }

            @Override
            public void onFailure(Call<ListPostResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class The_slide_timer extends TimerTask {
        @Override
        public void run() {

            if (isFragmentActive && getActivity() != null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (binding.myPager.getCurrentItem() < listPosts.size() - 1) {
                            binding.myPager.setCurrentItem(binding.myPager.getCurrentItem() + 1);
                        } else
                            binding.myPager.setCurrentItem(0);
                    }
                });
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isFragmentActive = false;
        if (timer != null) {
            timer.cancel();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        clickCallApiGetUserDetail();
        isFragmentActive = true;
        timer = new Timer();
        timer.scheduleAtFixedRate(new The_slide_timer(),2000,3000);
    }
}