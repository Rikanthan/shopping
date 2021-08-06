package com.example.login_page.Views;

import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login_page.Holder.CartViewHolder;
import com.example.login_page.Product.Cart;
import com.example.login_page.R;
import com.example.login_page.customer.GetBookings;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowConfirmOrders extends AppCompatActivity implements  CartViewHolder.OnItemClickListener {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    LinearLayoutManager linearLayoutManager;
    FirebaseAuth firebaseAuth;
    boolean isAdmin = false;
    String fuser;
    String customer, bookedDate;
    Button deletebutton, confirmButton;
    CartViewHolder mAdapter;
    ProgressBar progressBar;
    List<Cart> newcartlist;
    private static final String CHANNEL_ID = "100 " ;
    private APIService apiService;
    SendNotification sendNotification;
    String del="";
    int pos = 0;
    Long totalPrice = Long.valueOf(0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showorders);
        recyclerView   =  findViewById(R.id.show_cart);
        recyclerView.setHasFixedSize(true);
        deletebutton = findViewById(R.id.idelete);
        progressBar = findViewById(R.id.progress_circle);
        confirmButton = findViewById(R.id.confirmOrder);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        newcartlist = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        customer   =   getIntent().getStringExtra("id");
        bookedDate = getIntent().getStringExtra("date");
        if( customer == null)
        {
            fuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        else
        {
            fuser = customer;
        }
        System.out.println(fuser+" "+bookedDate);
        if(!FirebaseAuth.getInstance().getUid().contains(fuser))
        {
            isAdmin = true;
            confirmButton.setText("Confirmation");
            confirmButton.setVisibility(View.VISIBLE);
        }
        databaseReference= FirebaseDatabase.getInstance()
                .getReference("ordersBackup")
                .child(fuser)
                .child(bookedDate);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                newcartlist.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Cart mycart=dataSnapshot.getValue(Cart.class);
                    String Name=mycart.getPname();
                    Long quantity=mycart.getQuantity();
                    Long price=mycart.getPrice();
                    String imageUrl = mycart.getImageUrl();
                    totalPrice = totalPrice + price*quantity;
                    mycart.setPname(Name);
                    mycart.setQuantity(quantity);
                    mycart.setPrice(price);
                    Cart showcart=new Cart(Name,quantity,price,imageUrl);
                    newcartlist.add(showcart);
                }
                mAdapter = new CartViewHolder(ShowConfirmOrders.this, newcartlist);
                mAdapter.setOnItemClickListener(ShowConfirmOrders.this);
                recyclerView.setAdapter(mAdapter);
                progressBar.setVisibility(View.GONE);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowConfirmOrders.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public void onItemClick(final int position)
    {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure , You wanted to remove the item from cart");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        databaseReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot uselessSnapshot: snapshot.getChildren()) {
                                    if(pos == position)
                                    {
                                        uselessSnapshot.getRef().removeValue();
                                        pos = 0;
                                        Intent intent = new Intent(ShowConfirmOrders.this,ShowConfirmOrders.class);
                                        startActivity(intent);
                                        break;
                                    }
                                    pos++;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(ShowConfirmOrders.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

    }

    public void delete(View view){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure , You wanted to remove the item from cart");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        databaseReference.child(String.valueOf(pos)).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
                                    appleSnapshot.getRef().removeValue();
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(ShowConfirmOrders.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void confirm(View v)
    {
        if(isAdmin)
        {
            FirebaseDatabase.getInstance().getReference("Tokens")
                    .child(fuser).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if(snapshot.exists())
                    {
                        Map<String, Object> map = (Map<String, Object>) snapshot.getValue();
                        String userToken = (String)map.get("token");
                        sendNotifications(userToken,"Booking confirmed!","Your things are ready");
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else
        {
            Toast.makeText(ShowConfirmOrders.this,"Total price is :" +totalPrice.toString()+ "Rs", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(ShowConfirmOrders.this, GetBookings.class);
            i.putExtra("total",totalPrice.toString());
            startActivity(i);
        }

    }
    public void sendNotifications(String usertoken, String title, String message) {
        Date currentTime = new Date();
        String date = new SimpleDateFormat("dd-MMM-yy HH:mm:ss").format(currentTime);
        Data data = new Data(title, message, date,"Unread");
        NotificationSender sender = new NotificationSender(data, usertoken);
        FirebaseDatabase
                .getInstance()
                .getReference("Notification")
                .child(fuser).child(date).setValue(data);
        apiService.sendNotifcation(sender).enqueue(new Callback<MyResponse>() {
            @Override
            public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                if (response.code() == 200) {
                    if (response.body().success != 1) {
                        Toast.makeText(ShowConfirmOrders.this, "Failed ", Toast.LENGTH_LONG);
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