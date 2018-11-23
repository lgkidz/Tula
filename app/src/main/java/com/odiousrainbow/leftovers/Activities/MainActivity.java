package com.odiousrainbow.leftovers.Activities;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.miguelcatalan.materialsearchview.MaterialSearchView;
import com.odiousrainbow.leftovers.DataModel.Ingredient;
import com.odiousrainbow.leftovers.DataModel.Recipe;
import com.odiousrainbow.leftovers.HomescreenFragments.CartFragment;
import com.odiousrainbow.leftovers.HomescreenFragments.HomeFragment;
import com.odiousrainbow.leftovers.HomescreenFragments.NotiFragment;
import com.odiousrainbow.leftovers.HomescreenFragments.PlanFragment;
import com.odiousrainbow.leftovers.HomescreenFragments.TulaFragment;
import com.odiousrainbow.leftovers.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    private Toolbar myToolbar;
    public BottomNavigationView mBottomNavigationBar;
    private FrameLayout mMainFrame;
    private NavigationView navigationView;
    private HomeFragment homeFragment;
    private TulaFragment tulaFragment;
    private CartFragment cartFragment;
    private PlanFragment planFragment;
    private NotiFragment notiFragment;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private MaterialSearchView materialSearchView;
    private ListView listView;
    private ArrayAdapter searchAdapter;
    private FirebaseFirestore db;
    private List<Recipe> Recipedata = new ArrayList<>();
    private List<String> searchData = new ArrayList<>();
    private final String KEY_COLLECTION = "dishes";
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        myToolbar.setLogo(R.drawable.inapplogo);
        materialSearchView = findViewById(R.id.search_view);
        listView = findViewById(R.id.search_list_view);
        db = FirebaseFirestore.getInstance();
        getSearchData();


        materialSearchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
               if(newText != null && !newText.isEmpty()){
                   List<String> foundList = new ArrayList<>();
                   for(String item:searchData){
                       if(item.toLowerCase().contains(newText.toLowerCase())){
                           foundList.add(item);
                       }
                   }
                    if(foundList.size() > 0){
                        searchAdapter = new ArrayAdapter(MainActivity.this,android.R.layout.simple_list_item_1,foundList);
                        listView.setAdapter(searchAdapter);
                        listView.setVisibility(View.VISIBLE);
                    }
                    else{
                        listView.setVisibility(View.INVISIBLE);
                    }
               }
               else{
                   listView.setVisibility(View.INVISIBLE);
               }

               return true;
            }
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String itemName =  listView.getItemAtPosition(position).toString();
                for(Recipe r : Recipedata){
                    if(r.getName().equals(itemName)){
                        Intent intentDiskDetails = new Intent(MainActivity.this,DishDetailsActivity.class);
                        intentDiskDetails.putExtra("dish",r);
                        startActivity(intentDiskDetails);
                        break;
                    }
                }
            }
        });

        mBottomNavigationBar = findViewById(R.id.bottom_nav_bar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, R.string.drawer_open,R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        LayoutTransition lt = (mDrawerLayout).getLayoutTransition();
        lt.disableTransitionType(LayoutTransition.APPEARING);
        lt.disableTransitionType(LayoutTransition.DISAPPEARING);

        materialSearchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                LayoutTransition lt = (mDrawerLayout).getLayoutTransition();
                lt.enableTransitionType(LayoutTransition.APPEARING);
                lt.enableTransitionType(LayoutTransition.DISAPPEARING);
            }

            @Override
            public void onSearchViewClosed() {
                listView.setVisibility(View.INVISIBLE);
                LayoutTransition lt = (mDrawerLayout).getLayoutTransition();
                lt.disableTransitionType(LayoutTransition.APPEARING);
                lt.disableTransitionType(LayoutTransition.DISAPPEARING);
            }
        });
        navigationView = findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.drawer_menu_home:
                        mBottomNavigationBar.setSelectedItemId(R.id.bottom_nav_home);
                        mDrawerLayout.closeDrawer(Gravity.START,true);
                        return true;
                    case R.id.drawer_menu_tula:
                        mBottomNavigationBar.setSelectedItemId(R.id.bottom_nav_tula);
                        mDrawerLayout.closeDrawer(Gravity.START,true);
                        return true;
                    case R.id.drawer_menu_cart:
                        mBottomNavigationBar.setSelectedItemId(R.id.bottom_nav_cart);
                        mDrawerLayout.closeDrawer(Gravity.START,true);
                        return true;
                    case R.id.drawer_menu_plan:
                        mBottomNavigationBar.setSelectedItemId(R.id.bottom_nav_plan);
                        mDrawerLayout.closeDrawer(Gravity.START,true);
                        return true;
                    case R.id.drawer_menu_noti:
                        mBottomNavigationBar.setSelectedItemId(R.id.bottom_nav_noti);
                        mDrawerLayout.closeDrawer(Gravity.START,true);
                        return true;
                    case R.id.drawer_menu_fav:
                        Intent favIntent = new Intent(MainActivity.this,FavoriteDishesActivity.class);
                        startActivity(favIntent);
                        mDrawerLayout.closeDrawer(Gravity.START,true);
                        return true;
                    default:
                        return false;
                }
            }
        });

        mMainFrame = findViewById(R.id.main_frame);

        mBottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.bottom_nav_home:
                        homeFragment = new HomeFragment();
                        setFragment(homeFragment);
                        return true;
                    case R.id.bottom_nav_tula:
                        tulaFragment = new TulaFragment();
                        setFragment(tulaFragment);
                        return true;
                    case R.id.bottom_nav_cart:
                        cartFragment = new CartFragment();
                        setFragment(cartFragment);
                        return true;
                    case R.id.bottom_nav_plan:
                        planFragment = new PlanFragment();
                        setFragment(planFragment);
                        return true;
                    case R.id.bottom_nav_noti:
                        notiFragment = new NotiFragment();
                        setFragment(notiFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
        if(null == savedInstanceState) {
            setFragment(new HomeFragment());
        }
        Intent intent = getIntent();
        if(intent.hasExtra("navigateToTula")){
            mBottomNavigationBar.setSelectedItemId(R.id.bottom_nav_tula);
        }
    }

    private void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);
        materialSearchView.setMenuItem(searchItem);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(mDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }
        if(item.getItemId() == R.id.menu_up_to_pro){
            Intent UpgradeToProIntent = new Intent(MainActivity.this, UpgradeToProActivity.class);
            startActivity(UpgradeToProIntent);
        }
        else if(item.getItemId() == R.id.menu_setting){
            Intent settingIntent = new Intent(MainActivity.this,SettingsActivity.class);
            startActivity(settingIntent);
        }
        else if(item.getItemId() == R.id.menu_feedback){
            Intent feedbackIntent = new Intent(MainActivity.this, FeedbackActivity.class);
            startActivity(feedbackIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if(materialSearchView.isSearchOpen()){
            materialSearchView.closeSearch();
        }
        else{
            super.onBackPressed();
        }
    }



    public void getSearchData(){
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
                            Recipedata.add(recipe);
                            searchData.add(recipe.getName());

                        }
                    }
                });
    }

}

