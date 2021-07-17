package com.example.login_page.Admin.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.login_page.Images.Upload;
import com.example.login_page.Product.Product;
import com.example.login_page.R;
import com.example.login_page.Views.ShowOrders;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
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

public class EditItems extends AppCompatActivity {
    public static final String MY_PREFS_NAME = "MyPrefsFile";
    ImageView imageView;
    TextInputEditText editname,editprice,editquantity;
    String productName,productCategory,productImage,productPrice,productQuantity;
    int index;
    Context mContext;
    String fuser;
    String showQuantity;
    int catergoryIndex = 1;
    boolean findCatergory = false;
    DatabaseReference catergoryReference;
    String catergoryId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_items);
        index=getIntent().getIntExtra("index",index);
        System.out.println("index is:"+index);
        imageView=(ImageView)findViewById(R.id.edit_img);
        editname = (TextInputEditText) findViewById(R.id.edit_product_name);
        editprice = (TextInputEditText) findViewById(R.id.edit_product_price);
        editquantity = (TextInputEditText) findViewById(R.id.edit_product_quantity);
        getDetails();


    }
    private void getDetails()
    {
        DatabaseReference  productsRef = FirebaseDatabase.getInstance().getReference("Uploads");
        StorageReference storageReference= FirebaseStorage.getInstance().getReference("uploads");
        productsRef.child(String.valueOf(index+1)).addValueEventListener(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            Upload upload=snapshot.getValue(Upload.class);
                            if(upload != null) {
                                System.out.println(index);
                                String Name = upload.getName();
                                String categoryDescription = upload.getmCatergory();
                                String categoryPrice = upload.getmPrice();
                                String categoryImageUrl = upload.getImageUrl();
                                String quantity = upload.getmQuantity();
                                String uploadId = upload.getmuploadId();
                                catergoryId = upload.getmCatergoryId();
                                upload.setImageUrl(categoryImageUrl);
                                upload.setmCatergory(categoryDescription);
                                upload.setmPrice(categoryPrice);
                                upload.setName(Name);
                                upload.setmQuantity(quantity);
                                upload.setmuploadId(uploadId);                              
                                upload.setmCatergoryId(catergoryId);
                                Upload uploads = new Upload(Name, categoryImageUrl, categoryPrice, quantity, categoryDescription, uploadId ,catergoryId);
                                editname.setText(uploads.getName());
                                editprice.setText(uploads.getmPrice());
                                editquantity.setText(uploads.getmQuantity());
                                showQuantity= uploads.getName();
                                productImage = uploads.getImageUrl();
                                productCategory = uploads.getmCatergory();
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
    public void edititems(View v)
    {

        DatabaseReference databaseReference = FirebaseDatabase
                .getInstance()
                .getReference("Uploads")
                .child(String.valueOf(index+1));
        productPrice = editprice.getText().toString().trim();
        productName = editname.getText().toString().trim();
        productQuantity = editquantity.getText().toString().trim();
        final HashMap<String,Object> cartMap=new HashMap<>();
        cartMap.put("imageUrl",productImage);
        cartMap.put("mCatergory",productCategory);
        cartMap.put("mPrice",productPrice);
        cartMap.put("mQuantity",productQuantity);
        cartMap.put("name",productName);
        cartMap.put("mCatergoryId",catergoryId);
        cartMap.put("muploadId",String.valueOf(index+1));
        databaseReference.setValue(cartMap);
        catergoryReference = FirebaseDatabase.getInstance()
                .getReference(productCategory);

        System.out.println("Index is:"+catergoryIndex);
        catergoryReference.child(catergoryId).setValue(cartMap);

        Toast.makeText(EditItems.this,"Update successfully",Toast.LENGTH_SHORT).show();
    }
    public void show_cart_items(View v)
    {
        Intent i=new Intent(this, ShowOrders.class);
        startActivity(i);
    }
}