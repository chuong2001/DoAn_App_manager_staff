package com.example.managerstaff.fragments;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.managerstaff.R;
import com.example.managerstaff.activities.CommentActivity;
import com.example.managerstaff.activities.InfoUserActivity;
import com.example.managerstaff.activities.NewsActivity;
import com.example.managerstaff.databinding.FragmentHomeBinding;
import com.example.managerstaff.databinding.FragmentUtilitiesBinding;

public class UtilitiesFragment extends Fragment {

    FragmentUtilitiesBinding binding;
    private int IdUser, isAdmin;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentUtilitiesBinding.inflate(inflater, container, false);

        IdUser=getArguments().getInt("id_user");
        isAdmin=getArguments().getInt("is_admin");

        binding.cvFeatureShowMorePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(requireActivity(), NewsActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right,R.anim.slide_out_left).toBundle();
                intent.putExtra("id_user",IdUser);
                intent.putExtra("is_admin",isAdmin);
                startActivity(intent,bndlanimation);
            }
        });

        binding.cvFeatureShowMoreComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(requireActivity(), CommentActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(getActivity(), R.anim.slide_in_right,R.anim.slide_out_left).toBundle();
                intent.putExtra("id_user",IdUser);
                intent.putExtra("is_admin",isAdmin);
                startActivity(intent,bndlanimation);
            }
        });

        return binding.getRoot();
    }
}