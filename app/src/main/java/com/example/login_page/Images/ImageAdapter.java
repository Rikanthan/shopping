package com.example.login_page.Images;
import android.content.Context;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login_page.Admin.admin;
import com.example.login_page.R;
import com.example.login_page.Views.Individual_items;
import com.squareup.picasso.Picasso;
import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private final Context mContext;
    private final List<Upload> mUploads;
    private static OnItemClickListener mListener;
    public ImageAdapter(Context context, List<Upload> uploads) {
        mContext = context;
        mUploads = uploads;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Upload uploadCurrent = mUploads.get(position);
            holder.textViewName.setText(uploadCurrent.getName());
            Glide.with(mContext)
                    .load(uploadCurrent.getImageUrl())
                    .placeholder(R.mipmap.loading)
                    .into(holder.imageView);
        holder.textViewCatergory.setText(uploadCurrent.getmCatergory());
        holder.textViewPrice.setText(uploadCurrent.getmPrice()+" Rs");
        holder.textViewQuantity.setText(uploadCurrent.getmQuantity());

    }
    @Override
    public int getItemCount() {
        return mUploads.size();
    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView textViewName,textViewCatergory,textViewPrice,textViewQuantity;
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
            textViewName = itemView.findViewById(R.id.text_view_name);
            imageView = itemView.findViewById(R.id.image_view_upload);
            textViewCatergory=itemView.findViewById(R.id.text_view_catergory);
            textViewPrice=itemView.findViewById(R.id.text_view_price);
            textViewQuantity=itemView.findViewById(R.id.text_view_quantity);
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