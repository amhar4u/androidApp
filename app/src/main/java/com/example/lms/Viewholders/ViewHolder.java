package com.example.lms.Viewholders;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lms.Classes.Item;
import com.example.lms.Classes.userNotification;
import com.example.lms.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ViewHolder extends RecyclerView.ViewHolder {

//    public String userid;
   String imageapprove = "https://firebasestorage.googleapis.com/v0/b/lmsdb-988a7.appspot.com/o/approve.jpeg?alt=media&token=1f31311b-81f1-4f5e-9cff-fcfd50bd9841";
    String imagereject="https://firebasestorage.googleapis.com/v0/b/lmsdb-988a7.appspot.com/o/reject.png?alt=media&token=1daec2de-a20a-4a66-b68f-4ef29b9bac50";
    public TextView showtype;
    public TextView showsdate;
    public TextView showedate;
    public TextView showdays;
    public TextView showempid;
    Button approve, reject;
    DatabaseReference leaveRequestsRef;
    DatabaseReference notificationRef;
    List<Item> items;

    public ViewHolder(@NonNull View itemView, List<Item> items, String userId) {
        super(itemView);
        showempid = itemView.findViewById(R.id.txtshowempid);
        showtype = itemView.findViewById(R.id.txtshowtype);
        showsdate = itemView.findViewById(R.id.txtshowsdate);
        showedate = itemView.findViewById(R.id.txtshowedate);
        showdays = itemView.findViewById(R.id.txtshowdays);
        approve = itemView.findViewById(R.id.btnapprove);
        reject = itemView.findViewById(R.id.btnreject);
        this.items = items;
        notificationRef = FirebaseDatabase.getInstance().getReference("user_notification");

        leaveRequestsRef = FirebaseDatabase.getInstance().getReference("leave_requests");



        approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Item item = items.get(position);
                    String lid = item.getLid();

                    // Update the status to "approved" in the database
                    leaveRequestsRef.child(lid).child("status").setValue("approved")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(itemView.getContext(), "Approved successfully", Toast.LENGTH_SHORT).show();

                                    String subject =  "Your Leave Request is Approved";
                                    DatabaseReference newNotRef = notificationRef.push();
                                    String id = newNotRef.getKey();
                                    userNotification notification = new userNotification(id,"Leave Request", getCurrentDate(), getCurrentTime(),imageapprove,  item.getEmpid(), subject, "unread");
                                    newNotRef.setValue(notification);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(itemView.getContext(), "Failed to approve", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Item item = items.get(position);
                    String lid = item.getLid();

                    // Update the status to "approved" in the database
                    leaveRequestsRef.child(lid).child("status").setValue("reject")
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast.makeText(itemView.getContext(), "Reject successfully", Toast.LENGTH_SHORT).show();

                                    String subject =  "Your Leave Request is Rejected";
                                    DatabaseReference newNotRef = notificationRef.push();
                                    String id = newNotRef.getKey();
                                    userNotification notification = new userNotification(id,"Leave Request", getCurrentDate(), getCurrentTime(),imagereject,  item.getEmpid(), subject, "unread");
                                    newNotRef.setValue(notification);
                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(itemView.getContext(), "Failed to Reject", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }
}
