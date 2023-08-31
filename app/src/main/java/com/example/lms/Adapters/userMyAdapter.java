package com.example.lms.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lms.R;
import com.example.lms.Classes.userItems;
import com.example.lms.Viewholders.userViewHolder;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.List;

public class userMyAdapter extends RecyclerView.Adapter<userViewHolder> {
    Context context;
    List<userItems> items;

    public userMyAdapter(List<userItems> items) {
        this.items = items;
    }

    public userMyAdapter(Context context, List<userItems> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public userViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_view, parent, false);
        return new userViewHolder(view, items, this);
    }

    @Override
    public void onBindViewHolder(@NonNull userViewHolder holder, int position) {
        userItems user = items.get(position);

        holder.userempid.setText(user.getEmpid());
        holder.username.setText(user.getName());
        holder.useremail.setText(user.getEmail());
        holder.usercontact.setText(user.getContact());
        Picasso.get().load(user.getProfilepic()).into(holder.userprofile);
    }

    @Override
    public int getItemCount() {
        return items.size(); // Return the actual number of items in the list
    }

    public void addItem(userItems item) {
        items.add(item);
        notifyItemInserted(items.size() - 1);
    }

    public void updateItem(userItems updatedUser) {
        for (int i = 0; i < items.size(); i++) {
            userItems updatedItem = null;
            if (items.get(i).getEmpid().equals(updatedItem.getEmpid())) {
                items.set(i, updatedItem);
                notifyItemChanged(i);
                break;
            }
        }
    }
        public void deleteUser(int position) {
            // Get the user from your data source based on the position
            userItems user = items.get(position);

            // Get a reference to the Firebase database
            DatabaseReference databaseRef = FirebaseDatabase.getInstance().getReference();

            // Create a database query to find the user with a specific identifier (e.g., userId)
            Query userQuery = databaseRef.child("Users").orderByChild("").equalTo(user.getUid());

            // Remove the user from the database
            userQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        snapshot.getRef().removeValue();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    // Handle the cancellation or error case, if necessary
                }
            });

            // Remove the user from your data source
            items.remove(position);

            // Notify the adapter that an item has been removed at the specified position
            notifyItemRemoved(position);
        }



    public void removeItem(String empid) {
        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).getEmpid().equals(empid)) {
                items.remove(i);
                notifyItemRemoved(i);
                break;
            }
        }
    }

    public void setItems(List<userItems> items) {
        this.items = items;
        notifyDataSetChanged();
    }
}
