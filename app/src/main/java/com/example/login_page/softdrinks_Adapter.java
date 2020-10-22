package com.example.login_page;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class softdrinks_Adapter extends BaseAdapter {

   // private Integer[] softdrink_items={R.drawable.images,R.drawable.cocacola_mega,R.drawable.sprite_mega,R.drawable.sprite_buddy,R.drawable.pepsi_mega,R.drawable.pepsi_buddy,R.drawable.sevenup_mega,R.drawable.sevenup_buddy,R.drawable.fanta_buddy,R.drawable.fanta_mega};
    private Integer[] softdrink_items={R.drawable.bathroomcleaner,R.drawable.detergent,R.drawable.facewash,R.drawable.soap,R.drawable.toothbrush,R.drawable.toothpaste};
    private String[] softdrink_lables;
    private String[] softdrink_Prices;
    private LayoutInflater thisInflater;

    public softdrinks_Adapter(Context con,String[] labs,String[] price)
    {
        this.thisInflater=LayoutInflater.from(con);
        this.softdrink_lables=labs;
        this.softdrink_Prices=price;
    }
    @Override
    public int getCount() {
        return softdrink_items.length;
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
            convertView = thisInflater.inflate(R.layout.softdrinks_grid, parent, false);

            TextView textHeading = (TextView) convertView.findViewById(R.id.soft_text);
            ImageView thumbnailImage = (ImageView) convertView.findViewById(R.id.softdrinkimage);
            TextView textPrice=(TextView)convertView.findViewById(R.id.soft_price);

            textHeading.setText(softdrink_lables[i]);
            thumbnailImage.setImageResource(softdrink_items[i]);
            textPrice.setText(softdrink_Prices[i]);

        }
        return convertView;
    }
}

