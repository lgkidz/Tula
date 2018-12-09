package com.odiousrainbow.leftovers.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.odiousrainbow.leftovers.R;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FeedbackActivity extends AppCompatActivity {
    TextInputEditText et_email;
    TextInputEditText et_message;
    private FirebaseFirestore db;
    public static final String COLLECTION_NAME = "feedbacks";

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
        db = FirebaseFirestore.getInstance();
    }

    public void checkAndSendFeedBack() {
        Pattern pattern = Pattern.compile("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,4}");
        Matcher mat = pattern.matcher(et_email.getText().toString());
        if(!mat.matches() || et_message.getText().toString().trim().equals("")) {
            Toast.makeText(this,"Bạn cần nhập email và nội dung đánh giá.",Toast.LENGTH_LONG).show();
        }
        else{
            //SEND FEEDBACK
            new AlertDialog.Builder(this).setTitle("Cảm ơn phản hồi của bạn!")
                    .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Map<String, Object> data = new HashMap<>();
                            data.put("email",et_email.getText().toString());
                            data.put("content",et_message.getText().toString());
                            db.collection(COLLECTION_NAME).add(data);
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
