package com.example.login_page;

import androidx.annotation.NonNull;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.navigation.NavigationView;

public class admin extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mDrawer;
    private ActionBarDrawerToggle mToggle;
    private NavigationView navigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mDrawer=(DrawerLayout)findViewById(R.id.drawer);
        navigation=(NavigationView)findViewById(R.id.navigationview);
        navigation.setNavigationItemSelectedListener(this);
        mToggle=new ActionBarDrawerToggle(this,mDrawer,R.string.open,R.string.close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
       if(mToggle.onOptionsItemSelected(item)){
           return true;
       }

        return super.onOptionsItemSelected(item);
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

            case R.id.salesmen:
                Toast.makeText(getApplicationContext(),"salesmen",Toast.LENGTH_LONG).show();
                break;

            case R.id.inventory:
                Toast.makeText(getApplicationContext(),"inventory",Toast.LENGTH_LONG).show();
                break;

            case R.id.product:
                Toast.makeText(getApplicationContext(),"product",Toast.LENGTH_LONG).show();
                break;

            case R.id.offer:
                Toast.makeText(getApplicationContext(),"offer",Toast.LENGTH_LONG).show();
                break;

            case R.id.setting:
                Toast.makeText(getApplicationContext(),"setting",Toast.LENGTH_LONG).show();
                break;

            case R.id.logout:
                Toast.makeText(getApplicationContext(),"logout",Toast.LENGTH_LONG).show();
                break;


        }
        mDrawer.closeDrawer(GravityCompat.START);

        return true;
    }
}