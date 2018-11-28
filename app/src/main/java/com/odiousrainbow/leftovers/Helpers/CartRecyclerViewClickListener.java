package com.odiousrainbow.leftovers.Helpers;

import android.view.View;

public interface CartRecyclerViewClickListener {
    void onClick(View view, int position);

    void onLongClick(View view, int position);
}
