package com.example.managerstaff.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.managerstaff.R;
import com.example.managerstaff.adapter.TimeAdapter;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.FragmentTimeKeepingDetailBinding;
import com.example.managerstaff.databinding.FragmentTimekeepingBinding;
import com.example.managerstaff.models.Setting;
import com.example.managerstaff.models.User;
import com.example.managerstaff.models.responses.SettingResponse;
import com.example.managerstaff.models.responses.UserResponse;
import com.example.managerstaff.supports.Support;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeKeepingDetailFragment extends Fragment {

    FragmentTimeKeepingDetailBinding binding;
    private int IdUser;
    private User user;
    private Setting setting;
    private String timeDay,nameDay;
    private TimeAdapter adapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding= FragmentTimeKeepingDetailBinding.inflate(inflater, container, false);
        IdUser=getArguments().getInt("id_user");
        timeDay=getArguments().getString("time_day");
        nameDay=getArguments().getString("name_day");
        user=new User();
        setting=new Setting();
        adapter=new TimeAdapter(requireActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        binding.rcvListTime.setLayoutManager(linearLayoutManager);
        binding.txtRank.setText(nameDay);
        binding.txtDay.setText(timeDay);
        clickCallApiGetTimeUser(timeDay,timeDay);

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.popBackStack();
            }
        });

        return binding.getRoot();
    }

    private void clickCallApiGetTimeUser(String start_day,String end_day) {
        ApiService.apiService.getTimeKeeping(IdUser,start_day,end_day).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if(userResponse.getCode()==200){
                        user=userResponse.getUser();

                        adapter.setData(user.getListTimeIns(),user.getListTimeOuts());
                        binding.rcvListTime.setAdapter(adapter);
                        clickCallApiGetSetting(user);
                    }else{
                        Toast.makeText(getContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickCallApiGetSetting(User userS) {
        ApiService.apiService.getSetting().enqueue(new Callback<SettingResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                SettingResponse settingResponse = response.body();
                if (settingResponse != null) {
                    if(settingResponse.getCode()==200){
                        setting=settingResponse.getSetting();
                        binding.txtCoefficientWage.setText(String.valueOf(Support.getCoefficient(timeDay,setting,nameDay)));
                        binding.txtNumberHourWorking.setText(String.valueOf(Support.getNumberWorking(userS,timeDay)));
                        binding.txtWageOfDay.setText(Support.formatWage(String.valueOf((int)Support.getWageOfDay(userS,timeDay,setting,nameDay)))+" "+getString(R.string.VND));
                        binding.txtCoefficientWageOvertime.setText(String.valueOf(setting.getOvertime()));
                        binding.txtTimeStartWorking.setText(setting.getTimeStart());
                        binding.txtTimeEndWorking.setText(setting.getTimeEnd());

                    }else{
                        Toast.makeText(getContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SettingResponse> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}