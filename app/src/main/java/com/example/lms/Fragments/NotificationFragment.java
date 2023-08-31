package com.example.lms.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lms.Activities.ViewNotification;
import com.example.lms.Adapters.NotificationAdapter;
import com.example.lms.Adapters.UserNotificationAdapter;
import com.example.lms.Classes.Notification;
import com.example.lms.Classes.userNotification;
import com.example.lms.R;
import com.example.lms.Viewholders.UserNotificationViewHolder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment implements UserNotificationViewHolder.OnDeleteClickListener {

    private RecyclerView notificationRecyclerView;
    private UserNotificationAdapter adapter;
    private List<userNotification> notificationList;

    private String currentUserId;

    private DatabaseReference databaseRef;

    public ImageView notunread;
    public TextView unreadtext;

    private boolean showUnreadOnly = false;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view= inflater.inflate(R.layout.fragment_notification, container, false);


        databaseRef = FirebaseDatabase.getInstance().getReference("user_notification");

        notificationList = new ArrayList<>();

        notificationRecyclerView = view.findViewById(R.id.usernotificationrecyclerview);
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new UserNotificationAdapter(notificationList, getActivity(), this);
        notificationRecyclerView.setAdapter(adapter);
        notunread = view.findViewById(R.id.imguserunread);
        unreadtext = view.findViewById(R.id.txtuserunread);

        loadNotifications();
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                notificationList.clear();
                int unreadCount = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    userNotification notification = snapshot.getValue(userNotification.class);
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
                Toast.makeText(getActivity(), "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        notunread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showUnreadOnly = !showUnreadOnly;
                filterNotifications();
            }
        });

        return view;
    }

    private void filterNotifications() {
        List<userNotification> filteredList = new ArrayList<>();

        // Check if the flag is set to show only unread notifications
        if (showUnreadOnly) {
            // Filter the notifications to include only the ones with status "unread"
            for (userNotification notification : notificationList) {
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
    public void onDeleteClick(userNotification notification) {
        adapter.deleteNotification(notification);

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
                        Toast.makeText(getActivity(), "Failed to delete notification from the database: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void loadNotifications() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            currentUserId = currentUser.getUid();
        } else {
        }
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    String empid = snapshot.child("empid").getValue(String.class);
                    if (empid != null) {
                        DatabaseReference notificationRef = FirebaseDatabase.getInstance().getReference("user_notification");
                        notificationRef.orderByChild("userId").equalTo(empid).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                notificationList.clear();
                                int unreadCount = 0;
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    userNotification notification = snapshot.getValue(userNotification.class);
                                    if (notification != null) {
                                        notificationList.add(notification);
                                        if (notification.getStatus().equals("unread")) {
                                            unreadCount++;
                                        }
                                    }
                                }
                                unreadtext.setText(String.valueOf(unreadCount));

                                adapter.notifyDataSetChanged();
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getActivity(), "Failed to retrieve data: " + databaseError.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Failed to retrieve data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

}