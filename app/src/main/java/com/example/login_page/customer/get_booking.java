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

import com.example.login_page.notification.Data;
import com.example.login_page.notification.MyResponse;
import com.example.login_page.notification.NotificationSender;
import com.example.login_page.notification.SendNotification;
import com.example.login_page.R;
import com.example.login_page.Views.ShowBookings;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class get_booking extends AppCompatActivity {
    private static final String CHANNEL_ID = "100 " ;
    private APIService apiService;
    TextView total;
    EditText name;
    EditText phone;
    EditText location;
    String price;
    String userId;
    FirebaseAuth firebaseAuth;
    SendNotification sendNotification;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sendNotification = new SendNotification();
        createNotificationChannel();
        setContentView(R.layout.activity_get_booking);
        total=(TextView)findViewById(R.id.total);
        name =(EditText)findViewById(R.id.customer_name);
        phone=(EditText)findViewById(R.id.customer_phone);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        location=(EditText)findViewById(R.id.customer_location);
        price =getIntent().getStringExtra("total");
        total.setText("Total : "+ price +"Rs");
        firebaseAuth = FirebaseAuth.getInstance();
        userId = firebaseAuth.getUid();
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void book(View v)
    {

        DateFormat format = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss");
        final String date = format.format(new Date());
        final HashMap<String,Object> cartMap=new HashMap<>();
        DatabaseReference databaseReference= FirebaseDatabase.getInstance().getReference("booking");//.child(currentDate.toString());
        cartMap.put("id",userId);
        cartMap.put("name",name.getText().toString().trim());
        cartMap.put("phone",phone.getText().toString().trim());
        cartMap.put("location",location.getText().toString().trim());
        cartMap.put("price",price);
        cartMap.put("date",date);
       // assert fuser != null;
        databaseReference.child(date).setValue(cartMap);
        UpdateToken();
        Toast.makeText(this,"Your items booked successfully",Toast.LENGTH_LONG).show();
        FirebaseDatabase.getInstance().getReference().child("Tokens").child("4VUgoUAvIgSNWgPFVCEYaFh1Mfd2")
                .child("token").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    //Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                   // String userToken = (String)map.get("token");
                    String userToken = dataSnapshot.getValue(String.class);
                    sendNotifications(userToken, "New Booking", "Customer booked items");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

       // Intent intent = new Intent(this, ShowBookings.class);
       // startActivity(intent);

    }
    public void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(token);
    }

    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);

        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(get_booking.this, "Failed ", Toast.LENGTH_LONG);
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

}