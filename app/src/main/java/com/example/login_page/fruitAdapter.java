package com.example.login_page;

import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

        import android.widget.BaseAdapter;
        import android.widget.ImageView;
        import android.widget.TextView;

public class fruitAdapter extends BaseAdapter {

    private Integer[] fruit_items={R.drawable.mango,R.drawable.apple,R.drawable.orange,R.drawable.grapes,R.drawable.strawberry,R.drawable.pineapple,R.drawable.banana};
    private String[] fruitLabels;
    private LayoutInflater thisInflater;

    public fruitAdapter(Context con,String[] labs)
    {
        this.thisInflater=LayoutInflater.from(con);
        this.fruitLabels=labs;
    }
    @Override
    public int getCount() {
        return fruit_items.length;
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
            convertView = thisInflater.inflate(R.layout.fruit_grid, parent, false);

            TextView textHeading = (TextView) convertView.findViewById(R.id.fruittext);
            ImageView thumbnailImage = (ImageView) convertView.findViewById(R.id.fruitimage);

            textHeading.setText(fruitLabels[i]);
            thumbnailImage.setImageResource(fruit_items[i]);

        }
        return convertView;
    }
}
