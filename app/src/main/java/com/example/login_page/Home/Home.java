package com.example.login_page.Home;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.login_page.Holder.Bookings;
import com.example.login_page.Images.ImageAdapter;
import com.example.login_page.R;
import com.example.login_page.Views.IndividualItems;
import com.example.login_page.Views.PhoneDetails;
import com.example.login_page.Views.SeeTimer;
import com.example.login_page.Views.ShowNotifications;
import com.example.login_page.Views.ShowOrders;
import com.example.login_page.adapter.customAdapter;
import com.example.login_page.category.AllCatergories;
import com.example.login_page.customer.CustomerViewBookings;
import com.example.login_page.notification.Data;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements ImageAdapter.OnItemClickListener {
    String[] lables;
    ImageAdapter mAdapter;
    TextView cartText,bookingText,notificationText;
    SearchView searchView;
    RecyclerView recyclerView;
    CustomerViewBookings _customer;
    ImageButton imageButton;
    GridView grid;
    List<PhoneDetails> mPhoneDetailss;
    boolean isListView = false;
    long cartCount = 0;
    long bookingCount = 0;
    long notifCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Resources res = getResources();
        lables = res.getStringArray(R.array.headers);
        searchView = findViewById(R.id.search_items);
        recyclerView = findViewById(R.id.recycler_view);
        imageButton = findViewById(R.id.show_all);
        recyclerView.setHasFixedSize(true);
        mPhoneDetailss = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        grid=(GridView) findViewById(R.id.Items);
        _customer = new CustomerViewBookings();
        customAdapter myadapter =   new customAdapter(getApplicationContext(),lables);
        grid.setAdapter(myadapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    Intent intent = new Intent(getApplicationContext(), AllCatergories.class);
                        intent.putExtra("Product",lables[i]);
                        startActivity(intent);
            }
        });
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        if(query != null )
                        {
                            if(!query.isEmpty())
                            {
                                mPhoneDetailss.clear();
                                search(query);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                        else
                        {
                            grid.setVisibility(View.VISIBLE);
                        }
                        return true;
                    }
                    @Override
                    public boolean onQueryTextChange(String newText) {
                        recyclerView.setVisibility(View.VISIBLE);
                        if(newText != null )
                        {
                            if(!newText.isEmpty())
                            {
                                mPhoneDetailss.clear();
                                search(newText);

                            }
                            else
                            {
                                show();
                            }
                        }
                        else
                        {
                            show();
                        }
                        return true;
                    }
                }
        );
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_menu, menu);
        final View showCart = menu.findItem(R.id.show_cart).getActionView();
        cartText = (TextView) showCart.findViewById(R.id.update_cart);
        getCartItem();
        showCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Home.this, ShowOrders.class);
                startActivity(i);
            }
        });
        final View showBooking = menu.findItem(R.id.cust_bookings).getActionView();
        bookingText = (TextView) showBooking.findViewById(R.id.update_bookings_text);
        getBookingCount();
       showBooking.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent i1 = new Intent(Home.this, CustomerViewBookings.class);
               startActivity(i1);
           }
       });
       final View home = menu.findItem(R.id.customer_home).getActionView();
       menu.findItem(R.id.customer_home).setIcon(R.drawable.ic_baseline_timer_24);
       final View noti = menu.findItem(R.id.cust_notification).getActionView();
       notificationText = (TextView) noti.findViewById(R.id.update_notifications_text);
       getNotificationCount();
       noti.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(Home.this,ShowNotifications.class);
               startActivity(intent);
           }
       });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.customer_home:
                Intent intent = new Intent(this, SeeTimer.class);
                startActivity(intent);
                return true;
            case R.id.show_cart:
                Intent i = new Intent(this, ShowOrders.class);
                startActivity(i);
                return true;
            case R.id.cust_bookings:
                Intent i1 = new Intent(this, CustomerViewBookings.class);
                startActivity(i1);
                return true;
            case R.id.cust_notification:
                Intent i3 = new Intent(this, ShowNotifications.class);
                startActivity(i3);
                return true;
            case R.id.customer_logout:
                Intent i2 = new Intent(this,MainActivity.class);
                startActivity(i2);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void getCartItem()
    {
        String uid = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance()
                .getReference("orders")
                .child(uid).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            cartCount = snapshot.getChildrenCount();
                            cartText.setText(String.valueOf(cartCount));
                            cartText.setVisibility(View.VISIBLE);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );
    }
    public void getBookingCount()
    {
        final String uid = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance()
                .getReference("booking")
                .addValueEventListener(
                new ValueEventListener() {
                    @Override
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot:snapshot.getChildren())
                        {
                            if(dataSnapshot.exists())
                            {
                                Bookings getBooking = dataSnapshot.getValue(Bookings.class);
                                if(getBooking.getId().contains(uid))
                                {
                                    bookingCount++;
                                    bookingText.setText(String.valueOf(bookingCount));
                                    bookingText.setVisibility(View.VISIBLE);
                                }
                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                }
        );

    }
    public void getNotificationCount()
    {
        notifCount = 0;
        final String uid = FirebaseAuth.getInstance().getUid();
        FirebaseDatabase.getInstance()
                .getReference("Notification")
                .child(uid)
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            @RequiresApi(api = Build.VERSION_CODES.N)
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot dataSnapshot:snapshot.getChildren())
                                {
                                    if(dataSnapshot.exists())
                                    {
                                        Data data = dataSnapshot.getValue(Data.class);
                                        if(data.getStatus().contains("Unread") )
                                        {
                                            notifCount++;
                                            notificationText.setText(String.valueOf(notifCount));
                                            notificationText.setVisibility(View.VISIBLE);
                                        }
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }
                );

    }
    public void search(String s)
    {
        mPhoneDetailss.clear();
        s = s.substring(0,1).toUpperCase() + s.substring(1);
        FirebaseDatabase
                .getInstance()
                .getReference("PhoneDetailss")
                .orderByChild("name")
                .startAt(s)
                .endAt(s.toLowerCase()+"\uf8ff")
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot snapshot1: snapshot.getChildren())
                                    {
                                        if(snapshot1.exists())
                                        {
                                        PhoneDetails upload = snapshot1.getValue(PhoneDetails.class);
                                        mPhoneDetailss.add(upload);
                                        }
                                    mAdapter = new ImageAdapter(Home.this, mPhoneDetailss);
                                    mAdapter.setOnItemClickListener(Home.this);
                                    recyclerView.setAdapter(mAdapter);
                                    grid.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }
                );
    }
    public void showAll(View v)
    {
        mPhoneDetailss.clear();
        isListView = !isListView;
        if(isListView)
        {
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_view_list_24));
            recyclerView.setVisibility(View.VISIBLE);
        }
        else
        {
            imageButton.setImageDrawable(getResources().getDrawable(R.drawable.ic_baseline_view_module_24));
            Intent i = new Intent(this,Home.class);
            startActivity(i);
        }
        show();
    }
    @Override
    public void onItemClick(final int position) {
//        PhoneDetails upload = mPhoneDetailss.get(position);
//        Intent i=new Intent(this, IndividualItems.class);
//        int index = Integer.parseInt(upload.getmCatergoryId());
//        i.putExtra("Category",upload.getmCatergory());
//        i.putExtra("index",index-1);
//        startActivity(i);
    }
    public void show()
    {
        FirebaseDatabase
                .getInstance()
                .getReference("PhoneDetailss")
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                mPhoneDetailss.clear();
                                for(DataSnapshot snapshot1: snapshot.getChildren())
                                {
                                    if(snapshot1.exists())
                                    {
                                        PhoneDetails upload = snapshot1.getValue(PhoneDetails.class);
                                        mPhoneDetailss.add(upload);
                                    }
                                    mAdapter = new ImageAdapter(Home.this, mPhoneDetailss);
                                    mAdapter.setOnItemClickListener(Home.this);
                                    recyclerView.setAdapter(mAdapter);
                                    grid.setVisibility(View.GONE);
                                    recyclerView.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        }
                );
    }
}