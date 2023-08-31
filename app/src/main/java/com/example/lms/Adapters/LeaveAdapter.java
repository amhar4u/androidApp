package com.example.lms.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lms.Classes.Item;
import com.example.lms.Classes.LeaveRequest;
import com.example.lms.R;
import com.example.lms.Viewholders.LeaveHolder;

import java.util.ArrayList;
import java.util.List;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveHolder> {
    Context context;
    List<LeaveRequest> items; // Change the class name here
    List<LeaveRequest> originalItems; // Store the original list



    public LeaveAdapter(Context context, List<LeaveRequest> items, List<LeaveRequest> originalItems) { // Change the class name here
        this.context = context;
        this.items = items;
        this.originalItems = originalItems; // Initialize the original list
    }

    @NonNull
    @Override
    public LeaveHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        return new LeaveHolder(LayoutInflater.from(context).inflate(R.layout.leave__view, parent, false), items);
    }

    @Override
    public void onBindViewHolder(@NonNull LeaveHolder holder, int position) {
        LeaveRequest leaveRequest = items.get(position);

        holder.showleaveempid.setText(leaveRequest.getEmpid());
        holder.showleavetype.setText(leaveRequest.getType());
        holder.showleavesdate.setText(leaveRequest.getSdate());
        holder.showleaveedate.setText(leaveRequest.getEdate());
        holder.showleavedays.setText(String.valueOf(leaveRequest.getDays()));
        holder.status.setText(leaveRequest.getStatus());


        // Set background color based on status
        String status = leaveRequest.getStatus();
        if ("not approved".equals(status)) {
            holder.status.setBackgroundColor(ContextCompat.getColor(context, R.color.blue)); // Use your desired color resource for blue
        } else if ("approved".equals(status)) {
            holder.status.setBackgroundColor(ContextCompat.getColor(context, R.color.green)); // Use your desired color resource for green
        } else if ("reject".equals(status)) {
            holder.status.setBackgroundColor(ContextCompat.getColor(context, R.color.red)); // Use your desired color resource for red
        } else {
            // Set a default color if the status does not match any of the above conditions
            holder.status.setBackgroundColor(ContextCompat.getColor(context, R.color.default_color)); // Use your desired default color resource
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void filterData(String query) {
        List<LeaveRequest> filteredList = new ArrayList<>();
        for (LeaveRequest item : originalItems) {
            if (item.getEmpid().toLowerCase().contains(query.toLowerCase()) ||
                    item.getSdate().toLowerCase().contains(query.toLowerCase()) ||
                    item.getEdate().toLowerCase().contains(query.toLowerCase()) ||
                    item.getType().toLowerCase().contains(query.toLowerCase()) ||
                    item.getStatus().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(item);
            }
        }
        // Update the adapter list with the filtered data
        items = filteredList;
        notifyDataSetChanged();
    }
}
