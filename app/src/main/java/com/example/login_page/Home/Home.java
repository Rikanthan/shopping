package com.example.login_page.Home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.login_page.Login_front.forgetpassword;
import com.example.login_page.R;
import com.example.login_page.Views.hygiene;
import com.example.login_page.adapter.customAdapter;
import com.example.login_page.Views.fruit;
import com.example.login_page.Views.softdrinks;

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
                        Intent intent=new Intent(getApplicationContext(), fruit.class);
                        startActivity(intent);
                    }
                    else if (i==4)
                    {
                        Intent intent=new Intent(getApplicationContext(), hygiene.class);
                        startActivity(intent);
                    }
                    else if (i==6)
                    {
                        Intent intent=new Intent(getApplicationContext(), softdrinks.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent=new Intent(getApplicationContext(), forgetpassword.class);
                        startActivity(intent);

                    }


            }
        });


    }
}