package com.odiousrainbow.leftovers.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.odiousrainbow.leftovers.Activities.EditStuffDetailsActivity;
import com.odiousrainbow.leftovers.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TulaListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<Map<String,String>> mRawData;
    public List<Map<String,String>> mData;
    private List<String> headerList;
    Map<String,Integer> totalitemsPerHeader;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    public  TulaListAdapter(Context context, List<Map<String,String>> data){
        this.mContext = context;
        this.mRawData = data;
        this.headerList = new ArrayList<>();
        this.mData = new ArrayList<>();
        this.totalitemsPerHeader = new HashMap<>();
        for(int i = 0;i < mRawData.size();i++){
            String cate = mRawData.get(i).get("iCate");
            if(!headerList.contains(cate)){
                headerList.add(cate);
                totalitemsPerHeader.put(cate,1);
            }
            else{
                totalitemsPerHeader.put(cate,totalitemsPerHeader.get(cate) + 1);
            }
        }
        for(int i = 0;i<headerList.size();i++){
            //Log.d("thedamnlist", headerList.get(i));
            Map<String,String> m = new HashMap<>();
            m.put("iCate",headerList.get(i));
            m.put("iName","HeaderIndicator");

            mData.add(m);
            for(int j = 0;j< mRawData.size();j++){
                if(mRawData.get(j).get("iCate").equals(headerList.get(i))){
                    mData.add(mRawData.get(j));
                }
            }
        }
        Log.d("thedamnlist", mRawData.toString());
        for(Map<String,String> m : mData){
            Log.d("thedamnlist", m.toString());
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        if(viewType == TYPE_HEADER){
            View view = LayoutInflater.from(mContext).inflate(R.layout.tula_stuff_cate,viewGroup,false);
            return new TulaStuffHeaderViewHolder(view);
        }
        else if(viewType == TYPE_ITEM){
            View view  = LayoutInflater.from(mContext).inflate(R.layout.tula_stuff_item,viewGroup,false);
            return new TulaStuffItemViewHolder(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        if(viewHolder instanceof TulaStuffHeaderViewHolder){
            String header = mData.get(i).get("iCate");
            Log.d("thedamnlist", header);
            ((TulaStuffHeaderViewHolder) viewHolder).header.setText(header);
        }
        else if(viewHolder instanceof TulaStuffItemViewHolder){
            final String iCate = mData.get(i).get("iCate");
            final String iName = mData.get(i).get("iName");
            final String iQuan = mData.get(i).get("iQuan");
            final String iUnit = mData.get(i).get("iUnit");
            final String iExpDate = mData.get(i).get("iExpDate");
            final String iNoti = mData.get(i).get("iNoti");
            Log.d("thedamnlist", iName);
            ((TulaStuffItemViewHolder) viewHolder).setData(iName,iQuan,iUnit,iExpDate,iNoti);
            ((TulaStuffItemViewHolder) viewHolder).itemInfoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intentEditStuffDetails = new Intent(mContext,EditStuffDetailsActivity.class);
                    intentEditStuffDetails.putExtra("ingreName",iName);
                    intentEditStuffDetails.putExtra("ingreCate",iCate);
                    intentEditStuffDetails.putExtra("ingreQuan",iQuan);
                    intentEditStuffDetails.putExtra("ingreUnit",iUnit);
                    intentEditStuffDetails.putExtra("ingreExpDate",iExpDate);
                    intentEditStuffDetails.putExtra("ingreNoti",iNoti);
                    mContext.startActivity(intentEditStuffDetails);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(mData.get(position).get("iName").equals("HeaderIndicator")){
            return TYPE_HEADER;
        }
        return TYPE_ITEM;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void removeItem(int position){
        mData.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Map<String,String> item, int position){
        mData.add(position,item);
        notifyItemInserted(position);
    }

    public class TulaStuffHeaderViewHolder extends RecyclerView.ViewHolder{
        private TextView header;
        public TulaStuffHeaderViewHolder(@NonNull View itemView) {
            super(itemView);
            header = itemView.findViewById(R.id.tula_stuff_cate);
        }
    }

    public class TulaStuffItemViewHolder extends RecyclerView.ViewHolder{
        public ConstraintLayout frontLayout;
        public RelativeLayout backLayout;
        public TextView itemName;
        public TextView itemQuan;
        public ImageView itemInfoButton;
        public String iExpDate;
        public String iNoti;

        public TulaStuffItemViewHolder(@NonNull View itemView) {
            super(itemView);
            frontLayout = itemView.findViewById(R.id.tula_stuff_item_layout);
            backLayout = itemView.findViewById(R.id.tula_stuff_item_background);
            itemName = itemView.findViewById(R.id.tula_stuff_item_name);
            itemQuan = itemView.findViewById(R.id.tula_stuff_item_quan);
            itemInfoButton = itemView.findViewById(R.id.tula_stuff_item_info);
        }

        public void setData(String name,String quantity,String unit, String expDate, String noti){
            this.itemName.setText(name);
            this.itemQuan.setText(quantity + " "  +unit);
            this.iExpDate = expDate;
            this.iNoti = noti;
        }
    }
}
