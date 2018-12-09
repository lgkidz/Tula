package com.odiousrainbow.leftovers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.odiousrainbow.leftovers.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

public class NotificationListAdapter extends RecyclerView.Adapter<NotificationListAdapter.NotificationItemViewHolder> {
    private Context mContext;
    private List<Map<String,String>> mData;

    public NotificationListAdapter(Context context, List<Map<String,String>> data){
        this.mContext = context;
        this.mData = data;
    }

    @NonNull
    @Override
    public NotificationItemViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.noti_item,viewGroup,false);
        return new NotificationItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationItemViewHolder notificationItemViewHolder, int i) {
            notificationItemViewHolder.tv_name.setText(mData.get(i).get("iName"));
            notificationItemViewHolder.tv_quan.setText(mData.get(i).get("iQuan") + " " + mData.get(i).get("iUnit"));
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Calendar curDate = Calendar.getInstance();
            Calendar expDate = Calendar.getInstance();
            try {
                expDate.setTime(dateFormat.parse(mData.get(i).get("iExpDate")));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            long diff = expDate.getTimeInMillis() - curDate.getTimeInMillis();
            float dayCount = (float) diff / (24 * 60 * 60 * 1000);
            if((int)dayCount <= curDate.getActualMaximum(Calendar.DAY_OF_MONTH)){
                if((int) dayCount < 0){
                    notificationItemViewHolder.tv_days_left.setText("Quá thời gian dùng tốt nhất!");
                }
                else if((int) dayCount == 0){
                    notificationItemViewHolder.tv_days_left.setText("Nên dùng trong ngày");
                }
                else{
                    notificationItemViewHolder.tv_days_left.setText("Còn " + (int) dayCount + " ngày");
                }
            }
            else if((int)dayCount/365 > 0){
                notificationItemViewHolder.tv_days_left.setText("Còn khoảng " + Math.round(dayCount/365)  + " năm");
            }
            else{
                notificationItemViewHolder.tv_days_left.setText("Còn khoảng " + Math.round(dayCount/30)  + " tháng");
            }

    }


    @Override
    public int getItemCount() {
        return mData.size();
    }


    class NotificationItemViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name;
        private TextView tv_quan;
        private TextView tv_days_left;

        public NotificationItemViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_name = itemView.findViewById(R.id.noti_name);
            tv_quan = itemView.findViewById(R.id.noti_quan);
            tv_days_left = itemView.findViewById(R.id.noti_days_left);

        }
    }


}
