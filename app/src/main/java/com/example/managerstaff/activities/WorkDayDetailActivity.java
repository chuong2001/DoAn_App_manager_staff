package com.example.managerstaff.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.managerstaff.R;
import com.example.managerstaff.databinding.ActivityWorkDayDetailBinding;

public class WorkDayDetailActivity extends AppCompatActivity {

    ActivityWorkDayDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityWorkDayDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());



    }
}