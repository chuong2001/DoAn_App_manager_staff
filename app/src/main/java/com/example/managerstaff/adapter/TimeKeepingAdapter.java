package com.example.managerstaff.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.managerstaff.R;
import com.example.managerstaff.models.StatisticalTimeUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TimeKeepingAdapter extends RecyclerView.Adapter<TimeKeepingAdapter.TimeKeepingViewHolder> {
    private Activity mActivity;
    private List<StatisticalTimeUser> listTimeDays;
    private int idUser=0;
    public TimeKeepingAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setData(List<StatisticalTimeUser> list){
        this.listTimeDays=list;
        notifyDataSetChanged();
    }

    public void setIdUser(int idUser){
        this.idUser=idUser;
    }

    @NonNull
    @Override
    public TimeKeepingAdapter.TimeKeepingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timekeeping,parent,false);
        return new TimeKeepingAdapter.TimeKeepingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeKeepingViewHolder holder, int position) {
        StatisticalTimeUser statisticalTimeUser =listTimeDays.get(position);
        if(statisticalTimeUser!=null){
            holder.txtDay.setText(statisticalTimeUser.getDayOfWeek());
            holder.txtNameDay.setText(statisticalTimeUser.getDayOfWeekName());
            if(statisticalTimeUser.isDayOff()){
                holder.imgIsDayOff.setImageDrawable(mActivity.getDrawable(R.drawable.icon_close));
            }else{
                holder.imgIsDayOff.setImageDrawable(mActivity.getDrawable(R.drawable.icon_done));
            }
        }
    }


    @Override
    public int getItemCount() {
        if(listTimeDays!=null) return listTimeDays.size();
        return 0;
    }
    public class  TimeKeepingViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout layout_item;
        private TextView txtDay,txtNameDay;
        private ImageView imgIsDayOff;

        public TimeKeepingViewHolder(@NonNull View itemView) {
            super(itemView);

            layout_item=itemView.findViewById(R.id.layout_item_timekeeping);
            txtDay=itemView.findViewById(R.id.txt_day_of_week);
            txtNameDay=itemView.findViewById(R.id.txt_day_of_week_name);
            imgIsDayOff=itemView.findViewById(R.id.img_is_day_off);

        }
    }
}