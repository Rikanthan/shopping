package com.example.login_page.Holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login_page.Interface.ItemClickListner;
import com.example.login_page.Product.Cart;
import com.example.login_page.R;

import java.util.List;


public class CartViewHolder extends RecyclerView.Adapter<CartViewHolder.ImageViewHolder> {
    private final Context mContext;
    private final List<Cart> mCarts;
    private static OnItemClickListener mListener;
    public CartViewHolder(Context context, List<Cart> Carts) {
        mContext = context;
        mCarts = Carts;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.cart_items, parent, false);
        return new ImageViewHolder(v);
    }



    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Cart CartCurrent = mCarts.get(position);
        holder.textViewName.setText("   Product name :  "+CartCurrent.getPname());
        holder.textViewPrice.setText("   Product quantity :  "+CartCurrent.getPrice().toString());
        holder.textViewQuantity.setText("   Product price:  "+CartCurrent.getQuantity().toString() + " Rs");

    }
    @Override
    public int getItemCount() {
        return mCarts.size();
    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewName,textViewPrice,textViewQuantity;

        @Override
        public void onClick(View v) {
            if(mListener !=null)
            {
                int position=getAdapterPosition();
                if(position!= RecyclerView.NO_POSITION)
                {
                    mListener.onItemClick(position);
                }
            }
        }

        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.cart_product_name);
            textViewPrice=itemView.findViewById(R.id.cart_product_price);
            textViewQuantity=itemView.findViewById(R.id.cart_product_quantity);
            itemView.setOnClickListener(this);
        }

    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener)
    {
        mListener= listener;
    }

}
//public class cart_holder extends RecyclerView.ViewHolder implements View.OnClickListener {
//    public TextView txtCartProductName,txtCartProductQuantity,txtCartProductPrice;
//    private ItemClickListner itemClickListner;
//    public cart_holder(@NonNull View itemView) {
//        super(itemView);
//        txtCartProductName=itemView.findViewById(R.id.cart_product_name);
//        txtCartProductQuantity=itemView.findViewById(R.id.cart_product_quantity);
//        txtCartProductPrice=itemView.findViewById(R.id.cart_product_price);
//    }
//
//    @Override
//    public void onClick(View v) {
//        itemClickListner.onClick(v,getAdapterPosition(),false);
//    }
//
//    public void setItemClickListner(ItemClickListner itemClickListner) {
//        this.itemClickListner = itemClickListner;
//    }
//}
