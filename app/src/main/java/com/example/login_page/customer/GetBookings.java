package com.example.login_page.customer;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.login_page.Product.Cart;
import com.example.login_page.Views.Member;
import com.example.login_page.Views.SeeTimer;
import com.example.login_page.notification.Data;
import com.example.login_page.notification.MyResponse;
import com.example.login_page.notification.NotificationSender;
import com.example.login_page.notification.SendNotification;
import com.example.login_page.R;
import com.example.login_page.notification.APIService;
import com.example.login_page.notification.Client;
import com.example.login_page.notification.Token;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetBookings extends AppCompatActivity {
    private static final String CHANNEL_ID = "100 " ;
    private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
    private APIService apiService;
    TextView total;
    EditText name;
    EditText phone;
    EditText location;
    String price;
    String userId;
    String getDate;
    FirebaseAuth firebaseAuth;
    SendNotification sendNotification;
    SimpleDateFormat format;
    Date currentTime;
    String adminId;
    List<Cart> backupCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendNotification = new SendNotification();
        createNotificationChannel();
        setContentView(R.layout.activity_get_booking);
        total=(TextView)findViewById(R.id.total);
        name =(EditText)findViewById(R.id.customer_name);
        phone=(EditText)findViewById(R.id.customer_phone);
        backupCart = new ArrayList<>();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        location=(EditText)findViewById(R.id.customer_location);
        price = getIntent().getStringExtra("total");
        getDate = getIntent().getStringExtra("date");
       // backupCart = getIntent().getParcelableExtra("carts");
        total.setText("Total : Rs"+ price);
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getUid();
        format = new SimpleDateFormat(DATE_FORMAT);
        getUserDetails();
        adminId = "4VUgoUAvIgSNWgPFVCEYaFh1Mfd2";
        currentTime = new Date();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void book(View v)
    {
            final HashMap<String,Object> cartMap=new HashMap<>();
            DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("booking");
            cartMap.put("id",userId);
            cartMap.put("name",name.getText().toString().trim());
            cartMap.put("phone",phone.getText().toString().trim());
            cartMap.put("location",location.getText().toString().trim());
            cartMap.put("price",price);
            cartMap.put("date",getDate);
            databaseReference.child(getDate).setValue(cartMap);
            UpdateToken();
            Toast.makeText(this,"Your items booked successfully",Toast.LENGTH_LONG).show();
            FirebaseDatabase
                    .getInstance()
                    .getReference()
                    .child("Tokens")
                    .child(adminId)
                    .child("token")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.exists())
                    {
                        String userToken = dataSnapshot.getValue(String.class);
                        sendNotifications( userToken, "New Booking", name.getText().toString()+" booked items");
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



    }
    public void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase
                .getInstance()
                .getReference("Tokens")
                .child(
                        FirebaseAuth
                                .getInstance()
                                .getCurrentUser()
                                .getUid())
                                    .setValue(token);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void sendNotifications(String usertoken, String title, String message) {
        Date currentTime = new Date();
        String date = new SimpleDateFormat("dd-MMM-yy HH:mm:ss").format(currentTime);
        Data data = new Data(title, message, date,"Unread");
        NotificationSender sender = new NotificationSender(data, usertoken);
        FirebaseDatabase
                .getInstance()
                .getReference("Notification")
                .child(adminId).child(date).setValue(data);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(GetBookings.this, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

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
    public void getUserDetails()
    {
        FirebaseDatabase.getInstance().getReference("Member")
                .child(userId).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                           Member member = snapshot.getValue(Member.class);
                           name.setText(member.getName());
                           location.setText(member.getLocation());
                           phone.setText(String.valueOf(member.getMobile()));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }



}