package com.odiousrainbow.leftovers.DishDetailsFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.odiousrainbow.leftovers.R;

public class FragmentNutrition extends Fragment {
    public static FragmentNutrition instance;

    public static FragmentNutrition getInstance(){
        if (instance == null){
            instance = new FragmentNutrition();
        }

        return instance;
    }

    private View view;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_nutrition, container, false);
        return view;
    }
}
