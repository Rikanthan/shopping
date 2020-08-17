package com.example.login_page;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class customAdapter extends BaseAdapter {
    private Integer[] items=
            {R.drawable.vegetables,R.drawable.fruit,R.drawable.medical,R.drawable.paper,R.drawable.hygiene,R.drawable.cosmetrics,R.drawable.softdrinks,R.drawable.snacks,R.drawable.bakery,R.drawable.others};

    private String[] imageLabels;
    private LayoutInflater thisInflater;
    public customAdapter(Context con,String[] labs)
    {
        this.thisInflater=LayoutInflater.from(con);
        this.imageLabels=labs;
    }
    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
     if(convertView==null) {
         convertView = thisInflater.inflate(R.layout.grid_item, parent, false);

         TextView textHeading = (TextView) convertView.findViewById(R.id.newheader);
         ImageView thumbnailImage = (ImageView) convertView.findViewById(R.id.clipart);

         textHeading.setText(imageLabels[i]);
         thumbnailImage.setImageResource(items[i]);

     }
        return convertView;
    }
}
