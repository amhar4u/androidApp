package com.example.lms.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lms.Adapters.LeaveAdapter;
import com.example.lms.Adapters.NotificationAdapter;
import com.example.lms.Classes.LeaveRequest;
import com.example.lms.Classes.Notification;
import com.example.lms.R;
import com.example.lms.Viewholders.NotificationHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ViewNotification extends AppCompatActivity implements NotificationHolder.OnDeleteClickListener {

    private RecyclerView notificationRecyclerView;
    private NotificationAdapter adapter;
    private List<Notification> notificationList;

    private DatabaseReference databaseRef;

    public ImageView notback, notunread;
    public TextView unreadtext;

    private boolean showUnreadOnly = false;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_view_notification);

        databaseRef = FirebaseDatabase.getInstance().getReference("manager_notification");

        notificationList = new ArrayList<>();

        notificationRecyclerView = findViewById(R.id.notificationrecyclerview);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(notificationList, this, this); // Pass 'this' as the onDeleteClickListener
        notificationRecyclerView.setAdapter(adapter);
        notback = findViewById(R.id.imgnotificationback);
        notunread = findViewById(R.id.imgunread);
        unreadtext = findViewById(R.id.txtunread);

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationList.clear();
                int unreadCount = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Notification notification = snapshot.getValue(Notification.class);
                    if (notification != null) {
                        notificationList.add(notification);
                        if (notification.getStatus().equals("unread")) {
                            unreadCount++;
                        }
                    }
                }
                if(unreadCount==0){
                    unreadtext.setVisibility(View.GONE);
                }
                else {
                    unreadtext.setText(String.valueOf(unreadCount));
                }

                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ViewNotification.this, "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        notback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Manager.class);
                startActivity(intent);
            }
        });

        notunread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUnreadOnly = !showUnreadOnly;
                filterNotifications();
            }
        });
    }

    private void filterNotifications() {
        List<Notification> filteredList = new ArrayList<>();

        // Check if the flag is set to show only unread notifications
        if (showUnreadOnly) {
            // Filter the notifications to include only the ones with status "unread"
            for (Notification notification : notificationList) {
                if (notification.getStatus().equals("unread")) {
                    filteredList.add(notification);
                }
            }
        } else {
            // If showUnreadOnly is false, show all notifications
            filteredList.addAll(notificationList);
        }

        adapter.setNotifications(filteredList);
    }

    @Override
    public void onDeleteClick(Notification notification) {
        adapter.deleteNotification(notification);
        // Here you can remove the notification from the Firebase Realtime Database as well
        DatabaseReference notificationRef = databaseRef.child(notification.getId());
        notificationRef.removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Notification deleted successfully from the database
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(ViewNotification.this, "Failed to delete notification from the database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
