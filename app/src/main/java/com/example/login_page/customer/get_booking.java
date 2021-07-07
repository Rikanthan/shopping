package com.example.login_page.customer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login_page.R;
import com.example.login_page.Views.showorders;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class get_booking extends AppCompatActivity {
TextView total;
EditText name;
EditText phone;
EditText location;
String price;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_booking);
        total=(TextView)findViewById(R.id.total);
        name =(EditText)findViewById(R.id.customer_name);
        phone=(EditText)findViewById(R.id.customer_phone);
        location=(EditText)findViewById(R.id.customer_location);
        price =getIntent().getStringExtra("total");
        total.setText("Total : "+ price +"Rs");

    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void book(View v)
    {

        String saveCurrentTime,saveCurrentdate;
        Calendar calendar=Calendar.getInstance();
        DateFormat format = new SimpleDateFormat("YYYYMMdd_hhmmss a");
        String date = format.format(new Date());

       // long d = System.currentTimeMillis();
       // String date = format.format(new Date(d));
        SimpleDateFormat currentDate=new SimpleDateFormat("dd MMM, yyyy");
        saveCurrentdate=currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());
        final HashMap<String,Object> cartMap=new HashMap<>();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("bookings");//.child(currentDate.toString());
//        databaseReference.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if(snapshot.exists())
//                {
//                    oid=(snapshot.getChildrenCount());
//                }
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//            }
//        });
        cartMap.put("name",name.getText().toString().trim());
        cartMap.put("phone",phone.getText().toString().trim());
        cartMap.put("location",location.getText().toString().trim());
        cartMap.put("price",price);
        cartMap.put("date",date);

       // assert fuser != null;
        databaseReference.child(date).setValue(cartMap);
        Toast.makeText(this,"Your items booked successfully",Toast.LENGTH_LONG).show();
    }
}