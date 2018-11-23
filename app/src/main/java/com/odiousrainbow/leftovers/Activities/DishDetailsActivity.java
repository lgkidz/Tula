package com.odiousrainbow.leftovers.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odiousrainbow.leftovers.R;
import com.odiousrainbow.leftovers.Adapters.ViewPagerAdapter;
import com.odiousrainbow.leftovers.DataModel.Recipe;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class DishDetailsActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener {

    private final String KEY_IMAGES_FOLDER = "images";

    private StorageReference mStorageRef;
    private final String KEY_DISH_NAME = "name";
    private final String KEY_COLLECTION = "dishes";
    private final String KEY_DISH_INSTRUCTION = "instr";
    private final String KEY_DISH_IMAGE_URL = "image";
    private final String KEY_DISH_SERVING = "serve";

    private final String KEY_INGREDIENTS_NAME = "name";
    private final String KEY_INGREDIENTS = "ingres";
    private final String KEY_INGREDIENTS_QUANTITY = "quantity";
    private final String KEY_INGREDIENTS_UNIT = "unit";
    private final String KEY_INGREDIENTS_SPICE = "spice";

    private static final String TAG = "DishDetailsActivity";

    private TabLayout tabLayout;
    private ViewPager pager;
    private ViewPagerAdapter pagerAdapter;
    private Toolbar toolbar;
    private Recipe currentRecipe;
    private ImageView ivDish;
    private TextView tvCookingTime;
    private TextView tvServing;
    private TextView tvTotalCal;
    private Button btn_addToFav;

    private SharedPreferences sharedPreferences;
    private Gson gson;
    private Type type;
    private SharedPreferences.Editor editor;
    private List<Recipe> mFavoriteDishes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);
        mStorageRef = FirebaseStorage.getInstance().getReference(KEY_IMAGES_FOLDER);
        mFavoriteDishes = new ArrayList<>();
        sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);

        gson = new Gson();
        type = new TypeToken<ArrayList<Recipe>>(){}.getType();
        String json = sharedPreferences.getString(getString(R.string.prefernece_favorite_key),null);
        if(json != null && !json.equals("[]")){
            mFavoriteDishes = gson.fromJson(json,type);
        }
        getData();
        initViews();
        addEvents();
        initViewPager();
    }

    private void getData() {
        Intent intent = getIntent();
        currentRecipe = (Recipe) intent.getSerializableExtra("dish");
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currentRecipe.getName());
        ivDish = findViewById(R.id.iv_food);
        btn_addToFav = findViewById(R.id.btn_add_to_fav);
        tvCookingTime = findViewById(R.id.tv_recipe_cooking_time);
        tvServing = findViewById(R.id.tv_recipe_serving);
        tvTotalCal = findViewById(R.id.tv_recipe_total_cal);
        if(currentRecipe.getCookingTime() == null){
            tvCookingTime.setText("Chưa cập nhật");
        }
        else{
            tvCookingTime.setText(currentRecipe.getCookingTime() + " " + getString(R.string.recipe_cooking_time_suffix));
        }
        if(currentRecipe.getServing() == null){
            tvServing.setText("Chưa cập nhật");
        }
        else{
            tvServing.setText(currentRecipe.getServing() + " " + getString(R.string.recipe_serving_suffix));
        }
        if(currentRecipe.getTotalCal() == null){
            tvTotalCal.setText("Chưa cập nhật");
        }
        else{
            tvTotalCal.setText(currentRecipe.getTotalCal() + " " + getString(R.string.recipe_total_cal_suffix));
        }
        tabLayout = findViewById(R.id.tab_layout);
        pager = findViewById(R.id.pager);
    }

    private void addEvents() {
        mStorageRef.child(currentRecipe.getImageUrl()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(ivDish);
            }
        });

        if(mFavoriteDishes.contains(currentRecipe)){
            btn_addToFav.setBackgroundResource(R.drawable.ic_favorite_24dp);
        }
        else{
            btn_addToFav.setBackgroundResource(R.drawable.ic_favorite_border_24dp);
        }

        btn_addToFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mFavoriteDishes.contains(currentRecipe)){
                    mFavoriteDishes.remove(currentRecipe);
                    editor = sharedPreferences.edit();
                    String toJson = gson.toJson(mFavoriteDishes);
                    editor.putString(getString(R.string.prefernece_favorite_key),toJson);
                    editor.apply();
                    btn_addToFav.setBackgroundResource(R.drawable.ic_favorite_border_24dp);
                }
                else{
                    mFavoriteDishes.add(currentRecipe);
                    editor = sharedPreferences.edit();
                    String toJson = gson.toJson(mFavoriteDishes);
                    editor.putString(getString(R.string.prefernece_favorite_key),toJson);
                    editor.apply();
                    btn_addToFav.setBackgroundResource(R.drawable.ic_favorite_24dp);
                }
            }
        });

    }

    private void initViewPager() {
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),currentRecipe);
        pager.setAdapter(pagerAdapter);
        pager.setOffscreenPageLimit(pagerAdapter.getCount());
        pager.addOnPageChangeListener(this);
        tabLayout.setupWithViewPager(pager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dish_details_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_up_to_pro){
            Intent UpgradeToProIntent = new Intent(DishDetailsActivity.this, UpgradeToProActivity.class);
            startActivity(UpgradeToProIntent);
        }
        else if(item.getItemId() == R.id.menu_setting){
            Intent settingIntent = new Intent(DishDetailsActivity.this,SettingsActivity.class);
            startActivity(settingIntent);
        }
        else if(item.getItemId() == R.id.menu_feedback){
            Intent feedbackIntent = new Intent(DishDetailsActivity.this, FeedbackActivity.class);
            startActivity(feedbackIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    @Override
    public void onPageSelected(int i) {

    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
