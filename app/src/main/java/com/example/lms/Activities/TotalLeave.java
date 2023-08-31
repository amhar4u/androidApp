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
import com.example.lms.Adapters.MyAdapter;
import com.example.lms.Classes.Item;
import com.example.lms.Classes.LeaveRequest;
import com.example.lms.Fragments.DashFragment;
import com.example.lms.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class TotalLeave extends AppCompatActivity {

    private RecyclerView leaverecyclerView;
    private LeaveAdapter adapter;

    private EditText leavesearch;
    private Button leavesearchButton;
    private ImageView back;
    private List<LeaveRequest> items; // Change the class name here
    private List<LeaveRequest> originalItems; // Store the original list

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_total_leave);

        leaverecyclerView = findViewById(R.id.leaverecyclerview);
        leaverecyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        back = findViewById(R.id.imgback);
        leavesearch = findViewById(R.id.etleaveSearch);
        leavesearchButton = findViewById(R.id.btnleaveSearch);

        // Initialize the adapter with an empty list
        items = new ArrayList<>();
        originalItems = new ArrayList<>();
        adapter = new LeaveAdapter(getApplicationContext(), items, originalItems); // Change the class name here
        leaverecyclerView.setAdapter(adapter);

        // Fetch data from the database and update the adapter
        DatabaseReference leaveRequestsRef = FirebaseDatabase.getInstance().getReference("leave_requests");
        leaveRequestsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                items.clear(); // Clear the current list of items
                originalItems.clear(); // Clear the original list

                for (DataSnapshot leaveSnapshot : dataSnapshot.getChildren()) {
                    LeaveRequest leaveRequest = leaveSnapshot.getValue(LeaveRequest.class); // Change the class name here
                    items.add(leaveRequest); // Add each item to the list
                    originalItems.add(leaveRequest); // Add each item to the original list
                }

                adapter.notifyDataSetChanged(); // Notify the adapter about the data change
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                // Handle database error if needed
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
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
