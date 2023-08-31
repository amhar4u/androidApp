package com.example.lms.Classes;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.lms.Classes.NotificationHelper;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = "MyFirebaseMessagingService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if (remoteMessage.getData().size() > 0) {
            String title = remoteMessage.getData().get("title");
            String message = remoteMessage.getData().get("message");
            NotificationHelper.sendNotification(getApplicationContext(), title, message);
        }
    }

    @SuppressLint("LongLogTag")
    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
        // Save or update the token in your database
        // You can associate it with the user's account for sending personalized notifications
    }
}

