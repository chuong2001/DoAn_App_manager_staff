package com.example.managerstaff.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.managerstaff.R;
import com.example.managerstaff.adapter.SlidePostAdapter;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.FragmentNewsBinding;
import com.example.managerstaff.databinding.FragmentTimekeepingBinding;
import com.example.managerstaff.models.Post;
import com.example.managerstaff.models.User;
import com.example.managerstaff.models.responses.ListPostResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {

    FragmentNewsBinding binding;
    private List<Post> listPosts;
    private User user;
    private int IdUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentNewsBinding.inflate(inflater, container, false);
        listPosts=new ArrayList<>();
        user=new User();
        IdUser=getArguments().getInt("id_user");


        return binding.getRoot();
    }

    private void clickCallApiGetListPosts() {
        ApiService.apiService.getAllPost().enqueue(new Callback<ListPostResponse>() {
            @Override
            public void onResponse(Call<ListPostResponse> call, Response<ListPostResponse> response) {
                ListPostResponse listPostResponse = response.body();
                if (listPostResponse != null) {
                    if(listPostResponse.getCode()==200){
                        listPosts=listPostResponse.getListPosts();

                    }
                }
            }

            @Override
            public void onFailure(Call<ListPostResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}