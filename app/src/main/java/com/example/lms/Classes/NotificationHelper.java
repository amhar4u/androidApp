package com.example.lms.Classes;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.lms.R;

public class NotificationHelper {

    public static void sendNotification(Context context, String title, String message) {
        // Check if the notification permission is granted
        if (NotificationManagerCompat.from(context).areNotificationsEnabled()) {
            // Create the notification channel (if not already created)
            createNotificationChannel(context);

            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "leave_channel")
                    .setSmallIcon(R.drawable.baseline_notifications_24)
                    .setContentTitle(title)
                    .setContentText(message)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setAutoCancel(true);

            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            notificationManager.notify(1, builder.build());
        } else {
            // Handle case when the notification permission is not granted
            // You can show a message to the user or redirect them to the app settings
            Toast.makeText(context, "Please enable notifications for this app", Toast.LENGTH_SHORT).show();
        }
    }

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "Leave Notifications";
            String description = "Notification channel for leave requests";
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("leave_channel", name, importance);
            channel.setDescription(description);

            // Customize the channel settings (optional)
            channel.enableLights(true);
            channel.setLightColor(Color.RED);
            // Set the sound for notifications
            Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            channel.setSound(soundUri, null);

            NotificationManager notificationManager = context.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }


}
