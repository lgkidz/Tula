package com.odiousrainbow.leftovers.DishDetailsFragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.odiousrainbow.leftovers.Adapters.HowToStepsAdapter;
import com.odiousrainbow.leftovers.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    private RecyclerView stepRecyclerView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_how, container, false);
        stepRecyclerView = view.findViewById(R.id.tv_recipe_instruction);
        Bundle bundle = getArguments();
        instruction = bundle.getString("instr");
        List<String> instructionStepRaw = Arrays.asList(instruction.split("##"));
        List<String> instructionStep = new ArrayList<>();
        for(String s: instructionStepRaw){
            String refinedString = s.replace("#","\n");
            instructionStep.add(refinedString);
        }

        stepRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.VERTICAL,false));
        HowToStepsAdapter adapter = new HowToStepsAdapter(getActivity(),instructionStep);
        stepRecyclerView.setAdapter(adapter);
        stepRecyclerView.setItemViewCacheSize(15);
        stepRecyclerView.setHasFixedSize(true);
        return view;
    }
}
