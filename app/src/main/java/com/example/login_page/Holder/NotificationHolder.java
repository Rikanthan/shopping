package com.example.login_page.Holder;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.example.login_page.R;
import com.example.login_page.notification.Data;
import java.util.List;

public class NotificationHolder extends RecyclerView.Adapter<NotificationHolder.ImageViewHolder> {
    private final Context mContext;
    private final List<Data> mCarts;
    private static NotificationHolder.OnItemClickListener mListener;
    public NotificationHolder(Context context, List<Data> Carts) {
        mContext = context;
        mCarts = Carts;
    }
    @NonNull
    @Override
    public NotificationHolder.ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.view_notifications, parent, false);
        return new NotificationHolder.ImageViewHolder(v);
    }



    @Override
    public void onBindViewHolder(NotificationHolder.ImageViewHolder holder, int position) {
        Data CartCurrent = mCarts.get(position);
        holder.title.setText(CartCurrent.getTitle());
        holder.message.setText(CartCurrent.getMessage());
        holder.date.setText(CartCurrent.getDate());
        if(CartCurrent.getStatus().contains("Read"))
        {
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }

    }
    @Override
    public int getItemCount() {
        return mCarts.size();
    }
    public static class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView title,message,date;
        public CardView cardView;
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
            cardView = itemView.findViewById(R.id.notification_card);
            title = itemView.findViewById(R.id.notification_title);
            message = itemView.findViewById(R.id.notification_message);
            date = itemView.findViewById(R.id.notification_date);
            itemView.setOnClickListener(this);
        }

    }
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(NotificationHolder.OnItemClickListener listener)
    {
        mListener= listener;
    }

}
