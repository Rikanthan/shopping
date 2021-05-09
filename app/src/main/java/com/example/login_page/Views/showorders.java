package com.example.login_page.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.login_page.Holder.CartViewHolder;
import com.example.login_page.Images.ImageAdapter;
import com.example.login_page.Images.ImagesActivity;
import com.example.login_page.Product.Cart;
import com.example.login_page.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class showorders extends AppCompatActivity implements  CartViewHolder.OnItemClickListener {
RecyclerView recyclerView;
DatabaseReference databaseReference;
LinearLayoutManager linearLayoutManager;
FirebaseAuth firebaseAuth;
String fuser;
CartViewHolder mAdapter;
List<Cart> newcartlist;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showorders);
        recyclerView=findViewById(R.id.show_cart);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        newcartlist=new ArrayList<>();
        firebaseAuth=FirebaseAuth.getInstance();
        fuser=FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference=FirebaseDatabase.getInstance().getReference("orders");

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Cart mycart=dataSnapshot.getValue(Cart.class);
                    String Name=mycart.getPname();
                    Long quantity=mycart.getQuantity();
                    Long price=mycart.getPrice();
                    mycart.setPname(Name);
                    mycart.setQuantity(quantity);
                    mycart.setPrice(price);
                    Cart showcart=new Cart(Name,quantity,price);
                    newcartlist.add(showcart);
                }
                mAdapter = new CartViewHolder(showorders.this, newcartlist);
                mAdapter.setOnItemClickListener(showorders.this);
              recyclerView.setAdapter(mAdapter);

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(showorders.this, error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public void onItemClick(int position) {
    }
}