package com.odiousrainbow.leftovers.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.View;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.odiousrainbow.leftovers.Adapters.IngresCategoryListAdapter;
import com.odiousrainbow.leftovers.DataModel.IngredientCategory;
import com.odiousrainbow.leftovers.R;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AddStuffActivity extends AppCompatActivity {
    private FirebaseFirestore db;
    private final String KEY_COLLECTION = "Ingredient_categories"; // The collection's name on FireStore which store list of ingredients

    private Toolbar myToolbar;
    private ProgressBar mypb;
    private ExpandableListView cateListView;
    private ExpandableListAdapter expandableListAdapter;

    private List<String> cateList = new ArrayList<>();
    private List<IngredientCategory> categoryList = new ArrayList<>();
    private HashMap<String,List<String>> itemsMap = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stuff);

        db = FirebaseFirestore.getInstance(); //init db

        mypb = findViewById(R.id.add_stuff_pb);
        setToolbar();

        cateListView = findViewById(R.id.ingres_cate_list_view);
        getDataAndFillTheScreen();
    }

    public void setToolbar(){
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.add_stuff_activity);
    }

    /*********************
            Ingredient category data schema:
            {
                name: Category name,
                image: image.jpg,
                prods:[string1,string2,string3,more]
            }
     **********************/

    public void getDataAndFillTheScreen(){
        db.collection(KEY_COLLECTION).orderBy("name")
                .get()                                  //fetch data on firestore database
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                        String cateName = documentSnapshot.getString("name"); //List of category Name
                        String image = documentSnapshot.getString("image");
                        List<String> rawIngres = (List<String>) documentSnapshot.get("prods"); //List of products in said category Name. As data schema above.
                        List<String> ingres = new ArrayList<>();
                        for(String s : rawIngres){
                            String ingre = s.substring(0, 1).toUpperCase() + s.substring(1);
                            ingres.add(ingre);
                        }
                        Collections.sort(ingres);
                        IngredientCategory ingredientCategory = new IngredientCategory(cateName,image,ingres);
                        categoryList.add(ingredientCategory);
                        cateList.add(cateName);
                        itemsMap.put(cateName,ingres);
                    }
                    fillTheScreen();
                });
    }

    public void fillTheScreen(){
        expandableListAdapter = new IngresCategoryListAdapter(this, categoryList); //Adapter
        cateListView.setAdapter(expandableListAdapter);

        Display newDisplay = getWindowManager().getDefaultDisplay();    /////////////////////
        int width = newDisplay.getWidth();                              // set the indicator to the right.(default is left)
        cateListView.setIndicatorBounds(width-275, width);         // see more on ExpandableList document on google

        // set click event to start new Intent whenever a child is clicked
        cateListView.setOnChildClickListener((parent, v, groupPosition, childPosition, id) -> {
            Intent stuffDetailsIntent = new Intent(AddStuffActivity.this,AddStuffDetailsActivity.class);
            stuffDetailsIntent.putExtra("ingreCate",(String) expandableListAdapter.getGroup(groupPosition));
            stuffDetailsIntent.putExtra("ingreName",(String) expandableListAdapter.getChild(groupPosition,childPosition)); //send ingredient name to the next activity
            startActivity(stuffDetailsIntent);
            return true;
        });
        mypb.setVisibility(View.INVISIBLE);
        cateListView.setVisibility(View.VISIBLE);
    }

}
