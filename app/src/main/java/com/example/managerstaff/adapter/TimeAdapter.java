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
import androidx.recyclerview.widget.RecyclerView;

import com.example.managerstaff.R;
import com.example.managerstaff.models.StatisticalTimeUser;
import com.example.managerstaff.models.TimeIn;
import com.example.managerstaff.models.TimeOut;

import java.util.List;

public class TimeAdapter extends RecyclerView.Adapter<TimeAdapter.TimeKeepingViewHolder> {
    private Activity mActivity;

    private List<TimeIn> timeInList;
    private List<TimeOut> timeOutList;

    public TimeAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setData(List<TimeIn> timeInList,List<TimeOut> timeOutList){
        this.timeInList=timeInList;
        this.timeOutList=timeOutList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public TimeAdapter.TimeKeepingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_timekeeping_detail,parent,false);
        return new TimeAdapter.TimeKeepingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeKeepingViewHolder holder, int position) {

       if(position<timeInList.size()){
           TimeIn timeIn=timeInList.get(position);
           holder.txtTimeIn.setText(timeIn.getTimeIn());
       }

        if(position<timeOutList.size()){
            TimeOut timeOut=timeOutList.get(position);
            holder.txtTimeOut.setText(timeOut.getTimeOut());
        }
    }


    @Override
    public int getItemCount() {
        if(timeInList!=null) return timeInList.size();
        return 0;
    }
    public class  TimeKeepingViewHolder extends RecyclerView.ViewHolder{
        private TextView txtTimeIn,txtTimeOut;

        public TimeKeepingViewHolder(@NonNull View itemView) {
            super(itemView);
            txtTimeIn=itemView.findViewById(R.id.txt_time_in);
            txtTimeOut=itemView.findViewById(R.id.txt_time_out);

        }
    }
}