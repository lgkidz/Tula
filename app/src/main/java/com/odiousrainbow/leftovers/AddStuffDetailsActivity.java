package com.odiousrainbow.leftovers;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.YearMonth;
import java.util.Calendar;
import java.util.Date;

public class AddStuffDetailsActivity extends AppCompatActivity {
    private Toolbar myToolbar;
    private TextInputEditText et_name;
    private TextInputEditText et_cate;
    private Spinner unit_spinner;
    private TextInputEditText et_quan;
    private TextInputEditText et_exp_date;
    private TextView exp_date_text;
    private ImageView cal_icon;
    private Switch noti_switch;
    private DatePickerDialog.OnDateSetListener onDateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stuff_details);
        setToolbar();
        Intent intentStartedThis = getIntent();
        String ingreName = intentStartedThis.getExtras().getString("ingreName");
        String ingreCate = intentStartedThis.getExtras().getString("ingreCate");


        et_name = findViewById(R.id.et_name);
        et_cate = findViewById(R.id.et_cate);
        unit_spinner = findViewById(R.id.unit_spinner);
        et_quan = findViewById(R.id.et_quan);
        et_exp_date = findViewById(R.id.et_expire_date);
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 7);
        Date expDateRaw = calendar.getTime();
        String expDateString = dateFormat.format(expDateRaw);
        et_exp_date.setText(expDateString);

        exp_date_text = findViewById(R.id.exp_date_text);

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dayzeroOrNot = dayOfMonth<10?"0":"";
                String monthzeroOrNot = month+1<10?"0":"";
                et_exp_date.setText(dayzeroOrNot + dayOfMonth + "/" + monthzeroOrNot + (month + 1) + "/" + year);
                Calendar curDate = Calendar.getInstance();
                Calendar expDate = Calendar.getInstance();
                int curDay = curDate.get(Calendar.DAY_OF_MONTH);
                int curYear = curDate.get(Calendar.YEAR);
                int curMonth = curDate.get(Calendar.MONTH);
                String yearsBetween = "";
                String monthsBetween = "";
                String daysBetween = "";

                expDate.set(year,month,dayOfMonth);
                long diff = expDate.getTimeInMillis() - curDate.getTimeInMillis();
                float dayCount = (float) diff / (24 * 60 * 60 * 1000);
                if((int)dayCount <= curDate.getActualMaximum(Calendar.DAY_OF_MONTH)){
                    exp_date_text.setText("Dùng trong " + (int) dayCount + " ngày");
                }
                else if((int)dayCount/365 > 0){
                    exp_date_text.setText("Dùng trong khoảng " + Math.round(dayCount/365)  + " năm");
                }
                else{
                    exp_date_text.setText("Dùng trong khoảng " + Math.round(dayCount/30)  + " tháng");
                }

            }
        };

        cal_icon = findViewById(R.id.cal_icon);
        noti_switch = findViewById(R.id.noti_switch);



        et_name.setText(ingreName);
        et_cate.setText(ingreCate);
        int unitResourceId = R.array.mass_unit;
        switch (ingreCate.toLowerCase()){
            case "bột mì và gạo":
            case "bơ sữa":
            case "thủy hải sản":
            case "hạt có vỏ":
            case "cá":
            case "thịt":
            case "đậu và đỗ":
                unitResourceId = R.array.mass_unit;
                break;
            case "trứng":
                unitResourceId = R.array.egg_unit;
                break;
            case "hoa quả":
                unitResourceId = R.array.fruit_unit;
                break;
            case "rau củ":
                unitResourceId = R.array.veg_unit;
                break;
            default:
                unitResourceId = R.array.mass_unit;
                break;
        }


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,unitResourceId,R.layout.spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        unit_spinner.setAdapter(adapter);
    }

    public void setToolbar(){
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.add_stuff_activity);
    }

    public void showDatePicker(View v){
        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, onDateSetListener, year, month, day);
        datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
        datePickerDialog.show();
    }

}

