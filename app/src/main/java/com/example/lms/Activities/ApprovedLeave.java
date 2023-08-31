package com.example.lms.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.lms.Adapters.LeaveAdapter;
import com.example.lms.Classes.LeaveRequest;
import com.example.lms.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ApprovedLeave extends AppCompatActivity {
    private RecyclerView approvedRecyclerView;
    private LeaveAdapter adapter;

    private EditText leavesearch;
    private Button leavesearchButton;
    ImageView approvedback;
    private List<LeaveRequest> approvedLeaves;
    DatabaseReference leaveRequestsRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_approved_leave);

        approvedRecyclerView = findViewById(R.id.approved_recyclerview);
        approvedRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        leavesearch = findViewById(R.id.etleaveSearch);
        leavesearchButton = findViewById(R.id.btnleaveSearch);
        approvedback=(ImageView)findViewById(R.id.imgApproveback) ;

        // Initialize the adapter with an empty list
        approvedLeaves = new ArrayList<>();
        adapter = new LeaveAdapter(getApplicationContext(), approvedLeaves, approvedLeaves); // Use the same adapter and originalItems list
        approvedRecyclerView.setAdapter(adapter);

        // Fetch data from the database and filter the approved leaves
        leaveRequestsRef = FirebaseDatabase.getInstance().getReference("leave_requests");
        leaveRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                approvedLeaves.clear(); // Clear the current list of approved leaves

                for (DataSnapshot leaveSnapshot : dataSnapshot.getChildren()) {
                    LeaveRequest leaveRequest = leaveSnapshot.getValue(LeaveRequest.class);
                    String empid = leaveRequest.getEmpid();

                    // Check if the user's empid exists in the Users table
                    DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
                    Query userQuery = usersRef.orderByChild("empid").equalTo(empid);
                    userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // If the empid exists in the Users table and the leave status is "approved",
                            // add the leave request to the approvedLeaves list.
                            if (dataSnapshot.exists() && "approved".equals(leaveRequest.getStatus())) {
                                approvedLeaves.add(leaveRequest);
                                adapter.notifyDataSetChanged(); // Notify the adapter about the data change
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            // Handle database error if needed
                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
            }
        });

        approvedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Manager.class);
                startActivity(intent);
            }
        });

        leavesearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String query = leavesearch.getText().toString().trim();
                adapter.filterData(query);
            }
        });
        leavesearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                // Not used in this case
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString().trim();
                adapter.filterData(query);
            }

            @Override
            public void afterTextChanged(Editable editable) {
                // Not used in this case
            }
        });

        leaveRequestsRef = FirebaseDatabase.getInstance().getReference("leave_requests");
        leaveRequestsRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                LeaveRequest leaveRequest = dataSnapshot.getValue(LeaveRequest.class);
                String empid = leaveRequest.getEmpid();

                // Check if the user's empid exists in the Users table
                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");
                Query userQuery = usersRef.orderByChild("empid").equalTo(empid);
                userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        // If the empid exists in the Users table and the leave status is "approved",
                        // add the leave request to the approvedLeaves list.
                        if (dataSnapshot.exists() && "approved".equals(leaveRequest.getStatus())) {
                            approvedLeaves.add(leaveRequest);
                            adapter.notifyDataSetChanged(); // Notify the adapter about the data change
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        // Handle database error if needed
                    }
                });
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // This method will be called when a leave request is updated (e.g., status changes from "pending" to "approved")
                // You can handle the update here if needed.
                // For example, you might want to remove the old leave request from the list and add the updated one.
                // In this case, you can call approvedLeaves.clear() and then re-add all approved leave requests.
                // For simplicity, this example assumes that only new leave requests are added and not updated.
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // This method will be called when a leave request is removed (e.g., status changes from "approved" to "canceled")
                // You can handle the removal here if needed.
                // For example, you might want to remove the leave request from the approvedLeaves list.
                // In this case, you can call approvedLeaves.remove(leaveRequest) to remove the specific leave request.
                // For simplicity, this example assumes that leave requests are not removed once they are approved.
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // This method will be called when a leave request changes position in the list (e.g., priority changes)
                // This example does not handle this case, but you can implement custom logic if needed.
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
            }
        });

    }
}