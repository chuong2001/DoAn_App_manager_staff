package com.example.managerstaff.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.managerstaff.R;
import com.example.managerstaff.adapter.ViewPagerAdapter;
import com.example.managerstaff.api.ApiService;
import com.example.managerstaff.databinding.ActivityMainBinding;
import com.example.managerstaff.fragments.HomeFragment;
import com.example.managerstaff.models.User;
import com.example.managerstaff.models.responses.UserResponse;
import com.example.managerstaff.supports.Support;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements HomeFragment.OnFragmentInteractionListener {

    ActivityMainBinding binding;
    private int IdUser,p,IdAdmin;
    private User user;
    private ViewPagerAdapter viewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        IdUser = getIntent().getIntExtra("id_user", 0);
        IdAdmin = getIntent().getIntExtra("id_admin", 0);
        p = getIntent().getIntExtra("position", -1);
        user=new User();
        viewPagerAdapter=new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPagerAdapter.setIdUser(IdUser);
        viewPagerAdapter.setIdAdmin(IdAdmin);
        binding.viewPager.setAdapter(viewPagerAdapter);
        setFinish();
        binding.viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                switch (position){
                    case 0:
                        binding.bottomMenuApp.getMenu().findItem(R.id.item_home).setChecked(true);
                        break;
                    case 1:
                        binding.bottomMenuApp.getMenu().findItem(R.id.item_timekeeping).setChecked(true);
                        break;
                    case 2:
                        binding.bottomMenuApp.getMenu().findItem(R.id.item_calendar).setChecked(true);
                        break;
                    case 3:
                        binding.bottomMenuApp.getMenu().findItem(R.id.item_news).setChecked(true);
                        break;
                    case 4:
                        binding.bottomMenuApp.getMenu().findItem(R.id.item_user).setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        binding.bottomMenuApp.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                if(item.getItemId()==R.id.item_home){
                    binding.viewPager.setCurrentItem(0);
                }
                if(item.getItemId()==R.id.item_timekeeping){
                    binding.viewPager.setCurrentItem(1);
                }
                if(item.getItemId()==R.id.item_calendar){
                    binding.viewPager.setCurrentItem(2);
                }
                if(item.getItemId()==R.id.item_news){
                    binding.viewPager.setCurrentItem(3);
                }
                if(item.getItemId()==R.id.item_user){
                    binding.viewPager.setCurrentItem(4);
                }
                return true;
            }
        });

        viewPagerAdapter.notifyDataSetChanged();
    }

    @Override
    public void onFragment1ButtonClicked(int position) {
        binding.viewPager.setCurrentItem(position);
        switch (position){
            case 0:
                binding.bottomMenuApp.getMenu().findItem(R.id.item_home).setChecked(true);
                break;
            case 1:
                binding.bottomMenuApp.getMenu().findItem(R.id.item_timekeeping).setChecked(true);
                break;
            case 2:
                binding.bottomMenuApp.getMenu().findItem(R.id.item_calendar).setChecked(true);
                break;
            case 3:
                binding.bottomMenuApp.getMenu().findItem(R.id.item_news).setChecked(true);
                break;
            case 4:
                binding.bottomMenuApp.getMenu().findItem(R.id.item_user).setChecked(true);
                break;
        }
    }

    private void setFinish(){
        binding.viewPager.setCurrentItem(p);
        switch (p){
            case 0:
                binding.bottomMenuApp.getMenu().findItem(R.id.item_home).setChecked(true);
                break;
            case 1:
                binding.bottomMenuApp.getMenu().findItem(R.id.item_timekeeping).setChecked(true);
                break;
            case 2:
                binding.bottomMenuApp.getMenu().findItem(R.id.item_calendar).setChecked(true);
                break;
            case 3:
                binding.bottomMenuApp.getMenu().findItem(R.id.item_news).setChecked(true);
                break;
            case 4:
                binding.bottomMenuApp.getMenu().findItem(R.id.item_user).setChecked(true);
                break;
        }
    }
}