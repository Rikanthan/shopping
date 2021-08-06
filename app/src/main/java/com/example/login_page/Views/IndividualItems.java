package com.example.login_page.Views;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Date;
import java.util.UUID;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.login_page.Images.Upload;
import com.example.login_page.R;
import com.example.login_page.notification.APIService;
import com.example.login_page.notification.Client;
import com.example.login_page.notification.Data;
import com.example.login_page.notification.MyResponse;
import com.example.login_page.notification.NotificationSender;
import com.example.login_page.notification.Token;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IndividualItems extends AppCompatActivity {
    private static final String CHANNEL_ID = "100 " ;
    private APIService apiService;
    ImageView imageView;
    TextView textname,textprice,updateCount;

    String productCategory;
    int index;
    String fuser;
    String uploadId = "";
    String catergoryId = "";
    ElegantNumberButton elegantNumberButton;
    String showQuantity;
    FloatingActionButton fab;
    int itemIndex;
    FirebaseAuth firebaseAuth;
    DatabaseReference adminReference;
    Upload uploads;
    int pPrice  =   0;
    int quan=1;
    String catergoryImageUrl;
    String adminId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_items);
        index   =   getIntent().getIntExtra("index",index);
        System.out.println("index is:"+index);
        productCategory =   getIntent().getStringExtra("Category");
        imageView   =   (ImageView)findViewById(R.id.indi_img);
        textname    =   (TextView)findViewById(R.id.indi_name);
        textprice   =   (TextView)findViewById(R.id.indi_price);
        fab =   (FloatingActionButton)findViewById(R.id.cartfab);
        updateCount = (TextView) findViewById(R.id.update_cart_count);
        apiService = Client.getClient("https://fcm.googleapis.com/").create(APIService.class);
        elegantNumberButton =   (ElegantNumberButton)findViewById(R.id.ele_button);
        adminId = "4VUgoUAvIgSNWgPFVCEYaFh1Mfd2";
        createNotificationChannel();
        getDetails(productCategory);
        setCount();
        firebaseAuth=FirebaseAuth.getInstance();
        uploads = new Upload();
        fuser = FirebaseAuth.getInstance().getCurrentUser().getUid();
        elegantNumberButton.setOnClickListener(new ElegantNumberButton.OnClickListener() {
            @Override
            public void onClick(View view) {
                quan=Integer.parseInt(elegantNumberButton.getNumber());
                textprice.setText(String.valueOf(pPrice*quan)+" Rs");
                textname.setText(showQuantity+" X "+quan);

            }
        });
    }
    private void getDetails(final String productCategory)
    {
        DatabaseReference productsRef   = FirebaseDatabase.getInstance().getReference(productCategory);
        productsRef.child(String.valueOf(index+1)).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            Upload upload=snapshot.getValue(Upload.class);
                            if(upload != null) {
                                System.out.println(productCategory);
                                System.out.println(index);
                                String Name = upload.getName();
                                 uploadId = upload.getmuploadId();
                                String categoryDescription = upload.getmCatergory();
                                String categoryPrice = upload.getmPrice();
                                catergoryImageUrl = upload.getImageUrl();
                                String quantity = upload.getmQuantity();
                                 catergoryId = upload.getmCatergoryId();
                                 uploads = new Upload(Name, catergoryImageUrl, categoryPrice, quantity, categoryDescription ,uploadId ,catergoryId);
                                textname.setText(uploads.getName());
                                showQuantity= uploads.getName();
                                pPrice=Integer.parseInt(uploads.getmPrice());
                                int newPrice=Integer.parseInt(uploads.getmPrice())*quan;
                                textprice.setText(newPrice+" Rs");
                                Picasso.get().load(uploads.getImageUrl()).placeholder(R.mipmap.loading).into(imageView);
                            }
                        }
                        else
                        {
                            System.out.println("Data snap doesn't exists");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

    }

    public void add_to_cart(View v)
    {
        itemIndex = 1;
        DatabaseReference databaseReference =   FirebaseDatabase
                                                    .getInstance()
                                                        .getReference("orders")
                                                            .child(fuser);
        DatabaseReference changeReference = FirebaseDatabase
                                                        .getInstance()
                                                        .getReference(productCategory);
        String saveCurrentTime,saveCurrentdate;
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd MMM, yyyy");
        saveCurrentdate=currentDate.format(calendar.getTime());
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());
        final HashMap<String,Object> cartMap=new HashMap<>();

        cartMap.put("pname",showQuantity);
        cartMap.put("imageUrl",catergoryImageUrl);
        cartMap.put("price",pPrice);
        cartMap.put("quantity",quan);
        cartMap.put("date",saveCurrentdate);
        cartMap.put("time",saveCurrentTime);
        String uniqueID = UUID.randomUUID().toString();
        assert fuser != null;

        int getIndex = index;
        final String imageUrl = uploads.getImageUrl();
        String quantity = uploads.getmQuantity();
        final String pName = uploads.getName();
        String price = uploads.getmPrice();
        int reducedQuantity = Integer.parseInt(quantity) - quan;
        Upload changeUploads =new Upload();
        changeUploads.setImageUrl(imageUrl);
        changeUploads.setmCatergory(productCategory);
        changeUploads.setmPrice(price);
        changeUploads.setName(pName);
        if(reducedQuantity < 0)
        {
            changeUploads.setmQuantity("0");
        }
        else
        {
            changeUploads.setmQuantity(String.valueOf(reducedQuantity));
        }
        changeUploads.setmCatergoryId(catergoryId);
        changeUploads.setmuploadId(uploadId);
        if(reducedQuantity < 5)
        {
            FirebaseDatabase.getInstance().getReference().child("Tokens").child(adminId)
                    .child("token").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String usertoken = dataSnapshot.getValue(String.class);
                    sendNotifications(usertoken, "Items alert!", pName+" counts getting low");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            UpdateToken();
        }
        adminReference = FirebaseDatabase.getInstance().getReference("Uploads");
        System.out.println(uploadId);
        databaseReference.child(uniqueID).setValue(cartMap);
        changeReference.child(String.valueOf(getIndex+1)).setValue(changeUploads);
        adminReference.child(uploadId).setValue(changeUploads);
        Toast.makeText(this,"The Item added to cart successfully",Toast.LENGTH_LONG).show();
        setCount();

    }
    public void show_cart_items(View v)
    {
        Intent i=new Intent(this, ShowOrders.class);
        startActivity(i);
    }
    public void UpdateToken(){
        FirebaseUser firebaseUser= FirebaseAuth.getInstance().getCurrentUser();
        String refreshToken= FirebaseInstanceId.getInstance().getToken();
        Token token= new Token(refreshToken);
        FirebaseDatabase.getInstance().getReference("Tokens")
                .child(FirebaseAuth
                        .getInstance()
                        .getCurrentUser()
                        .getUid()
                ).setValue(token);
    }

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
                        Toast.makeText(IndividualItems.this, "Failed ", Toast.LENGTH_LONG);
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
    public void setCount()
    {
        String id = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance().getReference("orders").child(id).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            long count = snapshot.getChildrenCount();
                            if(count > 0)
                            {
                                updateCount.setText(String.valueOf(count));
                                updateCount.setVisibility(View.VISIBLE);
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