package com.odiousrainbow.leftovers.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.odiousrainbow.leftovers.DataModel.Ingredient;
import com.odiousrainbow.leftovers.R;

import java.util.List;

public class CartSimpleListAdapter extends RecyclerView.Adapter<CartSimpleListAdapter.SimpleCartItemHolder> {

    private Context mContext;
    public List<Ingredient> mData;
    private SparseBooleanArray mSelectedItemsIds;
    private SparseBooleanArray itemCheckedList;

    public CartSimpleListAdapter(Context context, List<Ingredient> data){
        this.mContext = context;
        this.mData = data;
        this.mSelectedItemsIds = new SparseBooleanArray();
        this.itemCheckedList = new SparseBooleanArray();
    }


    @NonNull
    @Override
    public SimpleCartItemHolder onCreateViewHolder(@NonNull ViewGroup viewGroup,int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_cart,viewGroup,false);
        return new SimpleCartItemHolder(view);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onBindViewHolder(@NonNull final SimpleCartItemHolder simpleCartItemHolder, final int i) {
        simpleCartItemHolder.setIsRecyclable(false);
        simpleCartItemHolder.name.setText(mData.get(i).getName());
        simpleCartItemHolder.quantity.setText(mData.get(i).getQuantity() + " " + mData.get(i).getUnit());
        simpleCartItemHolder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSelection(i);
            }
        });

        simpleCartItemHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleSelection(i);
            }
        });

        simpleCartItemHolder.checkBox.setChecked(mSelectedItemsIds.get(i,false));
        Log.d("loggo", itemCheckedList.toString());
        //simpleCartItemHolder.itemView
        //        .setBackgroundColor(mSelectedItemsIds.get(i) ? Color.parseColor(mContext.getString(R.color.colorAccent)) : Color.TRANSPARENT);
        //simpleCartItemHolder.name.setTextColor(mSelectedItemsIds.get(i) ? Color.WHITE : Color.BLACK);
        //simpleCartItemHolder.quantity.setTextColor(mSelectedItemsIds.get(i) ? Color.WHITE : Color.GRAY);
    }

    public void checkView(int position, boolean value){
        if(value){
            itemCheckedList.put(position,value);
        }
        else{
            itemCheckedList.delete(position);
        }
        notifyDataSetChanged();
    }

    public void toogleCheckBox(int position){
        checkView(position,!itemCheckedList.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void selectView(int position, boolean value) {
        if (value)
            mSelectedItemsIds.put(position, value);
        else
            mSelectedItemsIds.delete(position);

        notifyDataSetChanged();
    }

    public void toggleSelection(int position) {
        toogleCheckBox(position);
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public void removeSelection() {
        mSelectedItemsIds = new SparseBooleanArray();
        notifyDataSetChanged();
    }

    public int getSelectedCount() {
        return mSelectedItemsIds.size();
    }

    public SparseBooleanArray getSelectedIds() {
        return mSelectedItemsIds;
    }

    class SimpleCartItemHolder extends RecyclerView.ViewHolder{

        TextView name;
        TextView quantity;
        RelativeLayout layout;
        CheckBox checkBox;
        boolean checked = false;

        public SimpleCartItemHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tv_name_cart_item);
            quantity = itemView.findViewById(R.id.tv_quantity_cart_item);
            layout = itemView.findViewById(R.id.cart_item_layout);
            checkBox = itemView.findViewById(R.id.rd_check);
            checkBox.setChecked(checked);
        }
    }
}
