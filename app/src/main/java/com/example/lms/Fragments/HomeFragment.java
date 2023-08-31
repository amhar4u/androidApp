package com.example.lms.Fragments;

import static com.example.lms.R.id.back;
import static com.example.lms.R.id.txtapproveds;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lms.Classes.LeaveRequest;
import com.example.lms.R;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
// Other imports...

public class HomeFragment extends Fragment {

    TextView totleave, balance, approveds, rejects, pendinngs, nopays;
    private final int Totalleave = 50;
    private DatabaseReference leaveRequestRef;

    private ProgressDialog progressDialog;



    //private ImageView userapprove,userpending,userreject,usernopay,userbalance,userleave;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_home, container, false);

        totleave = view.findViewById(R.id.txttotleave);
        balance = view.findViewById(R.id.txtbalance);
        approveds = view.findViewById(R.id.txtapproveds);
        rejects = view.findViewById(R.id.txtrejects);
        pendinngs = view.findViewById(R.id.txtpendings);
        nopays = view.findViewById(R.id.txtnopay);
        leaveRequestRef = FirebaseDatabase.getInstance().getReference("leave_requests"); // Use the correct database reference
        showProgressDialog("Loading...");
        updateLeaveCount();
        updateLeaveRequestCount();
        dismissProgressDialog();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLeaveCount(); // Refresh the data whenever the fragment is resumed
        updateLeaveRequestCount();
    }

    private void updateLeaveCount() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserUid = currentUser.getUid();

            // Query the database for leave requests of the current user
            Query userLeaveRequestsQuery = leaveRequestRef.orderByChild("uid").equalTo(currentUserUid);

            userLeaveRequestsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long leaveCount = 0; // Reset the leaveCount
                    long approvedLeaveCount = 0; // Initialize approvedLeaveCount

                    // Loop through all leave requests of the current user
                    for (DataSnapshot leaveSnapshot : dataSnapshot.getChildren()) {
                        LeaveRequest leaveRequest = leaveSnapshot.getValue(LeaveRequest.class);
                        leaveCount += leaveRequest.getDays();

                        // Check if the leave request is approved and belongs to the current user
                        if (leaveRequest.getStatus().equalsIgnoreCase("approved")) {
                            approvedLeaveCount += leaveRequest.getDays();
                        }
                    }

                    // Update the TextViews for total leave and balance
                    if (leaveCount < 10) {
                        totleave.setText("0" + String.valueOf(approvedLeaveCount));
                    } else {
                        totleave.setText(String.valueOf(approvedLeaveCount));
                    }

                    long nopayCount = Math.max(approvedLeaveCount - Totalleave, 0);
                    if (nopayCount < 10) {
                        nopays.setText("0" + String.valueOf(nopayCount));
                    } else {
                        nopays.setText(String.valueOf(nopayCount));
                    }

                    long balanceCount = Totalleave-approvedLeaveCount;

                    if (balanceCount < 0) {
                        balanceCount = 0;
                    }

                    if (balanceCount < 10) {
                        balance.setText("0" + String.valueOf(balanceCount));
                    } else {
                        balance.setText(String.valueOf(balanceCount));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error if needed
                }
            });
        }
    }


//private void balance(){
//
//    // Get the current user's UID
//    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//    if (currentUser != null) {
//        String currentUserUid = currentUser.getUid();
//
//        // Query the database for approved leave requests of the current user
//        Query userApprovedLeaveRequestsQuery = leaveRequestRef.orderByChild("uid")
//                .equalTo(currentUserUid)
//                .orderByChild("status")
//                .equalTo("approved");
//
//        userApprovedLeaveRequestsQuery.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                long approvedLeaveCount = 0; //
//
//                // Loop through all approved leave requests of the current user
//                for (DataSnapshot leaveSnapshot : dataSnapshot.getChildren()) {
//                    LeaveRequest leaveRequest = leaveSnapshot.getValue(LeaveRequest.class);
//                    approvedLeaveCount += leaveRequest.getDays();
//                }
//                long balanceCount = Totalleave - approvedLeaveCount;
//
//                // Ensure balance is not negative
//                if (balanceCount < 0) {
//                    balanceCount = 0;
//                }
//
//                if (balanceCount < 10) {
//                    balance.setText("0" + String.valueOf(balanceCount));
//                } else {
//                    balance.setText(String.valueOf(balanceCount));
//                }
//
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//
//            }
//        });
//    }

//}

    private void updateLeaveRequestCount() {
        // Get the current user's UID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserUid = currentUser.getUid();

            // Query the database for leave requests of the current user
            Query userLeaveRequestsQuery = leaveRequestRef.orderByChild("uid").equalTo(currentUserUid);

            userLeaveRequestsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int approvedCount = 0;
                    int rejectedCount = 0;
                    int pendingCount = 0;

                    // Loop through all leave requests of the current user
                    for (DataSnapshot leaveSnapshot : dataSnapshot.getChildren()) {
                        LeaveRequest leaveRequest = leaveSnapshot.getValue(LeaveRequest.class);
                        String status = leaveRequest.getStatus();

                        // Check the status and increment the respective count
                        if (status.equalsIgnoreCase("approved")) {
                            approvedCount++;
                        } else if (status.equalsIgnoreCase("rejected")) {
                            rejectedCount++;
                        } else {
                            pendingCount++;
                        }
                    }

                    // Update the TextViews with the respective counts
                    if (approvedCount < 10) {
                        approveds.setText("0" + String.valueOf(approvedCount));
                    } else {
                        approveds.setText(String.valueOf(approvedCount));
                    }
                    if (rejectedCount < 10) {
                        rejects.setText("0" + String.valueOf(rejectedCount));
                    } else {
                        rejects.setText(String.valueOf(rejectedCount));
                    }
                    if (pendingCount < 10) {
                        pendinngs.setText("0" + String.valueOf(pendingCount));
                    } else {
                        pendinngs.setText(String.valueOf(pendingCount));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error if needed
                }
            });
        }
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
