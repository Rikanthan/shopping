package com.example.login_page.Admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.login_page.R;
import com.example.login_page.Views.SeeTimer;
import com.example.login_page.Views.ShowBookings;
import com.example.login_page.customer.GetBookings;
import com.example.login_page.notification.APIService;
import com.example.login_page.notification.Client;
import com.example.login_page.notification.Data;
import com.example.login_page.notification.MyResponse;
import com.example.login_page.notification.NotificationSender;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SetDateActivity extends AppCompatActivity implements
        OnClickListener {
    private static final String CHANNEL_ID = "100 " ;
    private APIService apiService;
    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    TextView timeText;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String format = "";
    String date = "";
    String time = "";
    String getHour = "";
    String getMinute = "";
    String getDigital = "";
    String uid;
    boolean isCountdown;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_datectivity);
        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);
        timeText = (TextView) findViewById(R.id.timeText);
        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        uid = FirebaseAuth.getInstance().getUid();
        createNotificationChannel();
        isCountdown = getIntent().getBooleanExtra("Activity",false);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);

        if(isCountdown)
        {
            timeText.setText("Set Count Down");
        }
        else
        {
            timeText.setText("Set Booking time");
        }
    }

    @Override
    public void onClick(View v) {

        if (v == btnDatePicker) {

            // Get Current Date
            final Calendar c = Calendar.getInstance();
            mYear = c.get(Calendar.YEAR);
            mMonth = c.get(Calendar.MONTH);
            mDay = c.get(Calendar.DAY_OF_MONTH);


            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            if(dayOfMonth < 10 && monthOfYear < 10)
                            {
                                txtDate.setText(year + "-0" + (monthOfYear + 1) + "-0" + dayOfMonth);
                            }
                            else if(dayOfMonth < 10)
                            {
                                txtDate.setText(year + "-" + (monthOfYear + 1) + "-0" + dayOfMonth);
                            }
                            else if(monthOfYear < 10)
                            {
                                txtDate.setText(year + "-0" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                            else
                            {
                                txtDate.setText(year + "-" + (monthOfYear + 1) + "-" + dayOfMonth);
                            }
                        }
                    }, mYear, mMonth, mDay);
            datePickerDialog.show();
        }
        if (v == btnTimePicker) {

            // Get Current Time
            final Calendar c = Calendar.getInstance();
            mHour = c.get(Calendar.HOUR_OF_DAY);
            mMinute = c.get(Calendar.MINUTE);

            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {

                        @Override
                        public void onTimeSet(TimePicker view, int hour,
                                              int minute) {
                            getHour = String.valueOf(hour);
                            getMinute = String.valueOf(minute);
                            if (hour == 0) {
                                hour += 12;
                                format = ":00 AM";
                            } else if (hour == 12) {
                                format = ":00 PM";
                            } else if (hour > 12) {
                                hour -= 12;
                                format = ":00 PM";
                            } else {
                                format = ":00 AM";
                            }
                            if(minute < 10 && hour <10)
                            {
                                txtTime.setText("0"+hour+":0"+minute+format);
                            }
                            else if(minute < 10)
                            {
                                txtTime.setText(hour+":0"+minute+format);
                            }
                            else if(hour < 10)
                            {
                                txtTime.setText("0"+hour+":"+minute+format);
                            }
                            else
                            {
                                txtTime.setText(hour + ":" + minute+format);
                            }
                        }
                    }, mHour, mMinute, false);
            timePickerDialog.show();
        }
    }
    public void set(View v)
    {
        Intent intent = new Intent(this, ShowBookings.class);
        Intent intent1 = new Intent(this, SeeTimer.class);
        date = txtDate.getText().toString();
        time = txtTime.getText().toString();
        getDigital = date+" "+getHour+":"+getMinute+":00";
        if(date.isEmpty() || time.isEmpty())
        {
            Toast.makeText(this,"Please select date and time",Toast.LENGTH_LONG).show();
        }
        else
        {
            if(!isCountdown)
            {
                intent.putExtra("datetime",date+" "+time);
                startActivity(intent);
            }
            else
            {
                FirebaseDatabase.getInstance().getReference("Timer").setValue(getDigital);
                startActivity(intent1);
                notifyCounters(uid);
            }

        }
    }
    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);

        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(SetDateActivity.this, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
    public void notifyCounters(final String uid)
    {
        FirebaseDatabase
                .getInstance()
                .getReference("Tokens")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            if(dataSnapshot.exists())
                            {
                                Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                                String userToken = (String)map.get("token");
                                sendNotifications(userToken,"Booking Alert","Booking count down starts now!");
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }
    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "NotificationChannel";
            String description = "New Booking receive";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setLightColor(Color.CYAN);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }
}