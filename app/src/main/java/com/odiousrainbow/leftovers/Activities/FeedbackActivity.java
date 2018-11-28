package com.odiousrainbow.leftovers.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odiousrainbow.leftovers.R;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FeedbackActivity extends AppCompatActivity {
    TextInputEditText et_email;
    TextInputEditText et_message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        et_email = findViewById(R.id.et_email);
        et_message = findViewById(R.id.et_message);
    }

    public void checkAndSendFeedBack() {
        if(et_email.getText().toString().trim().equals("") || et_message.getText().toString().trim().equals("")) {
            Toast.makeText(this,"Bạn cần nhập email và nội dung đánh giá.",Toast.LENGTH_LONG).show();
        }
        else{
            //SEND FEEDBACK
            new AlertDialog.Builder(this).setTitle("Cảm ơn phản hồi của bạn!")
                    .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent backToTulaIntent = new Intent(FeedbackActivity.this,MainActivity.class);
                            backToTulaIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(backToTulaIntent);
                        }
                    })
                    .show();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.feedback_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.menu_send_feedback){
            checkAndSendFeedBack();
        }
        return super.onOptionsItemSelected(item);
    }
}
