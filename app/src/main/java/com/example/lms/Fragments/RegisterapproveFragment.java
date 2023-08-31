package com.example.lms.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.lms.Adapters.userMyAdapter;
import com.example.lms.R;
import com.example.lms.Classes.userItems;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class RegisterapproveFragment extends Fragment {

    private RecyclerView recyclerView;
    private userMyAdapter adapter;
    private DatabaseReference usersRef;
    private ChildEventListener childEventListener;


    private List<userItems> items;
    private List<userItems> filteredItems;

    private Button userSearch;
    private EditText searchUser;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_registerapprove, container, false);

        recyclerView = view.findViewById(R.id.recyclerviewuser);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        userSearch = view.findViewById(R.id.btnuserSearch);
        searchUser = view.findViewById(R.id.userSearch);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");


        items = new ArrayList<>();
        filteredItems = new ArrayList<>();
        adapter = new userMyAdapter(getActivity(), filteredItems);
        recyclerView.setAdapter(adapter);
        childEventListener = usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Called when a new child is added
                userItems user = dataSnapshot.getValue(userItems.class);
                items.add(user);
                filterData(searchUser.getText().toString().trim());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Called when an existing child is changed
                userItems updatedUser = dataSnapshot.getValue(userItems.class);
                int index = getItemIndex(updatedUser);
                if (index != -1) {
                    items.set(index, updatedUser);
                    filterData(searchUser.getText().toString().trim()); // Update the filtered data on child change
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Called when a child is removed
                String empid = dataSnapshot.child("empid").getValue(String.class);
                int index = getItemIndexByEmpId(empid);
                if (index != -1) {
                    items.remove(index);
                    filterData(searchUser.getText().toString().trim());
                }
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Called when a child changes position
                // Not used in this scenario
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Called when the listener is cancelled or encounters an error
            }
        });

        searchUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String searchText = charSequence.toString().trim();
                filterData(searchText);
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        userSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = searchUser.getText().toString().trim();
                filterData(searchText);
            }
        });


        return view;
    }

    private void filterData(String searchText) {
        filteredItems.clear();

        if (searchText.isEmpty()) {
            // If the search text is empty, show all data
            filteredItems.addAll(items);
        } else {
            for (userItems user : items) {
                if (user.getEmpid().toLowerCase().contains(searchText.toLowerCase()) ||
                        user.getEmail().toLowerCase().contains(searchText.toLowerCase()) ||
                        user.getName().toLowerCase().contains(searchText.toLowerCase()) ||
                        user.getContact().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredItems.add(user);
                }
            }
        }

        if (filteredItems.isEmpty()) {
            // Show a toast if no data matches the search criteria
            Toast.makeText(getActivity(), "No matching data found", Toast.LENGTH_SHORT).show();
        }

        adapter.notifyDataSetChanged();
    }


    private int getItemIndex(userItems user) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getEmpid().equals(user.getEmpid())) {
                return i;
            }
        }
        return -1;
    }

    private int getItemIndexByEmpId(String empid) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getEmpid().equals(empid)) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Remove the child event listener when the fragment is destroyed or no longer visible
        if (usersRef != null && childEventListener != null) {
            usersRef.removeEventListener(childEventListener);
        }
    }

}

