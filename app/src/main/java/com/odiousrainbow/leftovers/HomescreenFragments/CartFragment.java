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
import java.util.Arrays;
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
                        List<String> categoryList = Arrays.asList(getResources().getStringArray(R.array.temporary_ingredients_categories));
                        for (int i = (selected.size() - 1); i >= 0; i--) {
                            if (selected.valueAt(i)) {
                                Ingredient currentIngredient = data.get(selected.keyAt(i));
                                Map<String,String> m = new HashMap<>();
                                m.put("iName",currentIngredient.getName());
                                String possibleCategory = possibleCategory(currentIngredient.getName());
                                m.put("iCate",possibleCategory);
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

    public String possibleCategory(String name){
                        /*
                        <string-array name="temporary_ingredients_categories">
                            <item>Bơ sữa</item>
                            <item>Bộ mì và gạo</item>
                            <item>Cá</item>
                            <item>Hoa quả</item>
                            <item>Hạt có vỏ</item>
                            <item>Rau củ</item>
                            <item>Thịt</item>
                            <item>Thủy hải sản</item>
                            <item>Trứng</item>
                            <item>Đậu và đỗ</item>
                            <item>Đồ uống</item>
                        </string-array>
                         */
        String nameLowerCase = name.trim().toLowerCase();
        if(nameLowerCase.contains("kem") || name.contains("phô mai") || nameLowerCase.contains("sữa") || nameLowerCase.equals("bơ")){
            return "Bơ sữa";
        }
        else if(nameLowerCase.contains("bánh") || nameLowerCase.contains("bún") || nameLowerCase.contains("phở") ||
        nameLowerCase.contains("miến")|| nameLowerCase.contains("mỳ")|| nameLowerCase.contains("mì")|| nameLowerCase.contains("nui")|| nameLowerCase.contains("cảo")){
            return "Bột mì và gạo";
        }
        else if(nameLowerCase.contains("cá ")){
            return "Cá";
        }
        else if(nameLowerCase.contains("hạt") || nameLowerCase.contains("hạnh")|| nameLowerCase.contains("hồ") || nameLowerCase.contains("lạc")|| nameLowerCase.contains("yến mạch")){
            return "Hạt có vỏ";
        }
        else if(nameLowerCase.contains("gà") || nameLowerCase.contains("bò")|| nameLowerCase.contains("lợn")
                || nameLowerCase.contains("heo")|| nameLowerCase.contains("giò")|| nameLowerCase.contains("chả")|| nameLowerCase.contains("thịt")
                || nameLowerCase.contains("vịt")|| nameLowerCase.contains("ngan")|| nameLowerCase.contains("xương")
                || nameLowerCase.contains("sườn")|| nameLowerCase.contains("xúc xích")|| nameLowerCase.contains("lạp")
                || nameLowerCase.contains("pate")){
            return "Thịt";
        }
        else if(nameLowerCase.contains("cua")|| nameLowerCase.contains("ghẹ")|| nameLowerCase.contains("sò")
                || nameLowerCase.contains("ngao")|| nameLowerCase.contains("tôm")|| nameLowerCase.contains("tép")
                || nameLowerCase.contains("mực")|| nameLowerCase.contains("tuộc")|| nameLowerCase.contains("ếch")
                || nameLowerCase.contains("ốc")){
            return "Thủy hải sản";
        }
        else if(nameLowerCase.contains("trứng")){
            return "Trứng";
        }
        else if(nameLowerCase.contains("đỗ")|| nameLowerCase.contains("đậu")){
            return "Đậu và đỗ";
        }
        else if(nameLowerCase.contains("rau")|| nameLowerCase.contains("củ") || nameLowerCase.contains("bầu")
                || nameLowerCase.contains("cà") || nameLowerCase.contains("chua")|| nameLowerCase.contains("hành")
                || nameLowerCase.contains("bí")|| nameLowerCase.contains("bầu")|| nameLowerCase.contains("mướp")
                || nameLowerCase.contains("húng")|| nameLowerCase.contains("nấm")|| nameLowerCase.contains("khoai")
                || nameLowerCase.contains("su")|| nameLowerCase.contains("súp")|| nameLowerCase.contains("tía")){
            return "Rau củ";
        }
        else if(nameLowerCase.contains("bơ")|| nameLowerCase.contains("bưởi")|| nameLowerCase.contains("cam")|| nameLowerCase.contains("chanh")
                || nameLowerCase.contains("cherry")|| nameLowerCase.contains("chuối")|| nameLowerCase.contains("dâu")|| nameLowerCase.contains("dưa")
                || nameLowerCase.contains("dứa")|| nameLowerCase.contains("dừa")|| nameLowerCase.contains("hồng")|| nameLowerCase.contains("kiwi")
                || nameLowerCase.contains("măng cụt")|| nameLowerCase.contains("nho")|| nameLowerCase.contains("quýt")|| nameLowerCase.contains("quất")
                || nameLowerCase.contains("sung")|| nameLowerCase.contains("táo")|| nameLowerCase.contains("xoài")|| nameLowerCase.contains("đào")
                || nameLowerCase.contains("ổi")|| nameLowerCase.contains("lựu")|| nameLowerCase.contains("quả")|| nameLowerCase.contains("trái")){
            return "Hoa quả";
        }
        return "Khác";

    }

}
