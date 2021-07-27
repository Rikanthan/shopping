package com.example.login_page.Views;
import androidx.annotation.NonNull;
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
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.login_page.Holder.CartViewHolder;
import com.example.login_page.Product.Cart;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowOrders extends AppCompatActivity implements  CartViewHolder.OnItemClickListener {
RecyclerView recyclerView;
DatabaseReference databaseReference;
LinearLayoutManager linearLayoutManager;
FirebaseAuth firebaseAuth;
boolean isAdmin = false;
String fuser;
String customer;
Button deletebutton, confirmButton;
CartViewHolder mAdapter;
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
        confirmButton = findViewById(R.id.confirmOrder);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        newcartlist = new ArrayList<>();
        firebaseAuth = FirebaseAuth.getInstance();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        customer   =   getIntent().getStringExtra("id");
        if( customer == null)
        {
            fuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        }
        else
        {
            fuser = customer;
        }
        System.out.println(fuser);
        if(!FirebaseAuth.getInstance().getUid().contains(fuser))
        {
            isAdmin = true;
            confirmButton.setText("Confirmation");
            confirmButton.setBackgroundColor(Color.GREEN);
        }
        databaseReference=FirebaseDatabase.getInstance().getReference("orders").child(fuser);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Cart mycart=dataSnapshot.getValue(Cart.class);
                    String Name=mycart.getPname();
                    Long quantity=mycart.getQuantity();
                    Long price=mycart.getPrice();
                    totalPrice = totalPrice + price*quantity;
                    mycart.setPname(Name);
                    mycart.setQuantity(quantity);
                    mycart.setPrice(price);
                    Cart showcart=new Cart(Name,quantity,price);
                    newcartlist.add(showcart);
                }
                mAdapter = new CartViewHolder(ShowOrders.this, newcartlist);
                mAdapter.setOnItemClickListener(ShowOrders.this);
              recyclerView.setAdapter(mAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ShowOrders.this, error.getMessage(), Toast.LENGTH_SHORT).show();

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
                                        Intent intent = new Intent(ShowOrders.this,ShowOrders.class);
                                        startActivity(intent);
                                        break;
                                    }
                                    pos++;
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                Toast.makeText(ShowOrders.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
                                        Toast.makeText(ShowOrders.this, error.getMessage(), Toast.LENGTH_SHORT).show();
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
            Toast.makeText(ShowOrders.this,"Total price is :" +totalPrice.toString()+ "Rs", Toast.LENGTH_SHORT).show();
            Intent i=new Intent(ShowOrders.this, get_booking.class);
            i.putExtra("total",totalPrice.toString());
            startActivity(i);
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
                        Toast.makeText(ShowOrders.this, "Failed ", Toast.LENGTH_LONG);
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