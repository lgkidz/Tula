package com.odiousrainbow.leftovers;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    private RecyclerView homeRecyclerView;
    private ProgressBar mProgressBar;

    private FirebaseFirestore db;
    private StorageReference mStorageRef;

    private List<Recipe> recipes = new ArrayList<>();
    private final String KEY_COLLECTION = "dishes";
    private final String KEY_IMAGES_FOLDER = "images";

    private final String KEY_DISH_NAME = "name";
    private final String KEY_DISH_INSTRUCTION = "instr";
    private final String KEY_DISH_IMAGE_URL = "image";
    private final String KEY_DISH_SERVING = "serve";

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

        db = FirebaseFirestore.getInstance();
        mStorageRef = FirebaseStorage.getInstance().getReference(KEY_IMAGES_FOLDER);

        View v = inflater.inflate(R.layout.fragment_home, container, false);
        mProgressBar = v.findViewById(R.id.home_fragment_pb);
        homeRecyclerView = v.findViewById(R.id.dishes_grid);
        homeRecyclerView.setItemViewCacheSize(30);
        getSpecific();


        return v;
    }

    public void getSpecific(){
        db.collection(KEY_COLLECTION)
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        List<Map<String,Object>> m = null;
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
                                    ,ingredients);
                            Log.d("ingre:", recipe.getIngredients().toString());
                            recipes.add(recipe);
                        }
                        fillView();
                    }
                });

    }

    public void fillView(){
        DishesGridAdapter mAdapter = new DishesGridAdapter(getActivity(),recipes);
        homeRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(),2));
        homeRecyclerView.setAdapter(mAdapter);
        mProgressBar.setVisibility(View.INVISIBLE);
    }
}
