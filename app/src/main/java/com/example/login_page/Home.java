package com.example.login_page;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

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
                        Intent intent=new Intent(getApplicationContext(),fruit.class);
                        startActivity(intent);
                    }
                    else if (i==2)
                    {
                        Intent intent=new Intent(getApplicationContext(),display_page.class);
                        startActivity(intent);
                    }
                    else
                    {
                        Intent intent=new Intent(getApplicationContext(),forgetpassword.class);
                        startActivity(intent);

                    }




            }
        });


    }
}