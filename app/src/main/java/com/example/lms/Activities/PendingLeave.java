package com.example.lms.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class PendingLeave extends AppCompatActivity {

    private RecyclerView pendingRecyclerView;
    private LeaveAdapter adapter;

    private EditText leavesearch;
    private Button leavesearchButton;
    ImageView pendingback;
    private List<LeaveRequest> pendingLeaves;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_pending_leave);

        pendingRecyclerView = findViewById(R.id.pending_recyclerview);
        pendingRecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        leavesearch = findViewById(R.id.etleaveSearch);
        leavesearchButton = findViewById(R.id.btnleaveSearch);
        pendingback=(ImageView)findViewById(R.id.imgPendingback) ;

        // Initialize the adapter with an empty list
        pendingLeaves = new ArrayList<>();
        adapter = new LeaveAdapter(getApplicationContext(), pendingLeaves, pendingLeaves); // Use the same adapter and originalItems list
        pendingRecyclerView.setAdapter(adapter);

        // Fetch data from the database and filter the approved leaves
        DatabaseReference leaveRequestsRef = FirebaseDatabase.getInstance().getReference("leave_requests");
        leaveRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pendingLeaves.clear(); // Clear the current list of approved leaves

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
                            if (dataSnapshot.exists() && "not approved".equals(leaveRequest.getStatus())) {
                                pendingLeaves.add(leaveRequest);
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

        pendingback.setOnClickListener(new View.OnClickListener() {
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

    }


}