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
import android.text.Layout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.login_page.Holder.Bookings;
import com.example.login_page.Images.ImageAdapter;
import com.example.login_page.Images.ImagesActivity;
import com.example.login_page.Images.Upload;
import com.example.login_page.Product.ShowItemsActivity;
import com.example.login_page.R;
import com.example.login_page.Views.SeeTimer;
import com.example.login_page.Views.ShowOrders;
import com.example.login_page.adapter.customAdapter;
import com.example.login_page.category.Bakery;
import com.example.login_page.category.Cosmetrics;
import com.example.login_page.category.Hygiene;
import com.example.login_page.category.Medical;
import com.example.login_page.category.Paper;
import com.example.login_page.category.Snacks;
import com.example.login_page.category.Softdrinks;
import com.example.login_page.category.Vegetables;
import com.example.login_page.customer.CustomerViewBookings;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Home extends AppCompatActivity implements ImageAdapter.OnItemClickListener {
    String[] lables;
    String[] fruitPages;
    Layout updateCart;
    ImageAdapter mAdapter;
    TextView cartText,bookingText;
    SearchView searchView;
    RecyclerView recyclerView;
    CustomerViewBookings _customer;
    GridView grid;
    List<Upload> mUploads;

    long cartCount = 0;
    long bookingCount = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Resources res=getResources();
        lables=res.getStringArray(R.array.headers);
        fruitPages=res.getStringArray(R.array.fruits_page);
        searchView = findViewById(R.id.search_items);
        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        mUploads = new ArrayList<>();
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        grid=(GridView) findViewById(R.id.Items);
        _customer = new CustomerViewBookings();
        customAdapter myadapter=new customAdapter(getApplicationContext(),lables);
        grid.setAdapter(myadapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //intent.putExtra("newpage",fruitPages[i]);
                     long viewId = view.getId();

                    if (i==1)
                    {
                        Intent intent=new Intent(getApplicationContext(),ImagesActivity.class);
                        startActivity(intent);
                    }
                    else if (i==2)
                    {
                        Intent intent=new Intent(getApplicationContext(), Medical.class);
                        startActivity(intent);
                    }
                    else if (i==4)
                    {
                        Intent intent=new Intent(getApplicationContext(), Hygiene.class);
                        startActivity(intent);
                    }
                    else if (i==0)
                    {
                        Intent intent=new Intent(getApplicationContext(), Vegetables.class);
                        startActivity(intent);
                    }
                    else if (i==5)
                    {
                        Intent intent=new Intent(getApplicationContext(), Cosmetrics.class);
                        startActivity(intent);
                    }
                    else if (i==6)
                    {
                        Intent intent=new Intent(getApplicationContext(), Softdrinks.class);
                        startActivity(intent);
                    }
                    else if (i==7)
                    {
                        Intent intent=new Intent(getApplicationContext(), Snacks.class);
                        startActivity(intent);
                    }
                    else if(i==3)
                    {
                        Intent intent=new Intent(getApplicationContext(), Paper.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent=new Intent(getApplicationContext(), Bakery.class);
                        startActivity(intent);
                    }
            }
        });

        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        grid.setVisibility(View.GONE);

                        if(query != null )
                        {
                            if(!query.isEmpty())
                            {
                                mUploads.clear();
                                search(query);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        grid.setVisibility(View.GONE);

                        if(newText != null )
                        {
                            if(!newText.isEmpty())
                            {
                                mUploads.clear();
                                search(newText);
                                recyclerView.setVisibility(View.VISIBLE);
                            }
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
    public void search(String s)
    {
        FirebaseDatabase
                .getInstance()
                .getReference("Uploads")
                .orderByChild("name")
                .startAt(s)
                .endAt(s+"\uf8ff")
                .addValueEventListener(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists())
                                {
                                    for(DataSnapshot snapshot1: snapshot.getChildren())
                                    {
                                        Upload upload = snapshot1.getValue(Upload.class);
                                        String Name = upload.getName();
                                        String categoryDescription = upload.getmCatergory();
                                        String categoryPrice = upload.getmPrice();
                                        String categoryImageUrl = upload.getImageUrl();
                                        String quantity=upload.getmQuantity();
                                        String uploadId = upload.getmuploadId();
                                        String catergoryId = upload.getmCatergoryId();
                                        upload.setmuploadId(uploadId);
                                        upload.setmCatergoryId(catergoryId);
                                        upload.setImageUrl(categoryImageUrl);
                                        upload.setmCatergory(categoryDescription);
                                        upload.setmPrice(categoryPrice);
                                        upload.setName(Name);
                                        upload.setmQuantity(quantity);
                                        Upload uploads=new Upload(Name,categoryImageUrl,categoryPrice,quantity,categoryDescription,uploadId ,catergoryId);
                                        mUploads.add(uploads);
                                    }
                                    mAdapter = new ImageAdapter(Home.this, mUploads);
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

    @Override
    public void onItemClick(int position) {

    }
}