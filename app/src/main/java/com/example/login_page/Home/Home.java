package com.example.login_page.Home;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.login_page.Images.ImagesActivity;
import com.example.login_page.R;
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

public class Home extends AppCompatActivity {
    String[] lables;
    String[] fruitPages;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Resources res=getResources();
        lables=res.getStringArray(R.array.headers);
        fruitPages=res.getStringArray(R.array.fruits_page);
        final GridView grid=(GridView) findViewById(R.id.Items);
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


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.customer_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.customer_home:
                Intent intent = new Intent(this,Home.class);
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
}