package com.example.managerstaff.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.managerstaff.R;
import com.example.managerstaff.adapter.SlidePostAdapter;
import com.example.managerstaff.adapter.TimeKeepingAdapter;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.FragmentTimekeepingBinding;
import com.example.managerstaff.models.StatisticalTimeUser;
import com.example.managerstaff.models.User;
import com.example.managerstaff.models.responses.ListPostResponse;
import com.example.managerstaff.models.responses.UserResponse;
import com.example.managerstaff.supports.Support;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimekeepingFragment extends Fragment {

    FragmentTimekeepingBinding binding;
    private int month=0;
    private int year=0;
    private int IdUser;
    private User user;
    private TimeKeepingAdapter adapter;
    private List<StatisticalTimeUser> statisticalTimeUserList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentTimekeepingBinding.inflate(inflater, container, false);
        IdUser=getArguments().getInt("id_user");
        user=new User();
        adapter=new TimeKeepingAdapter(getActivity());
        adapter.setIdUser(IdUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        binding.rcvListMonth.setLayoutManager(linearLayoutManager);
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        statisticalTimeUserList=new ArrayList<>();
        statisticalTimeUserList.addAll(Support.getDatesInMonth(year,month));

        String start_day=Support.changeReverDateTime(statisticalTimeUserList.get(0).getDayOfWeek(),true);
        String end_day=Support.changeReverDateTime(statisticalTimeUserList.get(statisticalTimeUserList.size()-1).getDayOfWeek(),true);
        if(start_day.length()>0 && end_day.length()>0){
            clickCallApiGetTimeUser(start_day,end_day);
        }else{
            Toast.makeText(getContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
        }
        binding.rcvListMonth.setAdapter(adapter);
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
                        int countWorkDay=0;
                        double wageOfMonth=0;
                        for(int i=0;i<statisticalTimeUserList.size();i++){
                            for(int j=0;j<user.getListTimeIns().size();j++){
                                if(statisticalTimeUserList.get(i).getDayOfWeek().equals(user.getListTimeIns().get(j).getDayIn())){
                                    statisticalTimeUserList.get(i).setDayOff(false);
                                    countWorkDay++;
                                    break;
                                }
                            }
                        }
                        wageOfMonth=countWorkDay*user.getWage();
                        int countDayOff=statisticalTimeUserList.size()-countWorkDay;
                        binding.txtNumberOfWorkingDays.setText(countWorkDay+"");
                        binding.txtWageOfDay.setText(user.getWage()+"");
                        binding.txtWageOfMonth.setText(wageOfMonth+"");
                        binding.txtSomeHolidays.setText(countDayOff+"");
                        adapter.setData(statisticalTimeUserList);
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

}