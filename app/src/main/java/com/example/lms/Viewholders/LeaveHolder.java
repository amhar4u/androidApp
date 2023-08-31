package com.example.lms.Viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lms.Classes.LeaveRequest;
import com.example.lms.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class LeaveHolder extends RecyclerView.ViewHolder {

    public TextView showleaveempid;
    public TextView showleavetype;
    public TextView showleavesdate;
    public TextView showleaveedate;
    public TextView showleavedays;

    public TextView status;


    public Button leavedlt;

    public LeaveHolder(@NonNull View itemView, List<LeaveRequest> items) {
        super(itemView);
        showleaveempid = itemView.findViewById(R.id.txtshowleaveempid);
        showleavetype = itemView.findViewById(R.id.txtshowleavetype);
        showleavesdate = itemView.findViewById(R.id.txtshowleavesdate);
        showleaveedate = itemView.findViewById(R.id.txtshowleaveedate);
        showleavedays = itemView.findViewById(R.id.txtshowleavedays);
        status=itemView.findViewById(R.id.txtstatus);
        leavedlt=itemView.findViewById(R.id.btnleavedelete);


        leavedlt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    LeaveRequest leaveRequest = items.get(position);
                    // Call a method to delete the leave request from the database
                    deleteLeaveRequestFromDatabase(leaveRequest);
                }
            }
        });
    }

    private void deleteLeaveRequestFromDatabase(LeaveRequest leaveRequest) {
        // Implement the code to delete the leave request from the database here
        // You can use DatabaseReference to access the database and remove the data
        DatabaseReference leaveRequestsRef = FirebaseDatabase.getInstance().getReference("leave_requests");
        leaveRequestsRef.child(leaveRequest.getLid()).removeValue()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(itemView.getContext(), "Leave request deleted successfully", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(itemView.getContext(), "Failed to delete leave request", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
