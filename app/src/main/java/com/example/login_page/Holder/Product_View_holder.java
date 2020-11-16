package com.example.login_page.Holder;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.login_page.Interface.ItemClickListner;
import com.example.login_page.R;


public class Product_View_holder extends RecyclerView.ViewHolder implements View.OnClickListener
{
    public TextView txtProductName,txtProductPrice,txtProductCategory,txtProductQuantity;
    public ImageView imageView;
    public ItemClickListner listner;

    public Product_View_holder(View itemView)
    {
        super(itemView);
        txtProductName=(TextView)itemView.findViewById(R.id.item_name);
        imageView=(ImageView)itemView.findViewById(R.id.item_image);
        txtProductCategory=(TextView)itemView.findViewById(R.id.item_Catergory);
        txtProductPrice=(TextView)itemView.findViewById(R.id.item_Price);

        txtProductQuantity=(TextView)itemView.findViewById(R.id.item_quantity);


    }

    public void setItemClickListner(ItemClickListner listner)
    {
        this.listner=listner;
    }

    @Override
    public void onClick(View v) {
        listner.onClick(v,getAdapterPosition(),false);
    }
}