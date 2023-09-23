package com.example.managerstaff.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.managerstaff.R;
import com.example.managerstaff.models.CalendarA;

import java.util.List;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.TimeKeepingViewHolder> {
    private Activity mActivity;
    private List<CalendarA> listCalendarAS;
    private int idUser=0;

    private CalendarAdapter.OnClickListener onClickListener;

    public interface OnClickListener {
        void onItemClick(int position);
    }

    public void setOnClickListener(CalendarAdapter.OnClickListener listener) {
        this.onClickListener = listener;
    }

    public CalendarAdapter(Activity mActivity) {
        this.mActivity = mActivity;
    }

    public void setData(List<CalendarA> listCalendarAS){
        this.listCalendarAS = listCalendarAS;
        notifyDataSetChanged();
    }

    public void setIdUser(int idUser){
        this.idUser=idUser;
    }

    @NonNull
    @Override
    public TimeKeepingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_calendar,parent,false);
        return new TimeKeepingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TimeKeepingViewHolder holder, int position) {
        int p =position;
        CalendarA calendarA = listCalendarAS.get(position);
        if(calendarA !=null){
            holder.txtTimeStart.setText(calendarA.getTimeStart().substring(0, calendarA.getTimeStart().length()-3));
            holder.txtTimeEnd.setText(calendarA.getTimeEnd().substring(0, calendarA.getTimeEnd().length()-3));
            holder.txtHeader.setText(calendarA.getHeaderCalendar());
            holder.txtTypeCalendar.setText(calendarA.getTypeCalendar());

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
        if(listCalendarAS !=null) return listCalendarAS.size();
        return 0;
    }
    public class  TimeKeepingViewHolder extends RecyclerView.ViewHolder{
        private ConstraintLayout layout_item;
        private TextView txtTimeStart,txtTimeEnd,txtHeader,txtTypeCalendar;

        public TimeKeepingViewHolder(@NonNull View itemView) {
            super(itemView);

            layout_item=itemView.findViewById(R.id.layout_item_calendar);
            txtTimeStart=itemView.findViewById(R.id.txt_time_start);
            txtTimeEnd=itemView.findViewById(R.id.txt_time_end);
            txtHeader=itemView.findViewById(R.id.txt_header_calendar);
            txtTypeCalendar=itemView.findViewById(R.id.txt_type_calendar);

        }
    }
}