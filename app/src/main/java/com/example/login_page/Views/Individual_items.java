package com.example.login_page.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import java.util.UUID;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.example.login_page.Images.Upload;
import com.example.login_page.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class Individual_items extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
ImageView imageView;
TextView textname,textprice;
String productName,productCategory;
int index;
Context mContext;
String fuser;
ElegantNumberButton elegantNumberButton;
String showQuantity;
FloatingActionButton fab;
FirebaseAuth firebaseAuth;
Upload uploads;
int pPrice  =   0;
long id=0;
int quan=1;
long oid = 0;

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
        elegantNumberButton =   (ElegantNumberButton)findViewById(R.id.ele_button);
        getDetails(productCategory);
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
        DatabaseReference productsRef= FirebaseDatabase.getInstance().getReference(productCategory);
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
                                String categoryDescription = upload.getmCatergory();
                                String categoryPrice = upload.getmPrice();
                                String categoryImageUrl = upload.getImageUrl();
                                String quantity = upload.getmQuantity();
                                upload.setImageUrl(categoryImageUrl);
                                upload.setmCatergory(categoryDescription);
                                upload.setmPrice(categoryPrice);
                                upload.setName(Name);
                                upload.setmQuantity(quantity);
                                 uploads = new Upload(Name, categoryImageUrl, categoryPrice, quantity, categoryDescription);
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
                            System.out.println("DAta sNapshoT Not ExiSt");
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
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    oid=(snapshot.getChildrenCount());
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
        cartMap.put("pname",showQuantity);
        cartMap.put("price",pPrice);
        cartMap.put("quantity",quan);
        cartMap.put("date",saveCurrentdate);
        cartMap.put("time",saveCurrentTime);
        String uniqueID = UUID.randomUUID().toString();
        assert fuser != null;
        databaseReference.child(uniqueID).setValue(cartMap);
        int getIndex = index;
        String imageUrl = uploads.getImageUrl();
        String quantity = uploads.getmQuantity();
        String pName = uploads.getName();
        String price = uploads.getmPrice();
        int reducedQuantity = Integer.parseInt(quantity) - quan;
        Upload changeUploads =new Upload();
        changeUploads.setImageUrl(imageUrl);
        changeUploads.setmCatergory(productCategory);
        changeUploads.setmPrice(price);
        changeUploads.setName(pName);
        changeUploads.setmQuantity(String.valueOf(reducedQuantity));
//        final HashMap<String,Object> cartMap2=new HashMap<>();
//        cartMap2.put("imageUrl",imageUrl);
//        cartMap2.put("mCatergory",productCategory);
//        cartMap2.put("mPrice", price);
//        cartMap2.put("mQuantity", reducedQuantity);
//        cartMap2.put("name",pName);
//        System.out.println(index);
      changeReference.child(String.valueOf(getIndex+1)).setValue(changeUploads);
        Toast.makeText(this,"The Item added to cart successfully",Toast.LENGTH_LONG).show();

    }
    public void show_cart_items(View v)
    {
        Intent i=new Intent(this, ShowOrders.class);
        startActivity(i);
    }
}