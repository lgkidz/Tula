package com.odiousrainbow.leftovers.DishDetailsFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.odiousrainbow.leftovers.R;

public class FragmentHow extends Fragment {
    public static FragmentHow instance;

    public static FragmentHow getInstance(){
        if (instance == null){
            instance = new FragmentHow();
        }

        return instance;
    }

    private View view;
    private String instruction;
    private TextView tv_instruction;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_how, container, false);
        tv_instruction = view.findViewById(R.id.tv_recipe_instruction);
        Bundle bundle = getArguments();
        instruction = bundle.getString("instr");
        tv_instruction.setText(instruction);
        return view;
    }
}
