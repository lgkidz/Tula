package com.odiousrainbow.leftovers.Activities;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

import com.odiousrainbow.leftovers.R;

public class UpgradeToProActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upgrade_to_pro);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.upgrade_to_pro_activity_title);
    }

    @Override
    public void onBackPressed() {
        Intent backToMainScreen = new Intent(this,MainActivity.class);
        backToMainScreen.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(backToMainScreen);
    }

    public void onDevelopment(View v){
        Toast.makeText(this, "Tính năng đang trong quá trình phát triển", Toast.LENGTH_SHORT).show();
    }

}
