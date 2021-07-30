package com.example.login_page.Admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class SetDateActivity extends AppCompatActivity implements
        OnClickListener {

    Button btnDatePicker, btnTimePicker;
    EditText txtDate, txtTime;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private String format = "";
    String date = "";
    String time = "";
    String getHour = "";
    String getMinute = "";
    String getDigital = "";
    String activity;
    boolean isCountdown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_datectivity);

        btnDatePicker=(Button)findViewById(R.id.btn_date);
        btnTimePicker=(Button)findViewById(R.id.btn_time);
        txtDate=(EditText)findViewById(R.id.in_date);
        txtTime=(EditText)findViewById(R.id.in_time);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        activity = getIntent().getStringExtra("Activity");
        if(activity != null && activity.contains("BookingTime"))
        {
            isCountdown = false;
        }
        else if(activity != null && activity.contains("setTimer"))
        {
            isCountdown = true;
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
            FirebaseDatabase.getInstance().getReference("Timer").setValue(getDigital);
            startActivity(intent1);
        }
    }
}