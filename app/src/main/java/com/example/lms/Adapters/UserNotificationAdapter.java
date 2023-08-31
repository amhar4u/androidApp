package com.example.lms.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lms.Classes.Notification;
import com.example.lms.Classes.userNotification;
import com.example.lms.R;
import com.example.lms.Viewholders.UserNotificationViewHolder;
import com.squareup.picasso.Picasso;

import java.util.List;

public class UserNotificationAdapter extends RecyclerView.Adapter<UserNotificationViewHolder> {

    private List<userNotification> items;
    private Context context;
    private UserNotificationViewHolder.OnDeleteClickListener onDeleteClickListener;

    public UserNotificationAdapter(List<userNotification> items, Context context, UserNotificationViewHolder.OnDeleteClickListener onDeleteClickListener) {
        this.items = items;
        this.context = context;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public UserNotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.notification_view, parent, false);
        return new UserNotificationViewHolder(itemView, items, context, onDeleteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull UserNotificationViewHolder holder, int position) {

        userNotification notification = items.get(position);
        holder.title.setText(notification.getTitle());
        holder.date.setText(notification.getFormattedDate());
        holder.time.setText(notification.getTime());
        holder.subject.setText(notification.getSubject());
        Picasso.get().load(notification.getPropic()).into(holder.propic);
        holder.setBackgroundBasedOnStatus(notification.getStatus());

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setNotifications(List<userNotification> notifications) {
        items = notifications;
        notifyDataSetChanged();
    }

    public void deleteNotification(userNotification notification) {
        int position = items.indexOf(notification);
        if (position != -1) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }
}
