package com.example.managerstaff.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;
import com.example.managerstaff.R;
import com.example.managerstaff.fragments.TimeKeepingDetailFragment;
import com.example.managerstaff.models.StatisticalTimeUser;
import com.example.managerstaff.supports.Support;

import java.util.List;

public class TimeKeepingAdapter extends RecyclerView.Adapter<TimeKeepingAdapter.TimeKeepingViewHolder> {
    private FragmentActivity mActivity;
    private List<StatisticalTimeUser> listTimeDays;
    private int idUser=0;

    private OnClickListener onClickListener;

    public interface OnClickListener {
        void onItemClick(int position);
    }

    public void setOnClickListener(OnClickListener listener) {
        this.onClickListener = listener;
    }
    public TimeKeepingAdapter(FragmentActivity mActivity) {
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
        int p=position;
        StatisticalTimeUser statisticalTimeUser =listTimeDays.get(position);
        if(statisticalTimeUser!=null){
            holder.txtDay.setText(statisticalTimeUser.getDayOfWeek());
            holder.txtNameDay.setText(statisticalTimeUser.getDayOfWeekName());
            holder.txtWageDay.setText(Support.formatWage(String.valueOf((int) statisticalTimeUser.getWage())));
            if(statisticalTimeUser.isDayOff()){
                holder.imgIsDayOff.setImageDrawable(mActivity.getDrawable(R.drawable.icon_close));
            }else{
                holder.imgIsDayOff.setImageDrawable(mActivity.getDrawable(R.drawable.icon_done));
            }

            if(onClickListener!=null) {
                holder.layout_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onClickListener.onItemClick(p);
                    }
                });
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
        private TextView txtDay,txtNameDay,txtWageDay;
        private ImageView imgIsDayOff;

        public TimeKeepingViewHolder(@NonNull View itemView) {
            super(itemView);

            layout_item=itemView.findViewById(R.id.layout_item_timekeeping);
            txtDay=itemView.findViewById(R.id.txt_day_of_week);
            txtNameDay=itemView.findViewById(R.id.txt_day_of_week_name);
            imgIsDayOff=itemView.findViewById(R.id.img_is_day_off);
            txtWageDay=itemView.findViewById(R.id.txt_wage_of_day);

        }
    }
}