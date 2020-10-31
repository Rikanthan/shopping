package com.example.login_page.adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.login_page.R;

public class snacks_Adapter extends BaseAdapter {

    // private Integer[] softdrink_items={R.drawable.images,R.drawable.cocacola_mega,R.drawable.sprite_mega,R.drawable.sprite_buddy,R.drawable.pepsi_mega,R.drawable.pepsi_buddy,R.drawable.sevenup_mega,R.drawable.sevenup_buddy,R.drawable.fanta_buddy,R.drawable.fanta_mega};
    private Integer[] snack_items={R.drawable.cheesebiscuites,
            R.drawable.chocolatepuff,
            R.drawable.creamcracker,
            R.drawable.lemonpuff,
            R.drawable.mareebiscuits,
            R.drawable.milk,
            R.drawable.nice,
            R.drawable.onion,
            R.drawable.tipitip,
            R.drawable.wafers};
    private String[] snack_lables;
    private String[] snack_Prices;
    private LayoutInflater thisInflater;

    public snacks_Adapter(Context con,String[] labs,String[] price)
    {
        this.thisInflater=LayoutInflater.from(con);
        this.snack_lables=labs;
        this.snack_Prices=price;
    }
    @Override
    public int getCount() {
        return snack_items.length;
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
            convertView = thisInflater.inflate(R.layout.snacks_grid, parent, false);

            TextView textHeading = (TextView) convertView.findViewById(R.id.snack_text);
            ImageView thumbnailImage = (ImageView) convertView.findViewById(R.id.snackimage);
            TextView textPrice=(TextView)convertView.findViewById(R.id.snack_price);

            textHeading.setText(snack_lables[i]);
            thumbnailImage.setImageResource(snack_items[i]);
            textPrice.setText(snack_Prices[i]);

        }
        return convertView;
    }
}

