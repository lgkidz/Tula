package com.odiousrainbow.leftovers;

import android.content.Intent;
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
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.odiousrainbow.leftovers.HomescreenFragments.CartFragment;
import com.odiousrainbow.leftovers.HomescreenFragments.HomeFragment;
import com.odiousrainbow.leftovers.HomescreenFragments.NotiFragment;
import com.odiousrainbow.leftovers.HomescreenFragments.PlanFragment;
import com.odiousrainbow.leftovers.HomescreenFragments.TulaFragment;


public class MainActivity extends AppCompatActivity {
    private Toolbar myToolbar;
    private BottomNavigationView mBottomNavigationBar;
    private FrameLayout mMainFrame;
    private NavigationView navigationView;
    private HomeFragment homeFragment;
    private TulaFragment tulaFragment;
    private CartFragment cartFragment;
    private PlanFragment planFragment;
    private NotiFragment notiFragment;
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;


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

        mBottomNavigationBar = findViewById(R.id.bottom_nav_bar);


        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, R.string.drawer_open,R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

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
            Intent settingIntent = new Intent(MainActivity.this,SettingActivity.class);
            startActivity(settingIntent);
        }
        else if(item.getItemId() == R.id.menu_feedback){
            Intent feedbackIntent = new Intent(MainActivity.this, FeedbackActivity.class);
            startActivity(feedbackIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}

