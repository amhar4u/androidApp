package com.example.lms.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lms.Activities.ViewNotification;
import com.example.lms.Classes.Notification;
import com.example.lms.R;
import com.example.lms.Viewholders.NotificationHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationHolder> {

    private List<Notification> items;
    private Context context;
    private NotificationHolder.OnDeleteClickListener onDeleteClickListener;

    public NotificationAdapter(List<Notification> items, Context context, NotificationHolder.OnDeleteClickListener onDeleteClickListener) {
        this.items = items;
        this.context = context;
        this.onDeleteClickListener = onDeleteClickListener;
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.notification_view, parent, false);
        return new NotificationHolder(itemView, items, context, onDeleteClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationHolder holder, int position) {
        Notification notification = items.get(position);
        // Bind the data to the ViewHolder
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

    public void setNotifications(List<Notification> notifications) {
        items = notifications;
        notifyDataSetChanged();
    }

    public void deleteNotification(Notification notification) {
        int position = items.indexOf(notification);
        if (position != -1) {
            items.remove(position);
            notifyItemRemoved(position);
        }
    }
}
