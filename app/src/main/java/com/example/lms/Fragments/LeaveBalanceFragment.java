package com.example.lms.Fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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


public class LeaveBalanceFragment extends Fragment {

    TextView leave,shortlv,sicklv,casuallv,pendinglv,approvedlv,rejectlv,nopaylv,lvbalance;
    private DatabaseReference usersRef, leaveRef;

    private final int Totalleave = 50;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view= inflater.inflate(R.layout.fragment_leave_balance, container, false);

        leave=view.findViewById(R.id.txttleave);
        shortlv=view.findViewById(R.id.txtshleave);
        sicklv=view.findViewById(R.id.txtsleave);
        casuallv=view.findViewById(R.id.txtcleave);
        pendinglv=view.findViewById(R.id.txtpleave);
        approvedlv=view.findViewById(R.id.txtaleave);
        rejectlv=view.findViewById(R.id.txtrleave);
        nopaylv=view.findViewById(R.id.txtnleave);
        lvbalance=view.findViewById(R.id.txtleavebl);
        usersRef = FirebaseDatabase.getInstance().getReference("Users");
        leaveRef = FirebaseDatabase.getInstance().getReference("leave_requests");


        updateLeaveTypeCount();
        updateLeaveRequestCount();
        updateLeaveCount();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        updateLeaveCount();
        updateLeaveRequestCount();
        updateLeaveTypeCount();
    }

    private void updateLeaveRequestCount() {
        // Get the current user's UID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserUid = currentUser.getUid();

            // Query the database for leave requests of the current user
            Query userLeaveRequestsQuery = leaveRef.orderByChild("uid").equalTo(currentUserUid);

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
                    if(approvedCount<10){
                        approvedlv.setText("0"+String.valueOf(approvedCount));
                    }
                    else {
                        approvedlv.setText(String.valueOf(approvedCount));
                    }
                    if (rejectedCount<10){
                        rejectlv.setText("0"+String.valueOf(rejectedCount));
                    }
                    else {
                        rejectlv.setText(String.valueOf(rejectedCount));
                    }
                    if(pendingCount<10){
                        pendinglv.setText("0"+String.valueOf(pendingCount));
                    }
                    else {
                        pendinglv.setText(String.valueOf(pendingCount));
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error if needed
                }
            });
        }
    }

    private void updateLeaveCount() {
        // Get the current user's UID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserUid = currentUser.getUid();

            // Query the database for leave requests of the current user
            Query userLeaveRequestsQuery = leaveRef.orderByChild("uid").equalTo(currentUserUid);

            userLeaveRequestsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    long leaveCount = 0;

                    // Loop through all leave requests of the current user
                    for (DataSnapshot leaveSnapshot : dataSnapshot.getChildren()) {
                        LeaveRequest leaveRequest = leaveSnapshot.getValue(LeaveRequest.class);
                        leaveCount += leaveRequest.getDays();
                    }

                    // Calculate the balance
                    long balanceCount = Totalleave - leaveCount;

                    // Ensure balance is not negative
                    if (balanceCount < 0) {
                        balanceCount = 0;
                    }

                    // Update the TextViews
                    if (leaveCount < 10) {
                        leave.setText("0" + String.valueOf(leaveCount));
                    } else {
                        leave.setText(String.valueOf(leaveCount));
                    }

                    if (balanceCount < 10) {
                        lvbalance.setText("0" + String.valueOf(balanceCount));
                    } else {
                        lvbalance.setText(String.valueOf(balanceCount));
                    }

                    // Calculate the nopay count
                    long nopayCount = Math.max(leaveCount - Totalleave, 0);
                    if(nopayCount<10){
                        nopaylv.setText("0"+String.valueOf(nopayCount));
                    }
                    else{
                        nopaylv.setText(String.valueOf(nopayCount));}
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error if needed
                }
            });
        }
    }

    private void updateLeaveTypeCount() {
        // Get the current user's UID
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String currentUserUid = currentUser.getUid();

            // Query the database for leave requests of the current user
            Query userLeaveRequestsQuery = leaveRef.orderByChild("uid").equalTo(currentUserUid);

            userLeaveRequestsQuery.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    int casualCount = 0;
                    int shortCount = 0;
                    int sickCount = 0;

                    // Loop through all leave requests of the current user
                    for (DataSnapshot leaveSnapshot : dataSnapshot.getChildren()) {
                        LeaveRequest leaveRequest = leaveSnapshot.getValue(LeaveRequest.class);
                        if (leaveRequest != null) {
                            String type = leaveRequest.getType();
                            int days = Integer.parseInt(String.valueOf(leaveRequest.getDays()));

                            // Check the type and increment the respective count
                            if (type.equalsIgnoreCase("Casual")) {
                                casualCount += days;
                            } else if (type.equalsIgnoreCase("Short")) {
                                shortCount += days;
                            } else if (type.equalsIgnoreCase("Sick")) {
                                sickCount += days;
                            }
                        }
                    }

                    // Update the TextViews with the respective counts
                    if (casualCount < 10) {
                        casuallv.setText("0" + String.valueOf(casualCount));
                    } else {
                        casuallv.setText(String.valueOf(casualCount));
                    }
                    if (shortCount < 10) {
                        shortlv.setText("0" + String.valueOf(shortCount));
                    } else {
                        shortlv.setText(String.valueOf(shortCount));
                    }
                    if (sickCount < 10) {
                        sicklv.setText("0" + String.valueOf(sickCount));
                    } else {
                        sicklv.setText(String.valueOf(sickCount));
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle database error if needed
                }
            });
        }
    }



}