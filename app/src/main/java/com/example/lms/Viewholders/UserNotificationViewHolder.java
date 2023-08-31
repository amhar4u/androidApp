package com.example.lms.Viewholders;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lms.Classes.Notification;
import com.example.lms.Classes.userNotification;
import com.example.lms.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserNotificationViewHolder extends RecyclerView.ViewHolder {

    public TextView title, date, time, subject;
    public ImageView delete;

    public CircleImageView propic;
    public LinearLayout notificationLayout;

    private Context context;
    private List<userNotification> items;
    private UserNotificationViewHolder.OnDeleteClickListener onDeleteClickListener;

    public UserNotificationViewHolder(@NonNull View itemView, List<userNotification> items, Context context, OnDeleteClickListener onDeleteClickListener) {
        super(itemView);

        this.context = this.context;
        this.items = items;
        this.onDeleteClickListener = onDeleteClickListener;


        title = itemView.findViewById(R.id.notification_title);
        date = itemView.findViewById(R.id.notification_date);
        time = itemView.findViewById(R.id.notification_time);
        subject = itemView.findViewById(R.id.notification_subject);
        delete = itemView.findViewById(R.id.imgdelete);
        propic = itemView.findViewById(R.id.notification_profile_pic);
        notificationLayout = itemView.findViewById(R.id.notification_layout);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    userNotification notification = items.get(position);
                    onDeleteClickListener.onDeleteClick(notification);
                }
            }
        });

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    userNotification notification = items.get(position);
                    String title = notification.getTitle();

                    // Update the notification status to "read"
                    updateNotificationStatus(notification.getId());
                }
            }
        });
    }
    private void updateNotificationStatus(String notificationId) {
        DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("user_notification");
        databaseRef.child(notificationId).child("status").setValue("read")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Status updated successfully
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(context, "Failed to update status: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void setBackgroundBasedOnStatus(String status) {
        if (status != null && status.equals("unread")) {
            notificationLayout.setBackgroundColor(Color.parseColor("#AFDBF5"));
        } else {
            notificationLayout.setBackgroundColor(Color.WHITE);
        }
    }

    public interface OnDeleteClickListener {
        void onDeleteClick(userNotification notification);
    }

    public void deleteNotification(userNotification notification) {
        int position = items.indexOf(notification);
        if (position != -1) {
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size());

            // Delete the notification from the Firebase Realtime Database
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference("manager_notification");
            databaseRef.child(notification.getId()).removeValue()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Notification deleted successfully
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Failed to delete notification
                            Toast.makeText(context, "Failed to delete notification: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void notifyItemRangeChanged(int position, int size) {
    }

    private void notifyItemRemoved(int position) {

    }

}
