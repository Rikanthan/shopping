package com.example.login_page.Views;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.login_page.Holder.CartViewHolder;
import com.example.login_page.Product.Cart;
import com.example.login_page.R;
import com.example.login_page.customer.get_booking;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
public class ShowOrders extends AppCompatActivity implements  CartViewHolder.OnItemClickListener {
RecyclerView recyclerView;
DatabaseReference databaseReference;
LinearLayoutManager linearLayoutManager;
FirebaseAuth firebaseAuth;
String fuser;
String customer;
Button deletebutton;
CartViewHolder mAdapter;
List<Cart> newcartlist;
String del="";
int pos = 0;
Long totalPrice = Long.valueOf(0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showorders);
        recyclerView=findViewById(R.id.show_cart);
        recyclerView.setHasFixedSize(true);
        deletebutton=findViewById(R.id.idelete);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        newcartlist=new ArrayList<>();
        firebaseAuth=FirebaseAuth.getInstance();
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
        Toast.makeText(ShowOrders.this,"Total price is :" +totalPrice.toString()+ "Rs", Toast.LENGTH_SHORT).show();
        Intent i=new Intent(ShowOrders.this, get_booking.class);
        i.putExtra("total",totalPrice.toString());
        startActivity(i);
    }

}