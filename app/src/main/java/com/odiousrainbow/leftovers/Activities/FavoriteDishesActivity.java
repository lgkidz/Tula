package com.odiousrainbow.leftovers.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odiousrainbow.leftovers.Adapters.DishesGridAdapter;
import com.odiousrainbow.leftovers.DataModel.Recipe;
import com.odiousrainbow.leftovers.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FavoriteDishesActivity extends AppCompatActivity {

    private Toolbar myToolbar;
    private RecyclerView homeRecyclerView;
    private ProgressBar mProgressBar;
    private ConstraintLayout empty_tula_layout;
    private final String KEY_IMAGES_FOLDER = "images";
    private StorageReference mStorageRef;
    private List<Recipe> mFavoriteDishes = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private Type type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_dishes);
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        mStorageRef = FirebaseStorage.getInstance().getReference(KEY_IMAGES_FOLDER);
        homeRecyclerView = findViewById(R.id.dishes_grid);
        empty_tula_layout = findViewById(R.id.empty_tula_layout);
        homeRecyclerView.setItemViewCacheSize(30);
        mFavoriteDishes = new ArrayList<>();
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);

        gson = new Gson();
        type = new TypeToken<ArrayList<Recipe>>(){}.getType();
        String json = sharedPreferences.getString(getString(R.string.prefernece_favorite_key),null);
        if(json != null && !json.equals("[]")){
            mFavoriteDishes = gson.fromJson(json,type);
            DishesGridAdapter mAdapter = new DishesGridAdapter(this,mFavoriteDishes);
            homeRecyclerView.setLayoutManager(new GridLayoutManager(this,2));
            homeRecyclerView.setAdapter(mAdapter);
        }
        else{
            empty_tula_layout.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dish_details_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_up_to_pro){
            Intent UpgradeToProIntent = new Intent(FavoriteDishesActivity.this, UpgradeToProActivity.class);
            startActivity(UpgradeToProIntent);
        }
        else if(item.getItemId() == R.id.menu_setting){
            Intent settingIntent = new Intent(FavoriteDishesActivity.this,SettingsActivity.class);
            startActivity(settingIntent);
        }
        else if(item.getItemId() == R.id.menu_feedback){
            Intent feedbackIntent = new Intent(FavoriteDishesActivity.this, FeedbackActivity.class);
            startActivity(feedbackIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
