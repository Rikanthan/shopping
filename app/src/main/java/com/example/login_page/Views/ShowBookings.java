package com.example.login_page.Views;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.example.login_page.Holder.BookingHolder;
import com.example.login_page.Holder.Bookings;
import com.example.login_page.Interface.ItemClickListner;
import com.example.login_page.R;
import com.example.login_page.customer.get_booking;
import com.example.login_page.notification.APIService;
import com.example.login_page.notification.Client;
import com.example.login_page.notification.Data;
import com.example.login_page.notification.MyResponse;
import com.example.login_page.notification.NotificationSender;
import com.example.login_page.notification.SendNotification;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowBookings extends AppCompatActivity
        implements  BookingHolder.OnItemClickListener {
    private static final String CHANNEL_ID = "100 " ;
    private APIService apiService;
    SendNotification sendNotification;
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    LinearLayoutManager linearLayoutManager;
    FirebaseAuth firebaseAuth;
    String fuser;
    Button deletebutton;
    BookingHolder mAdapter;
    List<String> checkTime;
    List<Bookings> newcartlist;
    Bookings _bookings;
    TextView showTime;
    String preDate="";
    String settedDate = "";
    String userId ;
    int pos =  0;
    int clickPosition = 0;
    Long totalPrice = Long.valueOf(0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bookings);
        recyclerView=findViewById(R.id.show_bookings);
        showTime = findViewById(R.id.showDateandTime);
        recyclerView.setHasFixedSize(true);
        _bookings = new Bookings();
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        settedDate = getIntent().getStringExtra("datetime");
        if(!settedDate.isEmpty() || settedDate != null)
        {
            showTime.setText(settedDate);
        }
        newcartlist=new ArrayList<>();
        firebaseAuth=FirebaseAuth.getInstance();
        fuser=FirebaseAuth.getInstance().getCurrentUser().getUid();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        databaseReference= FirebaseDatabase.getInstance().getReference("booking");

    }


    public void process()
    {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    System.out.println("data snapshot length "+snapshot.getChildrenCount());
                    if(pos < snapshot.getChildrenCount())
                    {
                        Bookings mycart = dataSnapshot.getValue(Bookings.class);
                        String name=mycart.getName();
                        String phone = mycart.getPhone();
                        String userId = mycart.getId();
                        String location = mycart.getLocation();
                        String price = mycart.getPrice();
                        String date = mycart.getDate();
                        preDate = date;

                        System.out.println(userId);
                        mycart.setDate(date);
                        mycart.setPhone(phone);
                        mycart.setLocation(location);
                        mycart.setId(userId);
                        mycart.setName(name);
                        mycart.setPrice(price);
                        Calendar calendar = Calendar.getInstance();
                        String s = settedDate;
                        try {
                            Date date2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").parse(s);
                            calendar.setTime(date2);
                            calendar.add(Calendar.MINUTE ,pos*5);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Date currentDate = calendar.getTime();
                        String pickupTime = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss a").format(currentDate);

                        Bookings mybookings = new Bookings(userId,price,name,phone,location,pickupTime);
                        newcartlist.add(mybookings);
                        FirebaseDatabase.getInstance().getReference().child("Confirmedbooking").child(pickupTime).setValue(mybookings);
                        pos++;
                    }
                    else
                    {
                        break;
                    }


                }

                mAdapter = new BookingHolder(ShowBookings.this, newcartlist);
                mAdapter.setOnItemClickListener(ShowBookings.this);
                recyclerView.setAdapter(mAdapter);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowBookings.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }

        });

    }


    public void setDateTime(View v)
    {
        Toast.makeText(ShowBookings.this,"Time setted successfully" , Toast.LENGTH_SHORT).show();
        process();

    }
    @Override
    public void onItemClick(final int position)
    {

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                        if(position == clickPosition)
                        {
                            Bookings mybookings = dataSnapshot.getValue(Bookings.class);
                            userId = mybookings.getId();
                            clickPosition = 0;
                            break;
                        }
                        else
                        {
                            clickPosition ++;
                        }
                    }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                System.out.println(error);
            }
        });
        System.out.println("id : "+userId);
        Intent intent = new Intent(ShowBookings.this,ShowOrders.class);
        intent.putExtra("id",userId);
        startActivity(intent);

    }
    public void sendNotifications(String usertoken, String title, String message) {
        Data data = new Data(title, message);
        NotificationSender sender = new NotificationSender(data, usertoken);

        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(ShowBookings.this, "Failed ", Toast.LENGTH_LONG);
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