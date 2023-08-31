package com.example.lms.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lms.Activities.ApprovedLeave;
import com.example.lms.Activities.PendingLeave;
import com.example.lms.Activities.RejectedLeave;
import com.example.lms.Activities.TotalLeave;
import com.example.lms.Activities.TotUsers;
import com.example.lms.Activities.ViewNotification;
import com.example.lms.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class DashFragment extends Fragment {
    private TextView totUsers, pendings, approves, rejects, notifications, totleaves;
    private ImageView Totalusers, Pendingleavs, Approvedleaves, Rejectedleaves, Notification, Totalleaves;
    private DatabaseReference usersRef, leaveRef, notRef;
    private Handler handler;
    private Runnable runnable;

    private ProgressDialog progressDialog;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_dash, container, false);

        totUsers = (TextView) view.findViewById(R.id.txtusers);
        Totalusers = (ImageView) view.findViewById(R.id.imgusers);
        Pendingleavs = (ImageView) view.findViewById(R.id.imgpending);
        Approvedleaves = (ImageView) view.findViewById(R.id.imgapprove);
        Rejectedleaves = (ImageView) view.findViewById(R.id.imgreject);
        Notification = (ImageView) view.findViewById(R.id.imgnotification);
        Totalleaves = (ImageView) view.findViewById(R.id.imgleave);
        pendings = (TextView) view.findViewById(R.id.txtpending);
        approves = (TextView) view.findViewById(R.id.txtapproved);
        rejects = (TextView) view.findViewById(R.id.txtrejected);
        notifications = (TextView) view.findViewById(R.id.txtnotification);
        totleaves = (TextView) view.findViewById(R.id.txtleaves);
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        leaveRef = FirebaseDatabase.getInstance().getReference("leave_requests");
        notRef = FirebaseDatabase.getInstance().getReference("manager_notification");

        showProgressDialog("Loading...");

        handler = new Handler();
        runnable = new Runnable() {
            @Override
            public void run() {
                // Reload the page
                reloadPage();
            }
        };

        // Start the auto-reload mechanism
        startAutoReload();


        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long userCount = dataSnapshot.getChildrenCount();
                if (userCount < 10) {
                    totUsers.setText("0" + String.valueOf(userCount));
                } else {
                    totUsers.setText(String.valueOf(userCount));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });

        notRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long notificationCount = dataSnapshot.getChildrenCount();
                if (notificationCount < 10) {
                    notifications.setText("0" + String.valueOf(notificationCount));
                } else {
                    notifications.setText(String.valueOf(notificationCount));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });

        leaveRef.orderByChild("status").equalTo("approved").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long approveCount = dataSnapshot.getChildrenCount();
                if (approveCount < 10) {
                    approves.setText("0" + String.valueOf(approveCount));
                } else {
                    approves.setText(String.valueOf(approveCount));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });

        leaveRef.orderByChild("status").equalTo("not approved").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long pendingCount = dataSnapshot.getChildrenCount();
                if (pendingCount < 10) {
                    pendings.setText("0" + String.valueOf(pendingCount));
                } else {
                    pendings.setText(String.valueOf(pendingCount));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });

        leaveRef.orderByChild("status").equalTo("reject").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long rejectCount = dataSnapshot.getChildrenCount();
                if (rejectCount < 10) {
                    rejects.setText("0" + String.valueOf(rejectCount));
                } else {
                    rejects.setText(String.valueOf(rejectCount));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });

        leaveRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                long leaveCount = dataSnapshot.getChildrenCount();
                if (leaveCount < 10) {
                    totleaves.setText("0" + String.valueOf(leaveCount));
                } else {
                    totleaves.setText(String.valueOf(leaveCount));
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
            }
        });


        Totalleaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashFragment.this.getActivity(), TotalLeave.class);
                startActivity(intent);
            }
        });

        Approvedleaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashFragment.this.getActivity(), ApprovedLeave.class);
                startActivity(intent);
            }
        });

        Rejectedleaves.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashFragment.this.getActivity(), RejectedLeave.class);
                startActivity(intent);
            }
        });
        Pendingleavs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashFragment.this.getActivity(), PendingLeave.class);
                startActivity(intent);
            }
        });

        Notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashFragment.this.getActivity(), ViewNotification.class);
                startActivity(intent);
            }
        });

        Totalusers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DashFragment.this.getActivity(), TotUsers.class);
                startActivity(intent);
            }
        });

        dismissProgressDialog();

        return view;
    }

    // Reload the page and restart the auto-reload mechanism
    private void reloadPage() {
        FragmentTransaction ft = getParentFragmentManager().beginTransaction();
        ft.detach(this).attach(this).commitAllowingStateLoss();
        startAutoReload();
    }


    // Start the auto-reload mechanism
    private void startAutoReload() {
        // Delay before reloading the page (e.g., every 5 seconds)
        int delay = 5000; // milliseconds

        // Remove any existing callbacks to avoid duplicate calls
        handler.removeCallbacks(runnable);

        // Schedule the runnable to run after the specified delay
        handler.postDelayed(runnable, delay);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Remove any pending callbacks to avoid memory leaks
        handler.removeCallbacks(runnable);
    }

    private void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage(message);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.setCancelable(false);
        }

        if (!progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    private void dismissProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

}