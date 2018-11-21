package com.odiousrainbow.leftovers.Helpers;

import android.support.v7.widget.RecyclerView;

public interface TulaListRecyclerItemTouchHelperListener {
    void onSwiped(RecyclerView.ViewHolder viewHolder, int direction,int position);
}
