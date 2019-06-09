package com.odiousrainbow.leftovers.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odiousrainbow.leftovers.DataModel.Ingredient;
import com.odiousrainbow.leftovers.R;
import com.odiousrainbow.leftovers.Adapters.ViewPagerAdapter;
import com.odiousrainbow.leftovers.DataModel.Recipe;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    private AppBarLayout appBarLayout;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private ConstraintLayout addToCartButtonLayout;
    private int viewPagePreviousPage = 0;
    private List<Ingredient> neededIngredients;
    private List<Ingredient> cartIngres;
    private List<Ingredient> listIngredient;
    private List<Map<String,String>> stuffsInTula;
    private boolean alreadyAddedToCart = false;
    private Animation slide_down;
    private Animation slide_up;
    private TextView neededIngredientsText;

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
        slide_down = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_down);

        slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);
        getData();
        initViews();
        addEvents();
        initViewPager();
    }

    private void getData() {
        Intent intent = getIntent();
        String json = sharedPreferences.getString(getString(R.string.preference_stored_stuff_key),null);
        gson = new Gson();
        Type type = new TypeToken<ArrayList<Map<String,String>>>(){}.getType();
        if(json != null){
            stuffsInTula = gson.fromJson(json,type);
        }
        else{
            stuffsInTula = new ArrayList<>();
        }
        String cartJson = sharedPreferences.getString(getString(R.string.prefernece_cart_key),null);
        if(cartJson!= null){
            Type cartType = new TypeToken<ArrayList<Ingredient>>(){}.getType();
            cartIngres = gson.fromJson(cartJson,cartType);
        }
        else{
            cartIngres = new ArrayList<>();
        }
        currentRecipe = (Recipe) intent.getSerializableExtra("dish");
        listIngredient = currentRecipe.getIngredients();
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
        appBarLayout = findViewById(R.id.appbar);
        collapsingToolbarLayout = findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setTitle(currentRecipe.getName());
        addToCartButtonLayout = findViewById(R.id.add_to_cart_button_layout);
        neededIngredientsText = findViewById(R.id.needed_ingredients_text);

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

        neededIngredients = new ArrayList<>();
        boolean havingMoreThanNeeded = false;
        int ingresHaveMoreThanNedded = 0;

        List<Ingredient> notSpiceIngres = new ArrayList<>();
        for(int i =0 ;i<listIngredient.size();i++){
            if(!listIngredient.get(i).isSpice()){
                notSpiceIngres.add(listIngredient.get(i));
                Ingredient temp = new Ingredient(listIngredient.get(i));
                for(int j = 0;j<stuffsInTula.size();j++){
                    if(stuffsInTula.get(j).get("iName").equals(listIngredient.get(i).getName()) && haveMoreThanNeeded(i)){
                        havingMoreThanNeeded = true;
                        ingresHaveMoreThanNedded++;
                        break;
                    }
                    else if(stuffsInTula.get(j).get("iName").equals(listIngredient.get(i).getName()) && !haveMoreThanNeeded(i)){
                        if(temp.getUnit().equals(stuffsInTula.get(j).get("iUnit"))){
                            int quantityNeeded = Integer.parseInt(listIngredient.get(i).getQuantity()) - Integer.parseInt(stuffsInTula.get(j).get("iQuan"));
                            temp.setQuantity(String.valueOf(quantityNeeded));
                        }
                        havingMoreThanNeeded = false;
                        break;
                    }
                    else{
                        havingMoreThanNeeded = false;
                    }
                }
                if(!havingMoreThanNeeded){
                    //neededIngredients.add(listIngredient.get(i));
                    neededIngredients.add(temp);
                }
            }
        }

        //Log.d("hasmorethanneeded", neededIngredients.toString());
        if(ingresHaveMoreThanNedded == notSpiceIngres.size()){
            addToCartButtonLayout.setVisibility(View.INVISIBLE);
        }
        neededIngredientsText.setText(neededIngredients.size() + " nguyên liệu");

        addToCartButtonLayout.setOnClickListener(new View.OnClickListener() {
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
                    Snackbar.make((View)appBarLayout.getParent(),"Đã thêm " + neededIngredients.size() + " nguyên liệu còn thiếu vào giỏ hàng",Snackbar.LENGTH_SHORT).show();
                    addToCartButtonLayout.startAnimation(slide_down);
                    addToCartButtonLayout.setVisibility(View.INVISIBLE);
                    alreadyAddedToCart = true;
                }
                else{
                    Snackbar.make((View)appBarLayout.getParent(),"Bạn đã thêm những nguyên liệu này vào giỏ hàng!",Snackbar.LENGTH_SHORT).show();
                }
            }
        });

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
        if(!alreadyAddedToCart && neededIngredients.size() > 0){
            if(i == 1 && viewPagePreviousPage == 0){
                addToCartButtonLayout.startAnimation(slide_down);
                addToCartButtonLayout.setVisibility(View.INVISIBLE);

            }
            else if(i == 1 && viewPagePreviousPage == 2){
                addToCartButtonLayout.setVisibility(View.INVISIBLE);
            }
            else if(i == 2){
                addToCartButtonLayout.setVisibility(View.INVISIBLE);
            }
            else{
                addToCartButtonLayout.setVisibility(View.VISIBLE);
                addToCartButtonLayout.startAnimation(slide_up);
            }
        }
        else{
            addToCartButtonLayout.setVisibility(View.INVISIBLE);
        }

        viewPagePreviousPage = i;
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }
}
