package com.odiousrainbow.leftovers;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {
    private BottomNavigationView mBottomNavigationBar;
    private FrameLayout mMainFrame;
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
        mBottomNavigationBar = findViewById(R.id.bottom_nav_bar);

        mDrawerLayout = findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(MainActivity.this, mDrawerLayout, R.string.drawer_open,R.string.drawer_close);
        mDrawerLayout.addDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();
        ActionBar actionBar = getSupportActionBar();


        actionBar.setDisplayHomeAsUpEnabled(true);



        mMainFrame = findViewById(R.id.main_frame);

        homeFragment = new HomeFragment();
        tulaFragment = new TulaFragment();
        cartFragment = new CartFragment();
        planFragment = new PlanFragment();
        notiFragment = new NotiFragment();

        mBottomNavigationBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.bottom_nav_home:
                        setFragment(homeFragment);
                        return true;
                    case R.id.bottom_nav_tula:
                        setFragment(tulaFragment);
                        return true;
                    case R.id.bottom_nav_cart:
                        setFragment(cartFragment);
                        return true;
                    case R.id.bottom_nav_plan:
                        setFragment(planFragment);
                        return true;
                    case R.id.bottom_nav_noti:
                        setFragment(notiFragment);
                        return true;
                    default:
                        return false;
                }
            }
        });
        setFragment(homeFragment);


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
            Toast.makeText(this,"This feature is still in development!",Toast.LENGTH_SHORT).show();
        }
        else if(item.getItemId() == R.id.menu_setting){
            Intent setttingIntent = new Intent(MainActivity.this,SettingActivity.class);
            startActivity(setttingIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}

