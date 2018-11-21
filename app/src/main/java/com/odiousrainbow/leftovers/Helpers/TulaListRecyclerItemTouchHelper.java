package com.odiousrainbow.leftovers.Helpers;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.odiousrainbow.leftovers.Adapters.TulaListAdapter;

public class TulaListRecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {

    public TulaListRecyclerItemTouchHelperListener listener;

    public TulaListRecyclerItemTouchHelper(int dragDirs, int swipeDirs, TulaListRecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if(listener != null){
            listener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
        }
    }
    @Override
    public void clearView(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder){
        if(viewHolder instanceof TulaListAdapter.TulaStuffItemViewHolder){
            View frontLayoutView = ((TulaListAdapter.TulaStuffItemViewHolder) viewHolder).frontLayout;
            getDefaultUIUtil().clearView(frontLayoutView);
        }

    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    @Override
    public void onSelectedChanged(@Nullable RecyclerView.ViewHolder viewHolder, int actionState) {
        if(viewHolder != null){
            if(viewHolder instanceof TulaListAdapter.TulaStuffItemViewHolder){
                View frontLayoutView = ((TulaListAdapter.TulaStuffItemViewHolder) viewHolder).frontLayout;
                getDefaultUIUtil().onSelected(frontLayoutView);
            }
        }
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(viewHolder instanceof TulaListAdapter.TulaStuffItemViewHolder){
            View frontLayoutView = ((TulaListAdapter.TulaStuffItemViewHolder) viewHolder).frontLayout;
            getDefaultUIUtil().onDraw(c,recyclerView,frontLayoutView,dX,dY,actionState,isCurrentlyActive);

        }
    }

    @Override
    public void onChildDrawOver(@NonNull Canvas c, @NonNull RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        if(viewHolder instanceof TulaListAdapter.TulaStuffItemViewHolder){
            View frontLayoutView = ((TulaListAdapter.TulaStuffItemViewHolder) viewHolder).frontLayout;
            getDefaultUIUtil().onDrawOver(c,recyclerView,frontLayoutView,dX,dY,actionState,isCurrentlyActive);
        }
    }


}
