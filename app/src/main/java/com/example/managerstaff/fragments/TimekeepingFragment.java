package com.example.managerstaff.fragments;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.managerstaff.R;
import com.example.managerstaff.adapter.SlidePostAdapter;
import com.example.managerstaff.adapter.TimeKeepingAdapter;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.FragmentTimekeepingBinding;
import com.example.managerstaff.models.Setting;
import com.example.managerstaff.models.StatisticalTimeUser;
import com.example.managerstaff.models.User;
import com.example.managerstaff.models.responses.ListPostResponse;
import com.example.managerstaff.models.responses.SettingResponse;
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
    private Setting setting;
    private TimeKeepingAdapter adapter;
    private List<StatisticalTimeUser> statisticalTimeUserList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding=FragmentTimekeepingBinding.inflate(inflater, container, false);
        IdUser=getArguments().getInt("id_user");
        user=new User();
        setting=new Setting();
        FragmentActivity fragmentActivity = getActivity();
        adapter=new TimeKeepingAdapter(fragmentActivity);
        adapter.setOnClickListener(position -> {
            Fragment belowFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
            if (belowFragment == null || !belowFragment.isVisible()) {
                binding.fragmentContainer.setVisibility(View.VISIBLE);
                TimeKeepingDetailFragment fragment = new TimeKeepingDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putInt("id_user", this.IdUser);
                bundle.putString("time_day", this.statisticalTimeUserList.get(position).getDayOfWeek());
                bundle.putString("name_day", this.statisticalTimeUserList.get(position).getDayOfWeekName());
                fragment.setArguments(bundle);
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right);
                transaction.replace(R.id.fragment_container, fragment);
                transaction.addToBackStack(null);
                transaction.commit();
            }
        });

        adapter.setIdUser(IdUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        binding.rcvListMonth.setLayoutManager(linearLayoutManager);
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        setDataMonthYear();
        upDateTableTimeKeeping();

        List<String> months = new ArrayList<>();
        int positionYear=-1,positionMonth=-1;
        for (int i = 1; i <= 12; i++) {
            months.add(String.valueOf(i));
            if(i<=month) positionMonth++;
        }

        List<String> years = new ArrayList<>();
        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2000; i <= currentYear; i++) {
            years.add(String.valueOf(i));
            if(i<=year) positionYear++;
        }

        ArrayAdapter<String> monthAdapter = new ArrayAdapter<>(requireContext(),  R.layout.item_spinner, months);
        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(requireContext(),  R.layout.item_spinner, years);

        monthAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.spMonth.setAdapter(monthAdapter);
        binding.spYear.setAdapter(yearAdapter);
        binding.spMonth.setSelection(positionMonth);
        binding.spYear.setSelection(positionYear);

        binding.spMonth.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedMonth = parentView.getItemAtPosition(position).toString();
                binding.spMonth.setSelection(position);
                month=Integer.parseInt(selectedMonth);
                setDataMonthYear();
                upDateTableTimeKeeping();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        binding.spYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                // Xử lý khi một mục được chọn trong Spinner
                String selectedYear = parentView.getItemAtPosition(position).toString();
                binding.spYear.setSelection(position);
                year=Integer.parseInt(selectedYear);
                setDataMonthYear();
                upDateTableTimeKeeping();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        return binding.getRoot();
    }

    private void setDataMonthYear(){
        binding.txtNumberMonth.setText(String.valueOf(month));
        binding.txtNumberYear.setText(String.valueOf(year));
    }

    @Override
    public void onResume() {
        super.onResume();
        Fragment belowFragment = getActivity().getSupportFragmentManager().findFragmentById(R.id.fragment_container);
        if (belowFragment != null && belowFragment.isVisible()) {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            fragmentManager.popBackStack();
        }

        binding.fragmentContainer.setVisibility(View.GONE);
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        int positionYear=-1,positionMonth=-1;
        for (int i = 1; i <= 12; i++) {
            if(i<=month) positionMonth++;
        }

        int currentYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2000; i <= currentYear; i++) {
            if(i<=year) positionYear++;
        }
        binding.spMonth.setSelection(positionMonth);
        binding.spYear.setSelection(positionYear);
        setDataMonthYear();
        upDateTableTimeKeeping();
    }

    private void upDateTableTimeKeeping(){
        statisticalTimeUserList=new ArrayList<>();
        statisticalTimeUserList.addAll(Support.getDatesInMonth(year,month));
        String start_day=statisticalTimeUserList.get(0).getDayOfWeek();
        String end_day=statisticalTimeUserList.get(statisticalTimeUserList.size()-1).getDayOfWeek();
        if(start_day.length()>0 && end_day.length()>0 && isAdded()){
            clickCallApiGetTimeUser(start_day,end_day);
        }else{
            Toast.makeText(getContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
        }
    }

    private void clickCallApiGetTimeUser(String start_day,String end_day) {
        binding.pbLoadData.setVisibility(View.VISIBLE);
        ApiService.apiService.getTimeKeeping(Support.getAuthorization(requireActivity()),IdUser,start_day,end_day).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if(userResponse.getCode()==200){
                        user=userResponse.getUser();
                        clickCallApiGetSetting(user);
                    }else{
                        if(userResponse.getCode()==401){
                            Support.showDialogWarningExpiredAu(requireActivity());
                        }else{
                            Toast.makeText(requireContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
        binding.pbLoadData.setVisibility(View.GONE);
    }

    private void clickCallApiGetSetting(User userS) {
        binding.pbLoadData.setVisibility(View.VISIBLE);
        ApiService.apiService.getSetting(Support.getAuthorization(requireActivity())).enqueue(new Callback<SettingResponse>() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onResponse(Call<SettingResponse> call, Response<SettingResponse> response) {
                SettingResponse settingResponse = response.body();
                if (settingResponse != null) {
                    if(settingResponse.getCode()==200){
                        setting=settingResponse.getSetting();
                        int countWorkDay=0,countLate=0;
                        double wageOfMonth=0;
                        for(int i=0;i<statisticalTimeUserList.size();i++){
                            for(int j=0;j<userS.getListTimeIns().size();j++){
                                if(statisticalTimeUserList.get(i).getDayOfWeek().equals(userS.getListTimeIns().get(j).getDayIn())){
                                    statisticalTimeUserList.get(i).setDayOff(false);
                                    double w=Support.getWageOfDay(userS,statisticalTimeUserList.get(i).getDayOfWeek(),setting,statisticalTimeUserList.get(i).getDayOfWeekName());
                                    statisticalTimeUserList.get(i).setWage(w);
                                    if(userS.getListTimeIns().get(j).getTimeIn().compareTo(setting.getTimeStart())>0) countLate++;
                                    countWorkDay++;
                                    wageOfMonth+=w;
                                    break;
                                }
                            }
                        }
                        int countDayOff=statisticalTimeUserList.size()-countWorkDay;
                        binding.txtNumberOfWorkingDays.setText(countWorkDay+"");
                        binding.txtWageOfDay.setText(Support.formatWage(String.valueOf((int) userS.getWage()))+" VND");
                        binding.txtNumberOfTimesLate.setText(countLate+"");
                        binding.txtWageOfMonth.setText(Support.formatWage(String.valueOf((int) wageOfMonth))+" VND");
                        binding.txtSomeHolidays.setText(countDayOff+"");
                        adapter.setData(statisticalTimeUserList);
                        binding.rcvListMonth.setAdapter(adapter);
                    }else{
                        if(settingResponse.getCode()==401){
                            Support.showDialogWarningExpiredAu(requireActivity());
                        }else{
                            Toast.makeText(requireContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(requireContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<SettingResponse> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
        binding.pbLoadData.setVisibility(View.GONE);
    }

}