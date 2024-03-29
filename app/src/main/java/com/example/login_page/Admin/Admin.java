package com.example.login_page.Admin;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.login_page.Holder.Items;
import com.example.login_page.Holder.ProductViewHolder;
import com.example.login_page.Home.MainActivity;
import com.example.login_page.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class Admin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigation;
    private DatabaseReference productref;
    private String CategoryName, Description, Price, Pname, downloadImageUrl;
    private RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        productref= FirebaseDatabase.getInstance().getReference().child("Paper");
        mDrawer=(DrawerLayout)findViewById(R.id.drawer);
        navigation=(NavigationView)findViewById(R.id.navigationview);
        recyclerView=(RecyclerView)findViewById(R.id.recycler_menu);
        navigation.setNavigationItemSelectedListener(this);
        mToggle=new ActionBarDrawerToggle(this,mDrawer,R.string.open,R.string.close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
      recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if(mToggle.onOptionsItemSelected(item)){
           return true;
       }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerOptions<Items> options=new FirebaseRecyclerOptions.Builder<Items>().setQuery(productref,Items.class).build();

        FirebaseRecyclerAdapter<Items, ProductViewHolder> adapter=
                new FirebaseRecyclerAdapter<Items, ProductViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull ProductViewHolder holder, int position, @NonNull Items model) {
                        holder.txtProductName.setText(model.getPname());
                        Picasso.get().load(model.getPimageurl()).placeholder(R.mipmap.ic_launcher).into(holder.imageView);
                        holder.txtProductCategory.setText(model.getPcategory());
                        holder.txtProductPrice.setText(model.getPprice());
                        holder.txtProductQuantity.setText(model.getPquantity());
                    }

                    @NonNull
                    @Override
                    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                       View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.product_items_layout,parent ,false);
                        return new ProductViewHolder(view);
                    }
                };
      recyclerView.setAdapter(adapter);
        adapter.startListening();
    }



    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();

        switch (id)
        {
            case R.id.dashboard:
                Toast.makeText(getApplicationContext(),"dashboard",Toast.LENGTH_LONG).show();
            break;

            case R.id.search:
                Toast.makeText(getApplicationContext(),"search",Toast.LENGTH_LONG).show();
                break;

            case R.id.customer:
                Toast.makeText(getApplicationContext(),"customer",Toast.LENGTH_LONG).show();
                break;



            case R.id.inventory:

                Toast.makeText(getApplicationContext(),"customer",Toast.LENGTH_LONG).show();
                break;



            case R.id.product:
                Intent intent3=new Intent(getApplicationContext(), AdminProducts.class);
                startActivity(intent3);
                break;



            case R.id.logout:

                Intent intent=new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
                break;


        }
        mDrawer.closeDrawer(GravityCompat.START);

        return true;
    }

}