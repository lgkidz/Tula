package com.odiousrainbow.leftovers.HomescreenFragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odiousrainbow.leftovers.Adapters.TulaListAdapter;
import com.odiousrainbow.leftovers.Activities.AddStuffActivity;
import com.odiousrainbow.leftovers.Helpers.TulaListRecyclerItemTouchHelper;
import com.odiousrainbow.leftovers.Helpers.TulaListRecyclerItemTouchHelperListener;
import com.odiousrainbow.leftovers.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class TulaFragment extends Fragment implements TulaListRecyclerItemTouchHelperListener {
    private FloatingActionButton fab;
    private FloatingActionButton fab_empty;
    private ConstraintLayout empty_tula_layout;
    private RecyclerView tulaListLayout;
    private List<Map<String,String>> stuffsInTula;
    private TulaListAdapter adapter;
    private FrameLayout rootLayout;
    private Gson gson;


    public TulaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getActivity();
        View v = inflater.inflate(R.layout.fragment_tula, container, false);
        rootLayout = v.findViewById(R.id.tula_layout);
        fab = v.findViewById(R.id.fab);
        fab_empty = v.findViewById(R.id.fab_empty);
        empty_tula_layout = v.findViewById(R.id.empty_tula_layout);
        tulaListLayout = v.findViewById(R.id.tula_list_layout);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addStuffIntent = new Intent(getActivity(),AddStuffActivity.class);
                startActivity(addStuffIntent);
            }
        });

        fab_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addStuffIntent = new Intent(getActivity(),AddStuffActivity.class);
                startActivity(addStuffIntent);
            }
        });
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                getString(R.string.preference_file_key), MODE_PRIVATE);

        String json = sharedPreferences.getString(getString(R.string.preference_stored_stuff_key),null);
        gson = new Gson();
        Type type = new TypeToken<ArrayList<Map<String,String>>>(){}.getType();
        if(json != null){
            stuffsInTula = gson.fromJson(json,type);
        }
        else{
            stuffsInTula = new ArrayList<>();
        }
        if(json == null || json.equals("[]")){
            empty_tula_layout.setVisibility(View.VISIBLE);
            fab.setVisibility(View.INVISIBLE);
        }
        else{
            adapter = new TulaListAdapter(getActivity(),stuffsInTula);
            tulaListLayout.setLayoutManager(new LinearLayoutManager(getActivity()));
            tulaListLayout.setItemAnimator(new DefaultItemAnimator());

            tulaListLayout.setAdapter(adapter);

            ItemTouchHelper.SimpleCallback itemTouchHelperCallBack
                    = new TulaListRecyclerItemTouchHelper(0, ItemTouchHelper.LEFT,this);
             new ItemTouchHelper(itemTouchHelperCallBack).attachToRecyclerView(tulaListLayout);



        }

        return v;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position) {
        if(viewHolder instanceof TulaListAdapter.TulaStuffItemViewHolder){
            final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);
            final int itemIndex = viewHolder.getAdapterPosition();
            final Map<String,String> deletedItem = adapter.mData.get(itemIndex);
            String name = adapter.mData.get(itemIndex).get("iName");

            for(int i = 0;i<stuffsInTula.size();i++){
                if(stuffsInTula.get(i).get("iName").equals(name)
                        && stuffsInTula.get(i).get("iExpDate").equals(adapter.mData.get(itemIndex).get("iExpDate"))
                        && stuffsInTula.get(i).get("iQuan").equals(adapter.mData.get(itemIndex).get("iQuan"))){
                    stuffsInTula.remove(i);
                }
            }
            adapter.removeItem(itemIndex);

            String stuffsInJson = gson.toJson(stuffsInTula);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString(getString(R.string.preference_stored_stuff_key),stuffsInJson);
            editor.apply();

            Snackbar snackbar = Snackbar.make(rootLayout,"Đã xóa " + name,Snackbar.LENGTH_SHORT);
            snackbar.setAction("UNDO", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    adapter.restoreItem(deletedItem,itemIndex);
                    stuffsInTula.add(deletedItem);
                    String stuffsInJson = gson.toJson(stuffsInTula);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.preference_stored_stuff_key),stuffsInJson);
                    editor.apply();
                }
            });
            snackbar.setActionTextColor(Color.YELLOW);
            snackbar.show();
        }
    }
}
