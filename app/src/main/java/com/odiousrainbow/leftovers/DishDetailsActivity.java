package com.odiousrainbow.leftovers;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class DishDetailsActivity extends AppCompatActivity {
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dish_details);
        tv = findViewById(R.id.textView2);
        Intent intent = getIntent();

        String dishName = intent.getExtras().getString("name");
        tv.append(" " + dishName);
    }
}
