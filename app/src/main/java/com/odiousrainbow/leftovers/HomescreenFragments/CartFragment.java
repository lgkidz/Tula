package com.odiousrainbow.leftovers.HomescreenFragments;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odiousrainbow.leftovers.DataModel.Ingredient;
import com.odiousrainbow.leftovers.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartFragment extends Fragment {
    private List<Ingredient> cartIngres;
    private Gson gson;

    public CartFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_cart, container, false);

        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                getActivity().getString(R.string.preference_file_key), MODE_PRIVATE);

        gson = new Gson();
        String cartJson = sharedPreferences.getString(getString(R.string.prefernece_cart_key),null);

        if(cartJson!= null){
            Type cartType = new TypeToken<ArrayList<Ingredient>>(){}.getType();
            cartIngres = gson.fromJson(cartJson,cartType);


        }
        else{
            cartIngres = new ArrayList<>();
        }

        for(Ingredient i: cartIngres){
            Log.d("blahlah", i.toString());
        }

        return view;
    }

}
