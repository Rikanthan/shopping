package com.example.login_page.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.login_page.Images.ImagesActivity;
import com.example.login_page.R;
import com.example.login_page.adapter.customAdapter;
import com.example.login_page.category.bakery;
import com.example.login_page.category.cosmetics_activity;
import com.example.login_page.category.hygiene_activity;
import com.example.login_page.category.medical_activity;
import com.example.login_page.category.paper;
import com.example.login_page.category.snacks_activity;
import com.example.login_page.category.softdrinks_activity;
import com.example.login_page.category.veg_activity_new;

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
                        Intent intent=new Intent(getApplicationContext(), medical_activity.class);
                        startActivity(intent);
                    }
                    else if (i==4)
                    {
                        Intent intent=new Intent(getApplicationContext(), hygiene_activity.class);
                        startActivity(intent);
                    }
                    else if (i==0)
                    {
                        Intent intent=new Intent(getApplicationContext(), veg_activity_new.class);
                        startActivity(intent);
                    }
                    else if (i==5)
                    {
                        Intent intent=new Intent(getApplicationContext(), cosmetics_activity.class);
                        startActivity(intent);
                    }
                    else if (i==6)
                    {
                        Intent intent=new Intent(getApplicationContext(), softdrinks_activity.class);
                        startActivity(intent);
                    }
                    else if (i==7)
                    {
                        Intent intent=new Intent(getApplicationContext(), snacks_activity.class);
                        startActivity(intent);
                    }
                    else if(i==3)
                    {
                        Intent intent=new Intent(getApplicationContext(), paper.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent=new Intent(getApplicationContext(), bakery.class);
                        startActivity(intent);
                    }
            }
        });


    }
}