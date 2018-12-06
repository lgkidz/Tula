package com.odiousrainbow.leftovers.HomescreenFragments;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.support.v7.view.ActionMode;

import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odiousrainbow.leftovers.Activities.AddStuffDetailsActivity;
import com.odiousrainbow.leftovers.Activities.MainActivity;
import com.odiousrainbow.leftovers.Adapters.CartSimpleListAdapter;
import com.odiousrainbow.leftovers.DataModel.Ingredient;
import com.odiousrainbow.leftovers.Helpers.CartRecyclerViewClickListener;
import com.odiousrainbow.leftovers.Helpers.CartRecyclerViewTouchListener;
import com.odiousrainbow.leftovers.Helpers.CartToolbarActionModeCallback;
import com.odiousrainbow.leftovers.R;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    private static View view;
    private List<Ingredient> cartIngres;
    private Gson gson;
    private RecyclerView recyclerView;
    ConstraintLayout empty_cart;
    private ActionMode mActionMode;
    CartSimpleListAdapter cartSimpleListAdapter;
    List<Ingredient> itemsWithSumQuantity;
    private SharedPreferences sharedPreferences;
    List<Map<String,String>> stuffsInTula;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_cart, container, false);

        sharedPreferences = getActivity().getSharedPreferences(
                getActivity().getString(R.string.preference_file_key), MODE_PRIVATE);

        gson = new Gson();
        String cartJson = sharedPreferences.getString(getString(R.string.prefernece_cart_key),null);
        String stuffsInTulaJson = sharedPreferences.getString(getString(R.string.preference_stored_stuff_key),null);
        if(stuffsInTulaJson != null){
            Type stuffsInTulaType = new TypeToken<List<Map<String,String>>>(){}.getType();
            stuffsInTula = gson.fromJson(stuffsInTulaJson,stuffsInTulaType);
        }
        else{
            stuffsInTula = new ArrayList<>();
        }

        if(cartJson!= null){
            Type cartType = new TypeToken<ArrayList<Ingredient>>(){}.getType();
            cartIngres = gson.fromJson(cartJson,cartType);
        }
        else{
            cartIngres = new ArrayList<>();
        }
        empty_cart = view.findViewById(R.id.empty_cart_layout);

        recyclerView = view.findViewById(R.id.cart_recycler_view);
        itemsWithSumQuantity = new ArrayList<>();
        List<String> itemNames = new ArrayList<>();
        for (int i = 0; i < cartIngres.size() ; i++) {
            if(!itemNames.contains(cartIngres.get(i).getName())){
                itemNames.add(cartIngres.get(i).getName());
                itemsWithSumQuantity.add(cartIngres.get(i));
            }
            else{
                for (int j = 0; j <itemsWithSumQuantity.size() ; j++) {
                    if(itemsWithSumQuantity.get(j).getName().equals(cartIngres.get(i).getName())){
                        int sum = Integer.parseInt(itemsWithSumQuantity.get(j).getQuantity()) + Integer.parseInt(cartIngres.get(i).getQuantity());
                        itemsWithSumQuantity.get(j).setQuantity(String.valueOf(sum));
                    }
                }
            }
        }

        if(itemsWithSumQuantity.size() > 0){
            cartSimpleListAdapter = new CartSimpleListAdapter(getActivity(),itemsWithSumQuantity);
            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayout.VERTICAL,false));
            recyclerView.setItemViewCacheSize(30);
            //recyclerView.setHasFixedSize(true);
            recyclerView.setAdapter(cartSimpleListAdapter);
            empty_cart.setVisibility(View.INVISIBLE);
        }
        else{
            empty_cart.setVisibility(View.VISIBLE);
        }

        implementRecyclerViewClickListeners();
        return view;
    }

    private void implementRecyclerViewClickListeners() {
        recyclerView.addOnItemTouchListener(new CartRecyclerViewTouchListener(getActivity(), recyclerView, new CartRecyclerViewClickListener() {
            @Override
            public void onClick(View view, int position) {
                //If ActionMode not null select item
                //if (mActionMode != null)
                    onListItemSelect(position);
            }

            @Override
            public void onLongClick(View view, int position) {
                //Select item on long click
                onListItemSelect(position);
            }
        }));
    }

    private void onListItemSelect(int position) {
        cartSimpleListAdapter.toggleSelection(position);//Toggle the selection

        boolean hasCheckedItems = cartSimpleListAdapter.getSelectedCount() > 0;//Check if any items are already selected or not


        if (hasCheckedItems && mActionMode == null)
            // there are some selected items, start the actionMode
            mActionMode =((AppCompatActivity) getActivity()).startSupportActionMode( new CartToolbarActionModeCallback(getActivity(), cartSimpleListAdapter, CartFragment.this, (ArrayList<Ingredient>) itemsWithSumQuantity));
        else if (!hasCheckedItems && mActionMode != null)
            // there no selected items, finish the actionMode
            mActionMode.finish();

        if (mActionMode != null){}
            //set action mode title on item selection
            //mActionMode.setTitle(String.valueOf("Đã chọn " + cartSimpleListAdapter.getSelectedCount())); //gonna generate bug!


    }

    public void setNullToActionMode() {
        if (mActionMode != null)
            mActionMode = null;
    }

    public void deleteRows() {
        new AlertDialog.Builder(getActivity()).setTitle("Xóa khỏi giỏ hàng")
                .setMessage("Bạn có chắc muốn xóa các mục này khỏi giỏ hàng?")
                .setPositiveButton("Xóa", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SparseBooleanArray selected = cartSimpleListAdapter
                                .getSelectedIds();//Get selected ids

                        //Loop all selected ids
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                //If current id is selected remove the item via key
                                itemsWithSumQuantity.remove(selected.keyAt(i));
                                cartSimpleListAdapter.notifyDataSetChanged();//notify adapter

                            }
                        }
                        cartIngres = itemsWithSumQuantity;
                        String json = gson.toJson(cartIngres);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.prefernece_cart_key),json);
                        editor.apply();
                        mActionMode.finish();//Finish action mode after use
                        if(itemsWithSumQuantity.size() == 0){
                            empty_cart.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .setNegativeButton("Quay lại",null)
                .show();
    }

    public void saveRowsToTula(final ArrayList<Ingredient> data){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        Date expDateRaw = calendar.getTime();
        final String expDateString = dateFormat.format(expDateRaw);
        new AlertDialog.Builder(getActivity()).setTitle("Thêm vào tủ lạnh")
                .setMessage("Bạn có chắc muốn thêm các mục này vào tủ lạnh?")
                .setPositiveButton("Thêm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SparseBooleanArray selected = cartSimpleListAdapter
                                .getSelectedIds();//Get selected ids

                        //Loop all selected ids
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                Ingredient currentIngredient = data.get(selected.keyAt(i));
                                Map<String,String> m = new HashMap<>();
                                m.put("iName",currentIngredient.getName());
                                m.put("iCate","Khác");
                                m.put("iQuan",currentIngredient.getQuantity());
                                m.put("iUnit",currentIngredient.getUnit());
                                m.put("iExpDate",expDateString);
                                m.put("iNoti","true");
                                stuffsInTula.add(m);
                                itemsWithSumQuantity.remove(selected.keyAt(i));
                                cartSimpleListAdapter.notifyDataSetChanged();//notify adapter

                            }
                        }
                        cartIngres = itemsWithSumQuantity;
                        String json = gson.toJson(cartIngres);
                        String stuffsInJson = gson.toJson(stuffsInTula);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putString(getString(R.string.prefernece_cart_key),json);
                        editor.putString(getString(R.string.preference_stored_stuff_key),stuffsInJson);
                        editor.apply();
                        mActionMode.finish();//Finish action mode after use
                        if(itemsWithSumQuantity.size() == 0){
                            empty_cart.setVisibility(View.VISIBLE);
                        }
                    }
                })
                .setNegativeButton("Quay lại",null)
                .show();
    }

}
