package com.example.lms.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import android.widget.Toast;

import com.example.lms.Adapters.userMyAdapter;
import com.example.lms.Classes.userItems;
import com.example.lms.R;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

public class TotUsers extends AppCompatActivity {

    private RecyclerView recyclerView;
    private userMyAdapter adapter;
    private DatabaseReference usersRef;
    private ChildEventListener childEventListener;

    private List<userItems> items;

    private List<userItems> filteredItems;
    private ImageView userback;

    private Button searchuser;
    private EditText  searchtext;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_users);

        recyclerView = findViewById(R.id.recyclerviewUser);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        userback=(ImageView) findViewById(R.id.imgUserback) ;
        searchtext=(EditText)findViewById(R.id.Searchuser) ;
        searchuser=(Button)findViewById(R.id.btnSearchuser);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        usersRef = database.getReference("Users");

        // Initialize the list
        items = new ArrayList<>();
        filteredItems = new ArrayList<>();

        // Create the adapter with the items list
        adapter = new userMyAdapter(this, items);
        recyclerView.setAdapter(adapter);

        // Create and attach the child event listener
        childEventListener = usersRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Called when a new child is added
                userItems user = dataSnapshot.getValue(userItems.class);
                items.add(user);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String previousChildName) {
                // Called when an existing child is changed
                userItems updatedUser = dataSnapshot.getValue(userItems.class);
                int index = getItemIndex(updatedUser);
                if (index != -1) {
                    items.set(index, updatedUser);
                    filterData(searchtext.getText().toString().trim()); // Update the filtered data on child change
                }
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                // Called when a child is removed
                userItems removedUser = dataSnapshot.getValue(userItems.class);
                int index = getItemIndex(removedUser);
                if (index != -1) {
                    items.remove(index);
                    adapter.notifyItemRemoved(index);
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

        userback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Manager.class);
                startActivity(intent);
            }
        });

        searchtext.addTextChangedListener(new TextWatcher() {
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

        searchuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String searchText = searchtext.getText().toString().trim();
                filterData(searchText);
            }
        });

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
            Toast.makeText(this, "No matching data found", Toast.LENGTH_SHORT).show();
        }

        adapter.setItems(filteredItems); // Update the adapter with the filtered data
    }


    private int getItemIndex(userItems user) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getUid().equals(user.getUid())) {
                return i;
            }
        }
        return -1;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Remove the child event listener when the activity is destroyed or no longer visible
        if (usersRef != null && childEventListener != null) {
            usersRef.removeEventListener(childEventListener);
        }
    }
}


