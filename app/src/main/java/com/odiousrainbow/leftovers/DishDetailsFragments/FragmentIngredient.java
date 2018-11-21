package com.odiousrainbow.leftovers.DishDetailsFragments;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odiousrainbow.leftovers.R;
import com.odiousrainbow.leftovers.Adapters.IngredientAdapter;
import com.odiousrainbow.leftovers.DataModel.Ingredient;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

public class FragmentIngredient extends Fragment {
    public static FragmentIngredient instance;
    private boolean alreadyAddedToCart = false;

    public static FragmentIngredient getInstance(){
        if (instance == null){
            instance = new FragmentIngredient();
        }

        return instance;
    }

    private View view;
    private FloatingActionButton fab;
    private RecyclerView rvListIngredient;
    private IngredientAdapter ingredientAdapter;
    private List<Ingredient> listIngredient;
    private List<Map<String,String>> stuffsInTula;
    private Gson gson;
    private List<Ingredient> neededIngredients;
    private List<Ingredient> cartIngres;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ingredient, container, false);
        alreadyAddedToCart = false;
        fab = view.findViewById(R.id.fab_add_to_cart);
        Bundle bundle = getArguments();
        listIngredient = (ArrayList) bundle.getSerializable("recipes");
        final SharedPreferences sharedPreferences = getActivity().getSharedPreferences(
                getActivity().getString(R.string.preference_file_key), MODE_PRIVATE);

        String json = sharedPreferences.getString(getActivity().getString(R.string.preference_stored_stuff_key),null);
        gson = new Gson();
        Type type = new TypeToken<ArrayList<Map<String,String>>>(){}.getType();
        stuffsInTula = gson.fromJson(json,type);

        String cartJson = sharedPreferences.getString(getString(R.string.prefernece_cart_key),null);
        if(cartJson!= null){
            Type cartType = new TypeToken<ArrayList<Ingredient>>(){}.getType();
            cartIngres = gson.fromJson(cartJson,cartType);
        }
        else{
            cartIngres = new ArrayList<>();
        }

        initViews(view);
        addEvents();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!alreadyAddedToCart){
                    for(Ingredient i: neededIngredients){
                        cartIngres.add(i);
                    }
                    String toJsonCart = gson.toJson(cartIngres);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString(getString(R.string.prefernece_cart_key),toJsonCart);
                    editor.apply();
                    Snackbar.make(view,"Đã thêm " + neededIngredients.size() + " nguyên liệu còn thiếu vào giỏ hàng",Snackbar.LENGTH_SHORT).show();
                    alreadyAddedToCart = true;
                }
                else{
                    Snackbar.make(view,"Bạn đã thêm những nguyên liệu này vào giỏ hàng!",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
        return view;
    }

    private void initViews(View view) {
        rvListIngredient = view.findViewById(R.id.list_ingredient);
        rvListIngredient.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        rvListIngredient.setItemViewCacheSize(15);
        rvListIngredient.setHasFixedSize(true);
    }

    private void addEvents(){
        neededIngredients = new ArrayList<>();
        boolean havingMoreThanNeeded = false;
        ingredientAdapter = new IngredientAdapter(getActivity(),listIngredient);
        rvListIngredient.setAdapter(ingredientAdapter);

        for(int i =0 ;i<listIngredient.size();i++){
            if(!listIngredient.get(i).isSpice()){
                for(int j = 0;j<stuffsInTula.size();j++){
                    if(stuffsInTula.get(j).get("iName").equals(listIngredient.get(i).getName()) && haveMoreThanNeeded(i)){
                        havingMoreThanNeeded = true;
                        break;
                    }
                    else{
                        havingMoreThanNeeded = false;
                    }
                }
                if(!havingMoreThanNeeded){
                    neededIngredients.add(listIngredient.get(i));
                }
            }
        }

        Log.d("hasmorethanneeded", neededIngredients.toString());
        if(havingMoreThanNeeded){
            fab.setVisibility(View.INVISIBLE);
        }
    }

    public boolean haveMoreThanNeeded(int i){
        float quantityHave = 0;
        float quantityNeed;
        String unitNeed = listIngredient.get(i).getUnit().toLowerCase();
        String unitHave = "gram";
        switch (unitNeed){
            case "gram":{
                quantityNeed = Float.parseFloat(listIngredient.get(i).getQuantity());
                break;
            }
            case "kg":{
                quantityNeed = 1000 * Float.parseFloat(listIngredient.get(i).getQuantity());
                unitNeed = "gram";
                break;
            }
            case "lạng":{
                quantityNeed = 100 * Float.parseFloat(listIngredient.get(i).getQuantity());
                unitNeed = "gram";
                break;
            }
            case "ml":{
                quantityNeed = Float.parseFloat(listIngredient.get(i).getQuantity());
                unitNeed = "gram";
                break;
            }
            case "l":{
                quantityNeed = 1000 * Float.parseFloat(listIngredient.get(i).getQuantity());
                unitNeed = "gram";
                break;
            }
            default:{
                quantityNeed = Float.parseFloat(listIngredient.get(i).getQuantity());
                unitNeed = "countable_unit";
                break;
            }
        }

        for(int j = 0;j<stuffsInTula.size();j++){
            if(stuffsInTula.get(j).get("iName").equals(listIngredient.get(i).getName())){
                String u = stuffsInTula.get(j).get("iUnit").toLowerCase();
                switch (u){
                    case "gram":{
                        quantityHave += Float.parseFloat(stuffsInTula.get(j).get("iQuan"));
                        unitHave = "gram";
                        break;
                    }
                    case "kg":{
                        quantityHave += 1000 * Float.parseFloat(stuffsInTula.get(j).get("iQuan"));
                        unitHave = "gram";
                        break;
                    }
                    case "lạng":{
                        quantityHave += 100 * Float.parseFloat(stuffsInTula.get(j).get("iQuan"));
                        unitHave = "gram";
                        break;
                    }
                    case "ml":{
                        quantityHave += Float.parseFloat(stuffsInTula.get(j).get("iQuan"));
                        unitHave = "gram";
                        break;
                    }
                    case "l":{
                        quantityHave += 1000 * Float.parseFloat(stuffsInTula.get(j).get("iQuan"));
                        unitHave = "gram";
                        break;
                    }
                    default:{
                        quantityHave += Float.parseFloat(stuffsInTula.get(j).get("iQuan"));
                        unitHave = "countable_unit";
                        break;
                    }
                }
            }
        }



        if(unitHave.equals(unitNeed) && quantityHave >= quantityNeed){
            return true;
        }
        return false;
    }
}
