package com.example.managerstaff.adapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.managerstaff.R;
import com.example.managerstaff.fragments.CalendarFragment;
import com.example.managerstaff.fragments.HomeFragment;
import com.example.managerstaff.fragments.NewsFragment;
import com.example.managerstaff.fragments.TimekeepingFragment;
import com.example.managerstaff.fragments.UserFragment;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private int IdUser;

    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    public void setIdUser(int IdUser){
        this.IdUser=IdUser;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        Bundle bundle = new Bundle();
        bundle.putInt("id_user", this.IdUser);
        HomeFragment fragHome = new HomeFragment();
        fragHome.setArguments(bundle);
        CalendarFragment fragCalendar = new CalendarFragment();
        TimekeepingFragment fragTimekeeping = new TimekeepingFragment();
        NewsFragment fragNews = new NewsFragment();
        UserFragment fragUser = new UserFragment();
        fragUser.setArguments(bundle);


        switch (position) {
            case 0:
                return fragHome;
            case 1:
                return fragTimekeeping;
            case 2:
                return fragCalendar;
            case 3:
                return fragNews;
            case 4:
                return fragUser;
        }
        return fragHome;
    }

    @Override
    public int getCount() {
        return 5;
    }

//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        String title="";
//        switch (position){
//            case 0:
//                title = "Trang chủ";
//            case 1:
//                title = "Chấm công";
//            case 2:
//                title = "Lịch";
//            case 3:
//                title = "Tin tức";
//            case 4:
//                title = "Cá nhân";
//            default:
//                title = "Trang chủ";
//        }
//        return title;
//    }
}
