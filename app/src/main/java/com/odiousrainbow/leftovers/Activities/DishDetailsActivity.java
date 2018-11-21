package com.odiousrainbow.leftovers.Activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.odiousrainbow.leftovers.R;
import com.odiousrainbow.leftovers.Adapters.ViewPagerAdapter;
import com.odiousrainbow.leftovers.DataModel.Recipe;
import com.squareup.picasso.Picasso;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_detail);
        mStorageRef = FirebaseStorage.getInstance().getReference(KEY_IMAGES_FOLDER);

        getData();
        initViews();
        addEvents();
        initViewPager();
    }

    private void getData() {
        Intent intent = getIntent();
        currentRecipe = (Recipe) intent.getSerializableExtra("dish");
        Log.d(TAG, currentRecipe.getIngredients().toString());
    }

    private void initViews() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(currentRecipe.getName());
        ivDish = findViewById(R.id.iv_food);
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