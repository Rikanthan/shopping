package com.example.login_page.Holder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
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
        Glide.with(mContext)
                .load(CartCurrent.getImageUrl())
                .placeholder(R.mipmap.loading)
                .into(holder.imageView);
        holder.textViewName.setText(CartCurrent.getPname());
        holder.textViewPrice.setText(CartCurrent.getPrice().toString()+" X "+CartCurrent.getQuantity().toString() +" Rs");
        holder.textViewQuantity.setText("Rs "+CartCurrent.getQuantity().toString() + " .00");
        long total = CartCurrent.getPrice() * CartCurrent.getQuantity();
        holder.textViewTotal.setText("Rs "+String.valueOf(total)+" .00");
    }
    @Override
    public int getItemCount() {
        return mCarts.size();
    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewName,textViewPrice,textViewQuantity, textViewTotal;
        public ImageView imageView;
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
            imageView = itemView.findViewById(R.id.cart_product_image);
            textViewName = itemView.findViewById(R.id.cart_product_name);
            textViewPrice=itemView.findViewById(R.id.cart_product_price);
            textViewQuantity=itemView.findViewById(R.id.cart_product_quantity);
            textViewTotal = itemView.findViewById(R.id.cart_total);
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

