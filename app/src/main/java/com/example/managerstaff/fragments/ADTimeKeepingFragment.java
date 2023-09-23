package com.example.managerstaff.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.managerstaff.R;
import com.example.managerstaff.activities.InfoUserActivity;
import com.example.managerstaff.adapter.UserAdapter;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.FragmentADTimeKeepingBinding;
import com.example.managerstaff.databinding.FragmentHomeBinding;
import com.example.managerstaff.models.User;
import com.example.managerstaff.models.responses.ListUserResponse;
import com.example.managerstaff.models.responses.UserResponse;
import com.example.managerstaff.supports.Support;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ADTimeKeepingFragment extends Fragment {

    FragmentADTimeKeepingBinding binding;
    private int IdUser;
    private List<User> listUsers,listSearchUsers;
    private UserAdapter userAdapter;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentADTimeKeepingBinding.inflate(inflater, container, false);

        IdUser=getArguments().getInt("id_user");
        listUsers=new ArrayList<>();
        listSearchUsers=new ArrayList<>();
        user=new User();

        userAdapter=new UserAdapter(requireActivity());
        userAdapter.setIdUser(IdUser);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(requireContext());
        binding.rcvListUser.setLayoutManager(linearLayoutManager);
        userAdapter.setData(listUsers);
        binding.rcvListUser.setAdapter(userAdapter);
        clickCallApiGetListUser();


        binding.edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(binding.edtSearch.getText().toString().length()>0) {
                    listSearchUsers.clear();
                    for (int i = 0; i < listUsers.size(); i++) {
                        if(Support.checkEqualsString(listUsers.get(i).getFullName(),binding.edtSearch.getText().toString())){
                            listSearchUsers.add(listUsers.get(i));
                        }
                    }
                    binding.txtNumberUser.setText(listSearchUsers.size()+" người dùng");
                    userAdapter.setData(listSearchUsers);
                }else{
                    binding.txtNumberUser.setText(listUsers.size()+" người dùng");
                    userAdapter.setData(listUsers);
                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onResume() {
        super.onResume();
        clickCallApiGetListUser();
        binding.edtSearch.setText("");
    }

    private void clickCallApiGetListUser() {
        ApiService.apiService.getListUser().enqueue(new Callback<ListUserResponse>() {
            @Override
            public void onResponse(Call<ListUserResponse> call, Response<ListUserResponse> response) {
                ListUserResponse listUserResponse = response.body();
                if (listUserResponse != null) {
                    if(listUserResponse.getCode()==200){
                        listUsers=listUserResponse.getListUsers();
                        userAdapter.setData(listUsers);
                        binding.txtNumberUser.setText(listUsers.size()+" người dùng");
                    }
                }
            }

            @Override
            public void onFailure(Call<ListUserResponse> call, Throwable t) {
                Toast.makeText(requireContext(), getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }
}