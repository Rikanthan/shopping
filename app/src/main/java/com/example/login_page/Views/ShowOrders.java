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
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.login_page.Holder.CartViewHolder;
import com.example.login_page.Images.Upload;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
List<String> keys;
private static final String CHANNEL_ID = "100 " ;
private String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";
private APIService apiService;
SimpleDateFormat format;
ProgressBar progressBar;
Date currentTime;
Upload upload;
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
        progressBar = findViewById(R.id.progress_circle);
        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        newcartlist = new ArrayList<Cart>();
        keys = new ArrayList<>();
        upload = new Upload();
        firebaseAuth = FirebaseAuth.getInstance();
        createNotificationChannel();
        format = new SimpleDateFormat(DATE_FORMAT);
        currentTime = new Date();
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        customer   =   getIntent().getStringExtra("id");
        if( customer == null)
        {
            fuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
            confirmButton.setVisibility(View.VISIBLE);
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
                newcartlist.clear();
                keys.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    if(snapshot.exists())
                    {
                        Cart mycart = dataSnapshot.getValue(Cart.class);
                        keys.add(dataSnapshot.getKey());
                        String Name=mycart.getPname();
                        Long quantity=mycart.getQuantity();
                        Long price=mycart.getPrice();
                        String imageUrl = mycart.getImageUrl();
                        totalPrice = totalPrice + price*quantity;
                        Cart showcart=new Cart(Name,quantity,price,imageUrl,mycart.getProductId());
                        newcartlist.add(showcart);
                    }

                }
                mAdapter = new CartViewHolder(ShowOrders.this, newcartlist);
                mAdapter.setOnItemClickListener(ShowOrders.this);
              recyclerView.setAdapter(mAdapter);
              progressBar.setVisibility(View.GONE);

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
                        pos = position;
                        Cart getCart = newcartlist.get(position);
                        restores(getCart.getProductId(),getCart.getPrice());
                        newcartlist.remove(position);
                        databaseReference.child(keys.get(position)).removeValue();
                        keys.remove(position);
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
                                Cart getCart = newcartlist.get(pos);
                                restores(getCart.getProductId(),getCart.getPrice());
                                newcartlist.remove(pos);
                                databaseReference.child(keys.get(pos)).removeValue();
                                keys.remove(pos);
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
            FirebaseDatabase
                    .getInstance()
                    .getReference("Timer")
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    if(snapshot.exists())
                                    {
                                        String date = snapshot.getValue(String.class);
                                        try {
                                            Date eventdate = format.parse(date);
                                            if(eventdate.getTime() - currentTime.getTime() > 0)
                                            {
                                                Toast.makeText(ShowOrders.this,"Total price is :" +totalPrice.toString()+ "Rs", Toast.LENGTH_SHORT).show();
                                                Intent i=new Intent(ShowOrders.this, GetBookings.class);
                                                i.putExtra("total",totalPrice.toString());
                                                i.putExtra("date",getMove());
                                                startActivity(i);
                                            }
                                            else
                                            {
                                                Toast.makeText(ShowOrders.this,"Sorry! Booking time limit exceeds",Toast.LENGTH_LONG).show();
                                                Intent intent = new Intent(ShowOrders.this, SeeTimer.class);
                                                startActivity(intent);
                                            }
                                        } catch (ParseException e) {
                                            e.printStackTrace();
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {

                                }
                            }
                    );
        }

    }
    public String getMove()
    {
        final String date = format.format(currentTime);
        for(Cart cart: newcartlist)
        {
            long temp = cart.getPrice();
            cart.setPrice(cart.getQuantity());
            cart.setQuantity(temp);
            FirebaseDatabase.getInstance().getReference("ordersBackup")
                    .child(fuser).child(date).child( UUID.randomUUID().toString()).setValue(cart);
        }

        FirebaseDatabase.getInstance().getReference("orders").child(fuser).getRef().removeValue();
        return date;
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
                        Toast.makeText(ShowOrders.this, "Failed ", Toast.LENGTH_LONG);
                    }
                }
            }

            @Override
            public void onFailure(Call<MyResponse> call, Throwable t) {

            }
        });
    }
    public void restores(final String productId, final long quantity)
    {
        FirebaseDatabase
                .getInstance()
                .getReference("Uploads")
                .child(productId)
                .addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                   upload = snapshot.getValue(Upload.class);
                                   long quan = Integer.parseInt(upload.getmQuantity());
                                   Upload newUpload = new Upload(upload.getName(),upload.getImageUrl(),upload.getmPrice(),String.valueOf(quan+quantity) ,upload.getmCatergory(),productId,upload.getmCatergoryId());
                                   FirebaseDatabase.getInstance().getReference(upload.getmCatergory())
                                           .child(upload.getmCatergoryId()).setValue(newUpload);
                                   FirebaseDatabase.getInstance().getReference("Uploads").child(productId).setValue(newUpload);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }
                );
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