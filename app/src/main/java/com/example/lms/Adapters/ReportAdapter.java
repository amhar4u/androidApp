package com.example.lms.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lms.Classes.ReportClass;
import com.example.lms.R;
import com.example.lms.Viewholders.ReportHolder;

import java.util.ArrayList;
import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportHolder> {
    private List<ReportClass> originalReportList; // Store the original unfiltered list
    private List<ReportClass> filteredReportList; // Store the filtered list
    private Context context;

    public ReportAdapter(Context context, List<ReportClass> reportList) {
        this.context = context;
        this.originalReportList = reportList;
        this.filteredReportList = new ArrayList<>(reportList); // Initialize with all data
    }

    public void setReportList(List<ReportClass> reportList) {
        this.originalReportList = reportList;
        this.filteredReportList = new ArrayList<>(reportList);
        notifyDataSetChanged();
    }

    public void filterList(String empId) {
        filteredReportList.clear();
        if (empId.isEmpty()) {
            filteredReportList.addAll(originalReportList); // If search text is empty, show all data
        } else {
            for (ReportClass report : originalReportList) {
                if (report.getEmpId().toLowerCase().contains(empId.toLowerCase())) {
                    filteredReportList.add(report);
                }
            }
        }
        notifyDataSetChanged(); // Notify the adapter that data has changed
    }

    @NonNull
    @Override
    public ReportHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        return new ReportHolder(LayoutInflater.from(context).inflate(R.layout.report_view, parent, false), context);
    }


    @Override
    public void onBindViewHolder(@NonNull ReportHolder holder, int position) {
        ReportClass report = filteredReportList.get(position);
        holder.bindData(report);
    }

    @Override
    public int getItemCount() {
        return filteredReportList.size();
    }
}