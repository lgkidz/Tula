package com.odiousrainbow.leftovers.Helpers;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.support.v7.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.odiousrainbow.leftovers.Adapters.CartSimpleListAdapter;
import com.odiousrainbow.leftovers.DataModel.Ingredient;
import com.odiousrainbow.leftovers.HomescreenFragments.CartFragment;
import com.odiousrainbow.leftovers.R;

import java.util.ArrayList;

public class CartToolbarActionModeCallback implements android.support.v7.view.ActionMode.Callback {

    private Context context;
    private CartSimpleListAdapter recyclerView_adapter;
    private CartFragment recyclerFragment;
    private ArrayList<Ingredient> message_models;


    public CartToolbarActionModeCallback(Context context, CartSimpleListAdapter recyclerView_adapter, CartFragment infofragment, ArrayList<Ingredient> message_models) {
        this.context = context;
        this.recyclerView_adapter = recyclerView_adapter;
        this.recyclerFragment = infofragment;
        this.message_models = message_models;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mode.getMenuInflater().inflate(R.menu.cart_edit_menu, menu);//Inflate the menu over action mode
        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {

            menu.findItem(R.id.action_delete).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
            menu.findItem(R.id.action_copy).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

        return true;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:
                recyclerFragment.deleteRows();
                break;
            case R.id.action_copy:
                recyclerFragment.saveRowsToTula(message_models);

                break;

        }
        return false;
    }


    @Override
    public void onDestroyActionMode(ActionMode mode) {
        recyclerView_adapter.removeSelection();
        recyclerFragment.setNullToActionMode();
    }
}
