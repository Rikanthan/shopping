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
import com.example.login_page.Holder.BookingHolder;
import com.example.login_page.Holder.Bookings;
import com.example.login_page.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ShowBookings extends AppCompatActivity implements  BookingHolder.OnItemClickListener {
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    LinearLayoutManager linearLayoutManager;
    FirebaseAuth firebaseAuth;
    String fuser;
    Button deletebutton;
    BookingHolder mAdapter;
    List<Bookings> newcartlist;
    String del="";
    int pos=0;
    Long totalPrice = Long.valueOf(0);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_bookings);
        recyclerView=findViewById(R.id.show_bookings);
        recyclerView.setHasFixedSize(true);
        linearLayoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        newcartlist=new ArrayList<>();
        firebaseAuth=FirebaseAuth.getInstance();
        fuser=FirebaseAuth.getInstance().getCurrentUser().getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference("bookings");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                {
                    Bookings mycart = dataSnapshot.getValue(Bookings.class);
                    String name=mycart.getName();
                    String phone = mycart.getPhone();
                    String location = mycart.getLocation();
                    String price = mycart.getPrice();
                    String date = mycart.getDate();
                    String userId = mycart.getUserId();
                    mycart.setDate(date);
                    mycart.setPhone(phone);
                    mycart.setLocation(location);
                    mycart.setUserId(userId);
                    mycart.setName(name);
                    mycart.setPrice(price);
                    Bookings showcart=new Bookings(userId,price,name,phone,location,date);
                    newcartlist.add(showcart);
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
    @Override
    public void onItemClick(int position)
    {


    }

//    public void delete(View view){
//        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
//        alertDialogBuilder.setMessage("Are you sure , You wanted to remove the item from cart");
//        alertDialogBuilder.setPositiveButton("yes",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface arg0, int arg1) {
////                                Toast.makeText(showorders.this,"You clicked yes button",
////                                        Toast.LENGTH_LONG).show();
//                        databaseReference.child(String.valueOf(pos)).addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                for (DataSnapshot appleSnapshot: snapshot.getChildren()) {
//                                    appleSnapshot.getRef().removeValue();
//                                }
//                            }
//
//                            @Override
//                            public void onCancelled(@NonNull DatabaseError error) {
//                                Toast.makeText(showorders.this, error.getMessage(), Toast.LENGTH_SHORT).show();
//                            }
//                        });
//                    }
//                });
//
//        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                finish();
//            }
//        });
//
//        AlertDialog alertDialog = alertDialogBuilder.create();
//        alertDialog.show();
//    }

//    public void confirm(View v)
//    {
//        Toast.makeText(showorders.this,"Total price is :" +totalPrice.toString()+ "Rs", Toast.LENGTH_SHORT).show();
//        Intent i=new Intent(showorders.this, get_booking.class);
//        i.putExtra("total",totalPrice.toString());
//        startActivity(i);
//    }

}