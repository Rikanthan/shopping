package com.example.login_page.Images;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.login_page.R;
import com.example.login_page.Views.PhoneDetails;

import java.util.List;


public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
    private final Context mContext;
    private final List<PhoneDetails> mPhoneDetailss;
    private static OnItemClickListener mListener;
    public ImageAdapter(Context context, List<PhoneDetails> uploads) {
        mContext = context;
        mPhoneDetailss = uploads;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        PhoneDetails uploadCurrent = mPhoneDetailss.get(position);
            holder.phone.setText(uploadCurrent.getPhone());
            Glide.with(mContext)
                    .load(uploadCurrent.getImageUri())
                    .placeholder(R.mipmap.loading)
                    .into(holder.imageView);
        holder.description.setText("Description: "+uploadCurrent.getDescription());
        holder.price.setText("Phone price: "+uploadCurrent.getPrice()+" Rs");
        holder.battery.setText("Battery: "+uploadCurrent.getBattery());
        holder.camera.setText("Camera: "+uploadCurrent.getCamera());
        holder.ram.setText("Ram: "+uploadCurrent.getRam());
        holder.storage.setText("Storage: "+uploadCurrent.getStorage());
        holder.fingerPrint.setText("FingerPrint: "+uploadCurrent.getFingerPrint());
        holder.connection.setText("Network: "+uploadCurrent.getConnection());

    }
    @Override
    public int getItemCount() {
        return mPhoneDetailss.size();
    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView phone, description, price, battery,camera,ram,storage,fingerPrint,connection;
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
            phone = itemView.findViewById(R.id.phone_text);
            camera = itemView.findViewById(R.id.camera_text);
            battery = itemView.findViewById(R.id.battery_text);
            ram = itemView.findViewById(R.id.ram_text);
            storage = itemView.findViewById(R.id.storage_text);
            description = itemView.findViewById(R.id.description_text);
            fingerPrint = itemView.findViewById(R.id.fingerprint_text);
            connection = itemView.findViewById(R.id.connection_text);
            price = itemView.findViewById(R.id.price_text);
            imageView = itemView.findViewById(R.id.image_view_upload);
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