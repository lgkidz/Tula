package com.odiousrainbow.leftovers;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 */
public class TulaFragment extends Fragment {
    private FloatingActionButton fab;
    private FloatingActionButton fab_empty;
    private ConstraintLayout empty_tula_layout;

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
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("test","something");
        editor.apply();
        editor.remove("test");
        editor.apply();
        String s = sharedPreferences.getString("test","nothing");
        if(s.equals("nothing")){
            empty_tula_layout.setVisibility(View.VISIBLE);
            fab.setVisibility(View.INVISIBLE);
        }

        return v;
    }

}
