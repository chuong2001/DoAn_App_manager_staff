package com.example.managerstaff.fragments;

import android.app.Dialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managerstaff.R;
import com.example.managerstaff.adapter.CalendarAdapter;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.FragmentCalendarBinding;
import com.example.managerstaff.models.CalendarA;
import com.example.managerstaff.models.User;
import com.example.managerstaff.models.responses.ListCalendarResponse;
import com.example.managerstaff.models.responses.UserResponse;
import com.example.managerstaff.supports.Support;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarFragment extends Fragment {

    FragmentCalendarBinding binding;
    private int IdUser;
    private User user;
    private CalendarAdapter adapter;
    private List<CalendarA> listCalendarAS;
    private String day;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCalendarBinding.inflate(inflater, container, false);
        user=new User();
        IdUser=getArguments().getInt("id_user");
        listCalendarAS =new ArrayList<>();
        day=Support.geDayNow();
        binding.txtDayCalendar.setText(Support.defineTime(Support.changeReverDateTime(day,false)));
        adapter=new CalendarAdapter(requireActivity());
        adapter.setOnClickListener(position -> {
            showDialogCalendarDetail(position);
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        binding.rcvListCalendar.setLayoutManager(linearLayoutManager);
        clickCallApiGetUserDetail();
        binding.datePicker1.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                month++;
                day = year+"-"+month+"-"+dayOfMonth;
                binding.txtDayCalendar.setText("Ngày "+((dayOfMonth<10)?"0":"")+dayOfMonth+" Tháng "+((month<10)?"0":"")+month);
                clickCallApiGetListCalendar(user.getPart().getIdPart());
            }
        });


        return binding.getRoot();
    }

    private void showDialogCalendarDetail(int position){
        CalendarA calendarA = listCalendarAS.get(position);
        final Dialog dialog = new Dialog(requireContext());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_calendar_detail);

        TextView txtHeaderCalendar=dialog.findViewById(R.id.txt_header);
        TextView txtTypeCalendar=dialog.findViewById(R.id.txt_type);
        TextView txtAddress=dialog.findViewById(R.id.txt_address);
        TextView txtDayCalendar=dialog.findViewById(R.id.txt_day_calendar);
        TextView txtTimeStart=dialog.findViewById(R.id.txt_time_start);
        TextView txtTimeEnd=dialog.findViewById(R.id.txt_time_end);
        TextView txtBodyCalendar=dialog.findViewById(R.id.txt_body_calendar);
        ImageView imgCancel=dialog.findViewById(R.id.img_cancel);

        txtHeaderCalendar.setText(calendarA.getHeaderCalendar());
        txtTypeCalendar.setText(calendarA.getTypeCalendar());
        txtAddress.setText(calendarA.getAddress());
        txtDayCalendar.setText(calendarA.getDayCalendar());
        txtTimeStart.setText(calendarA.getTimeStart().substring(0, calendarA.getTimeStart().length()-3));
        txtTimeEnd.setText(calendarA.getTimeEnd().substring(0, calendarA.getTimeEnd().length()-3));
        txtBodyCalendar.setText(calendarA.getBodyCalendar());

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }

    private void clickCallApiGetUserDetail() {
        ApiService.apiService.getUserDetail(IdUser).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                UserResponse userResponse = response.body();
                if (userResponse != null) {
                    if(userResponse.getCode()==200){
                        user=userResponse.getUser();
                        clickCallApiGetListCalendar(user.getPart().getIdPart());
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

    private void clickCallApiGetListCalendar(int idPart) {
        ApiService.apiService.getCalendar(idPart,day).enqueue(new Callback<ListCalendarResponse>() {
            @Override
            public void onResponse(Call<ListCalendarResponse> call, Response<ListCalendarResponse> response) {
                ListCalendarResponse listCalendarResponse = response.body();
                if (listCalendarResponse != null) {
                    if(listCalendarResponse.getCode()==200){
                        listCalendarAS =listCalendarResponse.getListCalendars();
                        adapter.setData(listCalendarAS);
                        if(listCalendarAS.size()>0){
                            binding.imgNoData.setVisibility(View.GONE);
                            binding.txtNoData.setVisibility(View.GONE);
                        }
                        else{
                            binding.imgNoData.setVisibility(View.VISIBLE);
                            binding.txtNoData.setVisibility(View.VISIBLE);
                        }
                        binding.rcvListCalendar.setAdapter(adapter);
                    }else{
                        Toast.makeText(getContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListCalendarResponse> call, Throwable t) {
                Toast.makeText(getContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

}