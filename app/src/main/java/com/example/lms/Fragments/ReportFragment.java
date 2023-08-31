package com.example.lms.Fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupMenu;

import com.example.lms.Adapters.ReportAdapter;
import com.example.lms.Adapters.userMyAdapter;
import com.example.lms.Classes.ReportClass;
import com.example.lms.Classes.userItems;
import com.example.lms.R;
import com.example.lms.Viewholders.ReportHolder;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


public class ReportFragment extends Fragment {

    private ReportHolder reportHolder;
    private RecyclerView reportrecyclerview;
    Button reportsearch;
    EditText reporttext;

    private ReportAdapter adapter;
    private List<ReportClass> reportList;
    DatabaseReference usersRef;
    FirebaseDatabase database;
    DatabaseReference leaveRequestsRef;

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_report, container, false);

        reportrecyclerview = view.findViewById(R.id.recyclerviewreport);
        reportrecyclerview.setLayoutManager(new LinearLayoutManager(getActivity()));
        reportsearch = view.findViewById(R.id.btnreportSearch);
        reporttext = view.findViewById(R.id.txtreportSearch);
        database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");
        leaveRequestsRef = database.getReference("leave_requests");

        reportList = new ArrayList<>();
        adapter = new ReportAdapter(requireContext(), new ArrayList<>());
        reportrecyclerview.setAdapter(adapter);

        reporttext.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Filter the data based on entered empid
                String empId = charSequence.toString().trim();
                adapter.filterList(empId);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final int totalUsers = (int) dataSnapshot.getChildrenCount();
                final AtomicInteger usersProcessed = new AtomicInteger(0);

                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()) {
                    String empId = userSnapshot.child("empid").getValue(String.class);
                    String profilePicUrl = userSnapshot.child("profilepic").getValue(String.class);

                    leaveRequestsRef.orderByChild("empid").equalTo(empId).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            int totalLeaveDays = 0;
                            int approvedLeaveDays = 0;
                            int rejectedLeaveDays = 0;
                            int pendingLeaveDays = 0;
                            int casualLeaveCount = 0;
                            int shortLeaveCount = 0;
                            int sickLeaveCount = 0;

                            for (DataSnapshot leaveSnapshot : dataSnapshot.getChildren()) {
                                String status = leaveSnapshot.child("status").getValue(String.class);
                                String type = leaveSnapshot.child("type").getValue(String.class);
                                int days = leaveSnapshot.child("days").getValue(Integer.class);

                                totalLeaveDays += days;

                                if ("approved".equals(status)) {
                                    approvedLeaveDays++;
                                } else if ("reject".equals(status)) {
                                    rejectedLeaveDays++;
                                } else {
                                    pendingLeaveDays++;
                                }

                                if ("Casual".equals(type)) {
                                    casualLeaveCount += days;
                                } else if ("Short".equals(type)) {
                                    shortLeaveCount += days ;
                                } else if ("Sick".equals(type)) {
                                    sickLeaveCount += days;
                                }
                            }

                            // Calculate the balance leave and noPayDays
                            int totalBalance = 50; // Adjust this according to your requirements
                            int balanceLeave = totalBalance - totalLeaveDays;
                            int noPayDays = balanceLeave > 0 ? 0 : Math.abs(balanceLeave);

                            // Create the ReportClass object and add it to the list
                            ReportClass report = new ReportClass();
                            report.setEmpId(empId);
                            report.setProfilePicUrl(profilePicUrl);
                            report.setTotalLeaveDays(totalLeaveDays);
                            report.setApprovedLeaveDays(approvedLeaveDays);
                            report.setRejectedLeaveDays(rejectedLeaveDays);
                            report.setPendingLeaveDays(pendingLeaveDays);
                            report.setCasualLeaveCount(casualLeaveCount);
                            report.setShortLeaveCount(shortLeaveCount);
                            report.setSickLeaveCount(sickLeaveCount);
                            report.setBalanceLeave(balanceLeave);
                            report.setNoPayDays(noPayDays);

                            reportList.add(report);


                            int processedUsers = usersProcessed.incrementAndGet();
                            if (processedUsers == totalUsers) {
                                // All users' data is processed, notify the adapter
                                adapter.setReportList(reportList);
                            }
                        }

                            @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle the cancellation or error case
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle the cancellation or error case
            }
        });

        setupRecyclerView();

        return view;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // Delegate the result to the ReportHolder to handle the permission request
        reportHolder.onRequestPermissionsResult(requestCode, grantResults);
    }

    private void setupRecyclerView() {
        // ... (other setup code for RecyclerView)

        // Inflate the dummy_layout view
        View dummyView = LayoutInflater.from(requireContext()).inflate(R.layout.dummy_layout, reportrecyclerview, false);

        // Create the ReportHolder and pass the dummy view and the context
        reportHolder = new ReportHolder(dummyView, requireContext());

        // Initialize the adapter here before setting it to the RecyclerView
        adapter = new ReportAdapter(requireContext(), reportList);

        // Set the adapter to the RecyclerView
        reportrecyclerview.setAdapter(adapter);
    }


}