package com.odiousrainbow.leftovers.Activities;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.odiousrainbow.leftovers.Helpers.AlarmNotificationReceiver;
import com.odiousrainbow.leftovers.R;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private TextInputLayout textInputLayoutExpDate;
    private DatePickerDialog.OnDateSetListener onDateSetListener;
    private Calendar expDate;

    private List<Map<String,String>> stuffs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_stuff_details);
        et_name = findViewById(R.id.et_name);
        et_cate = findViewById(R.id.et_cate);
        unit_spinner = findViewById(R.id.unit_spinner);
        et_quan = findViewById(R.id.et_quan);
        et_exp_date = findViewById(R.id.et_expire_date);
        textInputLayoutExpDate = findViewById(R.id.textInputLayoutExpDate);
        cal_icon = findViewById(R.id.cal_icon);
        noti_switch = findViewById(R.id.noti_switch);
        exp_date_text = findViewById(R.id.exp_date_text);
        setToolbar();
        init();
    }

    public void setToolbar(){
        myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle(R.string.add_stuff_activity);
    }

    public void init(){
        Intent intentStartedThis = getIntent();
        String ingreName = intentStartedThis.getExtras().getString("ingreName");
        String ingreCate = intentStartedThis.getExtras().getString("ingreCate");
        et_name.setText(ingreName);
        et_cate.setText(ingreCate);



        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 2);
        exp_date_text.setText("Dùng trong 2 ngày");
        expDate = calendar;
        Date expDateRaw = calendar.getTime();
        String expDateString = dateFormat.format(expDateRaw);
        et_exp_date.setText(expDateString);

        onDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String dayzeroOrNot = dayOfMonth<10?"0":"";
                String monthzeroOrNot = month+1<10?"0":"";
                et_exp_date.setText(dayzeroOrNot + dayOfMonth + "/" + monthzeroOrNot + (month + 1) + "/" + year);
                Calendar curDate = Calendar.getInstance();
                expDate = Calendar.getInstance();

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

        noti_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){
                    textInputLayoutExpDate.setVisibility(View.GONE);
                    cal_icon.setVisibility(View.GONE);
                    exp_date_text.setVisibility(View.GONE);
                }
                else{
                    textInputLayoutExpDate.setVisibility(View.VISIBLE);
                    cal_icon.setVisibility(View.VISIBLE);
                    exp_date_text.setVisibility(View.VISIBLE);
                }
            }
        });


        int unitResourceId = R.array.mass_unit;
        switch (ingreCate.toLowerCase()){
            case "bột mì và gạo":
            case "thủy hải sản":
            case "hạt có vỏ":
            case "cá":
            case "thịt":
            case "đậu và đỗ":
                unitResourceId = R.array.mass_unit;
                break;
            case "bơ sữa":
                unitResourceId = R.array.dairy_unit;
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

    public void saveStuff(){
        if(!et_quan.getText().toString().equals("")){
            new AlertDialog.Builder(this).setTitle("Thêm nguyên liệu")
                    .setMessage("Bạn đã chắc chắn với những gì đã nhập chưa?")
                    .setPositiveButton("Oke", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPreferences = getSharedPreferences(getString(R.string.preference_file_key),MODE_PRIVATE);
                            String json = sharedPreferences.getString(getString(R.string.preference_stored_stuff_key),null);

                            Type type = new TypeToken<ArrayList<Map<String,String>>>(){}.getType();
                            Gson gson = new Gson();
                            stuffs = gson.fromJson(json,type);
                            if(json == null){
                                stuffs = new ArrayList<>();
                            }
                            String iName = et_name.getText().toString();
                            String iCate = et_cate.getText().toString();
                            String iQuan = et_quan.getText().toString();
                            String iUnit = unit_spinner.getSelectedItem().toString();
                            String iExpDate = et_exp_date.getText().toString();
                            String iNoti = String.valueOf(noti_switch.isChecked());
                            Map<String,String> m = new HashMap<>();
                            m.put("iName",iName);
                            m.put("iCate",iCate);
                            m.put("iQuan",iQuan);
                            m.put("iUnit",iUnit);
                            m.put("iExpDate",iExpDate);
                            m.put("iNoti",iNoti);
                            stuffs.add(m);

                            String stuffsInJson = gson.toJson(stuffs);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(getString(R.string.preference_stored_stuff_key),stuffsInJson);
                            editor.apply();

                            if(noti_switch.isChecked()){
                                setAlarmNotification();
                            }

                            Intent backToTulaIntent = new Intent(AddStuffDetailsActivity.this,MainActivity.class);
                            backToTulaIntent.putExtra("navigateToTula","true");
                            backToTulaIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(backToTulaIntent);
                        }
                    })
                    .setNegativeButton("Quay lại",null)
                    .show();
        }
        else{
            Toast.makeText(this,"Bạn cần nhập định lượng nguyên liệu.",Toast.LENGTH_LONG).show();
        }
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

    public void setAlarmNotification(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,AlarmNotificationReceiver.class);
        intent.putExtra("notiTitle","Nguyên liệu sắp hết hạn");
        intent.putExtra("notiMessage",et_name.getText().toString() + " của bạn sẽ hết hạn trong ngày!");
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this,1,intent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,expDate.getTimeInMillis(),pendingIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_stuff_save,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.add_stuff_save){
            saveStuff();
        }
        return super.onOptionsItemSelected(item);
    }
}

