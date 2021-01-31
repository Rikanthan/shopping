package com.example.login_page.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.login_page.Images.Upload;
import com.example.login_page.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class Individual_items extends AppCompatActivity {
ImageView imageView;
TextView textname,textprice;
String productName,productCategory;
int index;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_items);
        //productName=getIntent().getStringExtra("name");
        index=getIntent().getIntExtra("index",index);
        System.out.println("index is:"+index);
        productCategory=getIntent().getStringExtra("Category");
        imageView=(ImageView)findViewById(R.id.indi_img);
        textname=(TextView)findViewById(R.id.indi_name);
        textprice=(TextView)findViewById(R.id.indi_price);
        getDetails(productCategory);
    }
    private void getDetails(final String productCategory)
    {
        DatabaseReference productsRef= FirebaseDatabase.getInstance().getReference(productCategory);
        StorageReference storageReference= FirebaseStorage.getInstance().getReference("uploads");
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
                                Upload uploads = new Upload(Name, categoryImageUrl, categoryPrice, quantity, categoryDescription);
                                textname.setText(uploads.getName());
                                textprice.setText(uploads.getmPrice());
                                Picasso.get().load(uploads.getImageUrl()).into(imageView);
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
}