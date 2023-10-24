package com.example.managerstaff.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Toast;

import com.example.managerstaff.R;
import com.example.managerstaff.adapter.UserAdapter;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.ActivityManagerUserBinding;
import com.example.managerstaff.interfaces.ItemTouchHelperListener;
import com.example.managerstaff.models.User;
import com.example.managerstaff.models.responses.ListUserResponse;
import com.example.managerstaff.models.responses.ObjectResponse;
import com.example.managerstaff.supports.PaginationScrollListener;
import com.example.managerstaff.supports.RecyclerViewItemUserTouchHelper;
import com.example.managerstaff.supports.Support;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ManagerUserActivity extends AppCompatActivity implements ItemTouchHelperListener {

    ActivityManagerUserBinding binding;
    private int IdUser, IdAdmin;
    private boolean mIsLoading,showMore;
    private List<User> listUsers;
    private UserAdapter userAdapter;
    private boolean mIsLastPage;
    private String dataSearch;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityManagerUserBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        IdUser = getIntent().getIntExtra("id_user", 0);
        IdAdmin = getIntent().getIntExtra("id_admin", 0);
        listUsers = new ArrayList<>();
        showMore=true;
        mIsLoading = false;
        user = new User();
        dataSearch="";
        userAdapter = new UserAdapter(ManagerUserActivity.this);
        userAdapter.setIdUser(IdUser);
        userAdapter.setIdAdmin(IdAdmin);
        userAdapter.setAction("edit");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ManagerUserActivity.this);
        binding.rcvListUser.setLayoutManager(linearLayoutManager);
        userAdapter.setData(listUsers);
        binding.rcvListUser.setAdapter(userAdapter);
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        binding.rcvListUser.addItemDecoration(itemDecoration);
        binding.rcvListUser.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
            @Override
            public void loadMoreItems() {
                if (showMore) {
                    mIsLoading = true;
                    binding.pbLoadShowMore.setVisibility(View.VISIBLE);
                    loadNextPage();
                }
            }

            @Override
            public boolean isLoading() {
                return mIsLoading;
            }

            @Override
            public boolean isLastPage() {
                return mIsLastPage;
            }

            @Override
            public void onScrolledUp() {

            }
        });
        ItemTouchHelper.SimpleCallback simpleCallback = new RecyclerViewItemUserTouchHelper(0, ItemTouchHelper.LEFT, this);
        new ItemTouchHelper(simpleCallback).attachToRecyclerView(binding.rcvListUser);

        binding.layoutAddUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerUserActivity.this, InfoUserActivity.class);
                Bundle bndlanimation = ActivityOptions.makeCustomAnimation(ManagerUserActivity.this, R.anim.slide_in_right, R.anim.slide_out_left).toBundle();
                intent.putExtra("id_user", IdUser);
                intent.putExtra("id_user_watch", 0);
                intent.putExtra("action", "add");
                intent.putExtra("id_admin", IdAdmin);
                startActivity(intent, bndlanimation);
            }
        });

        binding.imgBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ManagerUserActivity.this, MainActivity.class);
                intent.putExtra("id_user", IdUser);
                intent.putExtra("id_admin", IdAdmin);
                intent.putExtra("position", 0);
                startActivity(intent);
                finish();
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
                if(!dataSearch.equals(binding.edtSearch.getText().toString())) {
                    dataSearch=binding.edtSearch.getText().toString();
                    userAdapter.resetData();
                    showMore = true;
                    clickCallApiGetListUser();
                }
            }
        });
        clickCallApiGetListUser();
    }

    private void clickCallApiGetListUser() {
        if (userAdapter.getListUsers().size() == 0)
            binding.pbLoadData.setVisibility(View.VISIBLE);
        ApiService.apiService.getListUser(Support.getAuthorization(this),userAdapter.getListUsers().size(), 16, binding.edtSearch.getText().toString()).enqueue(new Callback<ListUserResponse>() {
            @Override
            public void onResponse(Call<ListUserResponse> call, Response<ListUserResponse> response) {
                ListUserResponse listUserResponse = response.body();
                if (listUserResponse != null) {
                    if (listUserResponse.getCode() == 200) {
                        if(listUserResponse.getMessage().equals("Success")) {
                            List<User> list = listUserResponse.getListUsers();
                            if (list.size() == 0) showMore = false;
                            if (userAdapter.getListUsers().size() > 0) {
                                userAdapter.addAllData(list);
                            } else {
                                userAdapter.setData(list);
                            }

                            if (userAdapter.getListUsers().size() > 0) {
                                binding.txtNoData.setVisibility(View.GONE);
                                binding.imgNoData.setVisibility(View.GONE);
                            } else {
                                binding.txtNoData.setVisibility(View.VISIBLE);
                                binding.imgNoData.setVisibility(View.VISIBLE);
                            }

                            binding.txtNumberUser.setText(userAdapter.getListUsers().size() + " nhân viên");
                        }else{
                            Support.logOutExpiredToken(ManagerUserActivity.this);
                        }
                    }else{
                        if(listUserResponse.getCode()==401){
                            Support.showDialogWarningExpiredAu(ManagerUserActivity.this);
                        }else{
                            Toast.makeText(ManagerUserActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    Toast.makeText(ManagerUserActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                }
                binding.pbLoadData.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ListUserResponse> call, Throwable t) {
                binding.pbLoadData.setVisibility(View.GONE);
                Toast.makeText(ManagerUserActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void clickCallApiDeleteUser(User userDelete) {
        binding.pbLoadData.setVisibility(View.VISIBLE);
        ApiService.apiService.deleteUser(Support.getAuthorization(this),userDelete.getIdUser()).enqueue(new Callback<ObjectResponse>() {
            @Override
            public void onResponse(Call<ObjectResponse> call, Response<ObjectResponse> response) {
                ObjectResponse objectResponse = response.body();
                if (objectResponse != null) {
                    if (objectResponse.getCode() == 200) {
                        userAdapter.removeData(userDelete);
                        if (userAdapter.getListUsers().size() > 0) {
                            binding.txtNoData.setVisibility(View.GONE);
                            binding.imgNoData.setVisibility(View.GONE);
                        } else {
                            binding.txtNoData.setVisibility(View.VISIBLE);
                            binding.imgNoData.setVisibility(View.VISIBLE);
                        }
                        binding.txtNumberUser.setText(userAdapter.getListUsers().size() + " nhân viên");
                        Toast.makeText(ManagerUserActivity.this, "Xoá thành công!", Toast.LENGTH_SHORT).show();
                    } else{
                        if(objectResponse.getCode()==401){
                            Support.showDialogWarningExpiredAu(ManagerUserActivity.this);
                        }else{
                            userAdapter.notifyDataSetChanged();
                            Toast.makeText(ManagerUserActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                        }
                    }
                } else {
                    userAdapter.notifyDataSetChanged();
                    Toast.makeText(ManagerUserActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
                }
                binding.pbLoadData.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(Call<ObjectResponse> call, Throwable t) {
                binding.pbLoadData.setVisibility(View.GONE);
                Toast.makeText(ManagerUserActivity.this, getString(R.string.system_error), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder) {
        if (viewHolder instanceof UserAdapter.UserViewHolder) {

            int indexDelete = viewHolder.getAdapterPosition();
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
            alertDialog.setMessage("Bạn có muốn xoá tài khoản nhân viên không?");
            User userDelete = userAdapter.getListUsers().get(indexDelete);
            alertDialog.setPositiveButton("Có", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    clickCallApiDeleteUser(userDelete);
                }
            });
            alertDialog.setNegativeButton("Không", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    userAdapter.setData(listUsers);
                }
            });
            alertDialog.show();

        }
    }

    private void loadNextPage() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mIsLoading = false;
                clickCallApiGetListUser();
                binding.pbLoadShowMore.setVisibility(View.GONE);
            }
        }, 1000);
    }
}