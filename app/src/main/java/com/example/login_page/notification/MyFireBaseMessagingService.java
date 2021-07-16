package com.example.login_page.notification;

import android.app.NotificationManager;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.example.login_page.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFireBaseMessagingService extends FirebaseMessagingService {
    private static final String CHANNEL_ID = "100 " ;
    String title,message;
    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        title= "New booking";
        message= "My booking";

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(MyFireBaseMessagingService.this,CHANNEL_ID)
                        .setSmallIcon(R.drawable.app_shoppingcart)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
        NotificationManager manager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

}
