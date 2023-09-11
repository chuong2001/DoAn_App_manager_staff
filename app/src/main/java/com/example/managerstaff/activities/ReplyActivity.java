package com.example.managerstaff.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import com.example.managerstaff.databinding.ActivityReplyBinding;

public class ReplyActivity extends AppCompatActivity {

    ActivityReplyBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityReplyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

    }
}