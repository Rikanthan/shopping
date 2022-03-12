package com.example.login_page.notification;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "Authorization:key=AAAAUDvG1_s:APA91bF4-OFoaYiJ1g4tzKFJPxx6S9UqgJnV-pFVV128uXkBsJ7wFLCrdJBjGeC1d0XKWDA5BXWAHhZ4OUAZQV0znXsOeE4bdAa5hHvslFmJr3hmFMrqRRZ2V4ft7JZKfY92Je4tyW1E" // Your server key refer to video for finding your server key
            }
    )

    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}
