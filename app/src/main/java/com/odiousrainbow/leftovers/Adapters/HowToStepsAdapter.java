package com.odiousrainbow.leftovers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import com.odiousrainbow.leftovers.R;

import java.util.List;

public class HowToStepsAdapter extends RecyclerView.Adapter<HowToStepsAdapter.HowToStepHolder> {
    private Context mContext;
    private List<String> mData;
    private SparseBooleanArray mSelectedItemsIds;

    public HowToStepsAdapter(Context context, List<String> data){
        this.mContext = context;
        this.mData = data;
        this.mSelectedItemsIds = new SparseBooleanArray();
    }


    @NonNull
    @Override
    public HowToStepHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.how_to_step_layout,viewGroup,false);
        return new HowToStepHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HowToStepHolder howToStepHolder, int i) {
        howToStepHolder.bindData(i+1,mData.get(i));
        howToStepHolder.tvStepNumber.setOnClickListener(v -> {
            toggleSelection(i);
            howToStepHolder.enlarge(mContext,mSelectedItemsIds.get(i));
            Log.d("largeLog", String.valueOf(mSelectedItemsIds.get(i)));
        });
        howToStepHolder.tvStepContent.setOnClickListener(v -> {
            toggleSelection(i);
            howToStepHolder.enlarge(mContext,mSelectedItemsIds.get(i));
            Log.d("largeLog", String.valueOf(mSelectedItemsIds.get(i)));
        });
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
        selectView(position, !mSelectedItemsIds.get(position));
    }

    public class HowToStepHolder extends RecyclerView.ViewHolder{


        private TextView tvStepNumber;
        private TextView tvStepContent;

        public HowToStepHolder(@NonNull View itemView) {
            super(itemView);
            tvStepNumber = itemView.findViewById(R.id.tv_step_number);
            tvStepContent = itemView.findViewById(R.id.tv_step_content);
        }

        public void bindData(int step, String content){
            this.tvStepNumber.setText("Bước " + step);
            this.tvStepContent.setText(content);
        }

        public void enlarge(Context context,Boolean enlarged){
            if(enlarged){
                Animation a = AnimationUtils.loadAnimation(context, R.anim.step_scale_down);
                //a.reset();
                //this.tvStepNumber.clearAnimation();
                this.tvStepNumber.startAnimation(a);
                //this.tvStepContent.clearAnimation();
                this.tvStepContent.startAnimation(a);
                Log.d("largeLog", a.toString());
                //this.tvStepNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP,22);
                //this.tvStepContent.setTextSize(TypedValue.COMPLEX_UNIT_SP,18);
                Log.d("largeLog", String.valueOf(this.tvStepNumber.getTextSize()) + " " + enlarged.toString());
            }
            else{
                Animation a = AnimationUtils.loadAnimation(mContext, R.anim.step_scale_up);
                //a.reset();
                //this.tvStepNumber.clearAnimation();
                this.tvStepNumber.startAnimation(a);
                //this.tvStepContent.clearAnimation();
                this.tvStepContent.startAnimation(a);
                //this.tvStepNumber.setTextSize(TypedValue.COMPLEX_UNIT_SP,20);
                //this.tvStepContent.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
                Log.d("largeLog", String.valueOf(this.tvStepNumber.getTextSize()) + " " + enlarged.toString());
            }
        }
    }
}
