package com.odiousrainbow.leftovers.HomescreenFragments;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odiousrainbow.leftovers.Adapters.DishesGridAdapter;
import com.odiousrainbow.leftovers.Activities.AddStuffActivity;
import com.odiousrainbow.leftovers.DataModel.Ingredient;
import com.odiousrainbow.leftovers.DataModel.Recipe;
import com.odiousrainbow.leftovers.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView homeRecyclerView;
    private ProgressBar mProgressBar;
    private FloatingActionButton fab_empty;
    private ConstraintLayout empty_tula_layout;

    private FirebaseFirestore db;
    private StorageReference mStorageRef;

    private List<Recipe> data = new ArrayList<>();
    private List<Recipe> suggestedDishes = new ArrayList<>();
    private final String KEY_COLLECTION = "dishes";
    private final String KEY_IMAGES_FOLDER = "images";
    private Context context;
    private final String KEY_DISH_NAME = "name";
    private final String KEY_DISH_INSTRUCTION = "instr";
    private final String KEY_DISH_IMAGE_URL = "image";
    private final String KEY_DISH_SERVING = "serve";
    private final String KEY_DISH_COOKING_TIME = "cooktime";
    private final String KEY_DISH_TOTAL_CAL = "totalcal";

    private final String KEY_INGREDIENTS_NAME = "name";
    private final String KEY_INGREDIENTS = "ingres";
    private final String KEY_INGREDIENTS_QUANTITY = "quantity";
    private final String KEY_INGREDIENTS_UNIT = "unit";
    private final String KEY_INGREDIENTS_SPICE = "spice";



    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater,container,savedInstanceState);
        context = getActivity();
        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference(KEY_IMAGES_FOLDER);

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mProgressBar = v.findViewById(R.id.home_fragment_pb);
        homeRecyclerView = v.findViewById(R.id.dishes_grid);
        fab_empty = v.findViewById(R.id.fab_empty);

        fab_empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent addStuffIntent = new Intent(getActivity(),AddStuffActivity.class);
                startActivity(addStuffIntent);
            }
        });

        empty_tula_layout = v.findViewById(R.id.empty_tula_layout);
        homeRecyclerView.setItemViewCacheSize(30);
        getSpecific();


        return v;
    }

    public void getSpecific(){
        if(isAdded()){
            SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
            String json = sharedPreferences.getString(getString(R.string.preference_stored_stuff_key),null);
            if(json == null || json.equals("[]")){
                empty_tula_layout.setVisibility(View.VISIBLE);
            }
        }

        db.collection(KEY_COLLECTION)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Map<String,Object>> m;
                        for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){

                            List<Ingredient> ingredients = new ArrayList<>();

                            m =  (List<Map<String,Object>>) documentSnapshot.get(KEY_INGREDIENTS);
                            for (Map<String,Object> i : m){
                                String name = (String) i.get(KEY_INGREDIENTS_NAME);
                                String quantity = (String) i.get(KEY_INGREDIENTS_QUANTITY);
                                String unit = (String) i.get(KEY_INGREDIENTS_UNIT);
                                Boolean isSpice = (Boolean) i.get(KEY_INGREDIENTS_SPICE);

                                Ingredient ingredient = new Ingredient(name,quantity,unit,isSpice);
                                ingredients.add(ingredient);

                            }
                            Recipe recipe = new Recipe(documentSnapshot.getString(KEY_DISH_IMAGE_URL)
                                    ,documentSnapshot.getString(KEY_DISH_NAME)
                                    ,documentSnapshot.getString(KEY_DISH_INSTRUCTION)
                                    ,documentSnapshot.getString(KEY_DISH_SERVING)
                                    ,documentSnapshot.getString(KEY_DISH_COOKING_TIME)
                                    ,documentSnapshot.getString(KEY_DISH_TOTAL_CAL)
                                    ,ingredients);
                            data.add(recipe);
                        }
                        filterWithStuffInTula();
                    }
                });

    }

    public void filterWithStuffInTula(){
        if(isAdded()) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
            String json = sharedPreferences.getString(getString(R.string.preference_stored_stuff_key),null);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Map<String,String>>>(){}.getType();
            List<Map<String,String>> stuffsInTula = gson.fromJson(json,type);

            if(json != null && !json.equals("[]")){
                empty_tula_layout.setVisibility(View.INVISIBLE);
                for(int i = 0;i < data.size();i++){
                    for(int j = 0;j<data.get(i).getIngredients().size();j++){
                        boolean found = false;
                        for(int k = 0; k < stuffsInTula.size();k++){
                            String stringNme = stuffsInTula.get(k).get("iName").toLowerCase();
                            if(data.get(i).getIngredients().get(j).getName().toLowerCase().contains(stringNme)){
                                Log.d("dishes", data.get(i).getIngredients().get(j).getName().toLowerCase() + ", " + stuffsInTula.get(k).get("iName").toLowerCase());
                                suggestedDishes.add(data.get(i));
                                found = true;
                                break;
                            }
                        }
                        if(found){
                            break;
                        }
                    }
                }
                fillView();
            }
        }
    }

    public void fillView(){
        DishesGridAdapter mAdapter = new DishesGridAdapter(getActivity(),suggestedDishes);
        homeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        homeRecyclerView.setAdapter(mAdapter);
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
