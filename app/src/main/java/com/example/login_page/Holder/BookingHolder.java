package com.example.login_page.Holder;
import android.content.Context;
import android.graphics.Color;
import android.graphics.fonts.FontStyle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.login_page.Interface.ItemClickListner;

import com.example.login_page.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;


public class BookingHolder extends RecyclerView.Adapter<BookingHolder.ImageViewHolder> {
    private final Context mContext;
    private final List<Bookings> mbookings;
    private static OnItemClickListener mListener;
    public BookingHolder(Context context, List<Bookings> bookings) {
        mContext = context;
        mbookings = bookings;
    }
    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_bookings, parent, false);
        return new ImageViewHolder(v);
    }



    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Bookings BookingsCurrent = mbookings.get(position);
        Spannable wordtoSpan = new SpannableString("Order#"+position);
        int length = wordtoSpan.length();
        wordtoSpan.setSpan(new ForegroundColorSpan(Color.YELLOW), 5, length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        holder.orderId.setText(wordtoSpan);
        holder.customername.setText(BookingsCurrent.getName());
        holder.customerlocation.setText(BookingsCurrent.getLocation());
        holder.customerphone.setText(BookingsCurrent.getPhone());
        holder.totalprice.setText("Rs "+BookingsCurrent.getPrice() + ".00");
        holder.customerpickuptime.setText(BookingsCurrent.getDate());

    }
    @Override
    public int getItemCount() {
        return mbookings.size();
    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView customername,customerlocation,customerphone,totalprice,customerpickuptime,orderId;

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
            customername = itemView.findViewById(R.id.customer_name);
            customerlocation = itemView.findViewById(R.id.customer_location);
            customerphone = itemView.findViewById(R.id.customer_phone);
            totalprice = itemView.findViewById(R.id.total_price);
            customerpickuptime = itemView.findViewById(R.id.customer_pickup_time);
            orderId = itemView.findViewById(R.id.order_id);
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

