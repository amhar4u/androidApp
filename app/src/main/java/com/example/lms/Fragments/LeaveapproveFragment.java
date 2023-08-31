package com.example.lms.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.lms.Activities.Login;
import com.example.lms.Adapters.MyAdapter;
import com.example.lms.Classes.Item;
import com.example.lms.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LeaveapproveFragment extends Fragment {

    private RecyclerView recyclerView;
    private MyAdapter adapter;

    String userId;
    private EditText search;
    private Button searchButton;
    private List<Item> items;

    private  ProgressDialog progressDialog;



    private static final long AUTO_RELOAD_DELAY = 5000; // 5 seconds

    private final Handler autoReloadHandler = new Handler();
    private final Runnable autoReloadRunnable = new Runnable() {
        @Override
        public void run() {
            // Reload the page

            loadLeaveRequests();

            autoReloadHandler.postDelayed(this, AUTO_RELOAD_DELAY);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_leaveapprove, container, false);



        recyclerView = view.findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        search = view.findViewById(R.id.etSearch);
        searchButton = view.findViewById(R.id.btnSearch);

        // Initialize the adapter with an empty list
        items = new ArrayList<>();
        adapter = new MyAdapter(getActivity(), items, userId);
        recyclerView.setAdapter(adapter);


        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = search.getText().toString().trim().toLowerCase();
                filterData(searchText);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Start auto-reload when the fragment is resumed
        startAutoReload();
    }

    @Override
    public void onPause() {
        super.onPause();
        // Stop auto-reload when the fragment is paused
        stopAutoReload();
    }

    private void startAutoReload() {
        autoReloadHandler.postDelayed(autoReloadRunnable, AUTO_RELOAD_DELAY);
    }

    private void stopAutoReload() {
        autoReloadHandler.removeCallbacks(autoReloadRunnable);
    }

    private void loadLeaveRequests() {

        DatabaseReference leaveRequestsRef = FirebaseDatabase.getInstance().getReference("leave_requests");
        Query query = leaveRequestsRef.orderByChild("currentTime");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                items.clear();

                for (DataSnapshot childSnapshot : dataSnapshot.getChildren()) {
                    String empid = childSnapshot.child("empid").getValue(String.class);
                    String type = childSnapshot.child("type").getValue(String.class);
                    String startDate = childSnapshot.child("sdate").getValue(String.class);
                    String endDate = childSnapshot.child("edate").getValue(String.class);
                    long duration = childSnapshot.child("days").getValue(Long.class);
                    String status = childSnapshot.child("status").getValue(String.class);
                    String uid = childSnapshot.child("uid").getValue(String.class);
                    String lid = childSnapshot.child("lid").getValue(String.class);

                    // Check if the user exists in the user table
                    DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(uid);
                    userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.exists()) {
                                // User exists, add the leave request to the list
                                if (status != null && status.equals("not approved")) {
                                    items.add(new Item(type, startDate, endDate, duration, empid, uid, lid));
                                }
                            } else {
                                // User does not exist, remove the leave request from the list
                                childSnapshot.getRef().removeValue();
                            }
                            adapter.notifyDataSetChanged();

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // Handle the cancellation or error case

                        }
                    });
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle error case

            }

        });

    }


    private void filterData(String searchText) {
        List<Item> filteredItems = new ArrayList<>();

        for (Item item : items) {
            if (item.getSDate().toLowerCase().contains(searchText) ||
                    item.getEDate().toLowerCase().contains(searchText) ||
                    item.getType().toLowerCase().contains(searchText) ||
                    item.getEmpid().toLowerCase().contains(searchText)) {
                filteredItems.add(item);
            }
        }

        adapter.setFilteredList(filteredItems);
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
