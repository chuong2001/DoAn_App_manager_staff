package com.example.managerstaff.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.managerstaff.R;
import com.example.managerstaff.adapter.PostAdapter;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.ActivityNewsBinding;
import com.example.managerstaff.models.Post;
import com.example.managerstaff.models.User;
import com.example.managerstaff.models.responses.ListPostResponse;
import com.example.managerstaff.supports.Support;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsActivity extends AppCompatActivity {

    ActivityNewsBinding binding;
    private List<Post> listPosts,listPostSearchs;
    private List<String> listTypePosts;
    private User user;
    private PostAdapter adapter;
    private String timeStartFilter,timeEndFilter,typeFilter;
    private int IdUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityNewsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        listPosts=new ArrayList<>();
        listPostSearchs=new ArrayList<>();
        listTypePosts=new ArrayList<>();
        timeStartFilter="";
        timeEndFilter="";
        typeFilter="";
        user=new User();
        adapter=new PostAdapter(NewsActivity.this);
        IdUser = getIntent().getIntExtra("id_user", 0);
        adapter.setIdUser(IdUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(NewsActivity.this);
        binding.rcvListNews.setLayoutManager(linearLayoutManager);
        clickCallApiGetListPosts();

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.imgFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogFilterNews();
            }
        });

        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                clickCallApiGetListPosts();

            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        timeStartFilter="";
        timeEndFilter="";
        typeFilter="";
        clickCallApiGetListPosts();
        binding.edtSearch.setText("");
    }

    private void showDialogFilterNews(){
        final Dialog dialog = new Dialog(NewsActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_filter_post);

        String type = "Tất cả";
        String time= Support.changeReverDateTime(Support.geDayNow(),false);
        TextView txtTimeStart=dialog.findViewById(R.id.txt_time_start_post);
        TextView txtTimeEnd=dialog.findViewById(R.id.txt_time_end_post);
        TextView txtTypePost=dialog.findViewById(R.id.txt_type_post);
        ImageView imgClockStart=dialog.findViewById(R.id.img_show_clock_time_start_post);
        ImageView imgClockEnd=dialog.findViewById(R.id.img_show_clock_time_end_post);
        Spinner spTypePost=dialog.findViewById(R.id.sp_type_post);
        ImageView imgCancel=dialog.findViewById(R.id.img_cancel);
        Button btnFilter=dialog.findViewById(R.id.btn_filter);
        Button btnUnFilter=dialog.findViewById(R.id.btn_unfilter);
        ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(NewsActivity.this, android.R.layout.simple_spinner_item, listTypePosts);
        typeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spTypePost.setAdapter(typeAdapter);
        spTypePost.setSelection(0);
        txtTimeStart.setText((timeStartFilter.length()>0)?timeStartFilter:time);
        txtTimeEnd.setText((timeEndFilter.length()>0)?timeEndFilter:time);
        txtTypePost.setText((typeFilter.length()>0)?typeFilter:type);

        spTypePost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                String selectedType = parentView.getItemAtPosition(position).toString();
                spTypePost.setSelection(position);
                txtTypePost.setText(selectedType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        txtTypePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spTypePost.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                        String selectedType = parentView.getItemAtPosition(position).toString();
                        spTypePost.setSelection(position);
                        txtTypePost.setText(selectedType);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parentView) {
                    }
                });
            }
        });

        btnUnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStartFilter="";
                timeEndFilter="";
                typeFilter="";
                clickCallApiGetListPosts();
                dialog.dismiss();
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeStartFilter=txtTimeStart.getText().toString();
                timeEndFilter=txtTimeEnd.getText().toString();
                typeFilter=txtTypePost.getText().toString();
                listPosts=Support.filterPost(listPosts,timeStartFilter,timeEndFilter,typeFilter);
                adapter.setData(listPosts);
                binding.txtNumberNews.setText(listPosts.size()+" tin tức");
                dialog.dismiss();
            }
        });

        imgClockStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar selectedDate = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        NewsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                                String timeS=Support.getFormatString(day,month+1,year);

                                if(Support.compareToDate(timeS,txtTimeEnd.getText().toString())<=0){
                                    selectedDate.set(Calendar.YEAR, year);
                                    selectedDate.set(Calendar.MONTH, month);
                                    selectedDate.set(Calendar.DAY_OF_MONTH, day);

                                    txtTimeStart.setText(timeS);
                                }else{
                                    Toast.makeText(NewsActivity.this, "Thời gian không hợp lệ", Toast.LENGTH_SHORT).show();
                                }


                            }
                        },
                        Integer.parseInt(txtTimeStart.getText().toString().substring(6)),
                        Integer.parseInt(txtTimeStart.getText().toString().substring(3,5))-1,
                        Integer.parseInt(txtTimeStart.getText().toString().substring(0,2))
                );
                datePickerDialog.show();
            }
        });

        imgClockEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar selectedDate = Calendar.getInstance();
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        NewsActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                String timeE=Support.getFormatString(day,month+1,year);

                                if(Support.compareToDate(timeE,txtTimeEnd.getText().toString())>=0){
                                    selectedDate.set(Calendar.YEAR, year);
                                    selectedDate.set(Calendar.MONTH, month);
                                    selectedDate.set(Calendar.DAY_OF_MONTH, day);

                                    txtTimeEnd.setText(timeE);
                                }else{
                                    Toast.makeText(NewsActivity.this, "Thời gian không hợp lệ", Toast.LENGTH_SHORT).show();
                                }
                            }
                        },
                        Integer.parseInt(txtTimeEnd.getText().toString().substring(6)),
                        Integer.parseInt(txtTimeEnd.getText().toString().substring(3,5))-1,
                        Integer.parseInt(txtTimeEnd.getText().toString().substring(0,2))
                );
                datePickerDialog.show();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();

    }


    private void clickCallApiGetListPosts() {
        ApiService.apiService.getAllPost().enqueue(new Callback<ListPostResponse>() {
            @Override
            public void onResponse(Call<ListPostResponse> call, Response<ListPostResponse> response) {
                ListPostResponse listPostResponse = response.body();
                if (listPostResponse != null) {
                    if(listPostResponse.getCode()==200){
                        listPosts=listPostResponse.getListPosts();
                        listTypePosts=Support.getListTypePost(listPosts);
                        listPosts=Support.searchListPosts(listPosts,binding.edtSearch.getText().toString());
                        listPosts=Support.filterPost(listPosts,timeStartFilter,timeEndFilter,typeFilter);
                        if(listPosts.size()>0){
                            binding.imgNoData.setVisibility(View.GONE);
                            binding.txtNoData.setVisibility(View.GONE);
                        }else{
                            binding.imgNoData.setVisibility(View.VISIBLE);
                            binding.txtNoData.setVisibility(View.VISIBLE);
                        }
                        adapter.setData(listPosts);
                        binding.rcvListNews.setAdapter(adapter);
                        binding.txtNumberNews.setText(listPosts.size()+" tin tức");
                    }else{
                        Toast.makeText(NewsActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(NewsActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListPostResponse> call, Throwable t) {
                Toast.makeText(NewsActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
}