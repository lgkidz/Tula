package com.odiousrainbow.leftovers.HomescreenFragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odiousrainbow.leftovers.Adapters.TulaListAdapter;
import com.odiousrainbow.leftovers.AddStuffActivity;
import com.odiousrainbow.leftovers.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class TulaFragment extends Fragment {
    private FloatingActionButton fab;
    private FloatingActionButton fab_empty;
    private ConstraintLayout empty_tula_layout;
    private RecyclerView tulaListLayout;
    private List<Map<String,String>> stuffsInTula;

    public TulaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Context context = getActivity();
        View v = inflater.inflate(R.layout.fragment_tula, container, false);
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
                getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        String json = sharedPreferences.getString(getString(R.string.preference_stored_stuff_key),null);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<Map<String,String>>>(){}.getType();
        stuffsInTula = gson.fromJson(json,type);
        if(json == null){
            empty_tula_layout.setVisibility(View.VISIBLE);
            fab.setVisibility(View.INVISIBLE);
            Log.d("thedamnlist", "no data in preference");
        }
        else{
            TulaListAdapter adapter = new TulaListAdapter(getActivity(),stuffsInTula);
            tulaListLayout.setLayoutManager(new LinearLayoutManager(getActivity()));
            tulaListLayout.setAdapter(adapter);
            Log.d("thedamnlist", "start!");
        }

        return v;
    }

}
