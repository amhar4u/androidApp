package com.example.lms.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lms.Classes.Item;
import com.example.lms.R;
import com.example.lms.Viewholders.ViewHolder;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<ViewHolder> {
    Context context;
    List<Item> items;
    String userId; // Add the userId field to the adapter

    public MyAdapter(Context context, List<Item> items, String userId) {
        this.context = context;
        this.items = items;
        this.userId = userId; // Initialize the userId field
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_view, parent, false), items, userId);
    }


    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.showempid.setText(items.get(position).getEmpid());
        holder.showtype.setText(items.get(position).getType());
        holder.showsdate.setText(items.get(position).getSDate());
        holder.showedate.setText(items.get(position).getEDate());
        holder.showdays.setText(String.valueOf(items.get(position).getDays()));

    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setFilteredList(List<Item> filteredItems) {
        items = filteredItems;
        notifyDataSetChanged();
    }
}
