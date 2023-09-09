package com.example.managerstaff.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.managerstaff.R;
import com.example.managerstaff.databinding.ActivityMainBinding;
import com.example.managerstaff.databinding.ActivityPostDetailBinding;

public class PostDetailActivity extends AppCompatActivity {

    ActivityPostDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPostDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


    }
}