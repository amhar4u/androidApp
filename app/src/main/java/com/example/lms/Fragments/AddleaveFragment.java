package com.example.lms.Fragments;

import static com.example.lms.R.id.spinnertype;
import static com.example.lms.R.id.txtdays;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lms.Activities.Employee;
import com.example.lms.Classes.Leave;
import com.example.lms.Classes.LeaveRequest;
import com.example.lms.Classes.Notification;
import com.example.lms.Classes.userNotification;
import com.example.lms.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;


public class AddleaveFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    long daysCount;
    TextView sdate, enddate, days;
    private DatabaseReference notificationRef;
    private DatabaseReference usernotificationRef;

    private DatabaseReference leaveRef;
    String profileurl;

    String eid;
    String imgnopay="https://firebasestorage.googleapis.com/v0/b/lmsdb-988a7.appspot.com/o/nopay.png?alt=media&token=b4cb7881-fef2-4eff-83f1-0d299ff3d34b";
    String imgsuccsess="https://firebasestorage.googleapis.com/v0/b/lmsdb-988a7.appspot.com/o/succsess.png?alt=media&token=b01ecfe1-66e4-440b-bc77-e2d464d7fa65";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_addleave, container, false);


        Spinner spinner = (Spinner) view.findViewById(spinnertype);
        sdate = (TextView) view.findViewById(R.id.txtshowdate);
        enddate = (TextView) view.findViewById(R.id.txtenddate);
        days = (TextView) view.findViewById(txtdays);
        Button add = (Button) view.findViewById(R.id.btnapply);
        notificationRef = FirebaseDatabase.getInstance().getReference("manager_notification");
        usernotificationRef = FirebaseDatabase.getInstance().getReference("user_notification");


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),
                R.array.type,
                android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);


        sdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                sdate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                updateDaysCount();
                            }
                        },
                        year, month, day);

                // Set the minimum date as today's date
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());

                datePickerDialog.show();
            }
        });

        enddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();

                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int monthOfYear, int dayOfMonth) {
                                enddate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);
                                updateDaysCount();
                            }
                        },
                        year, month, day);

                // Set the minimum date for the end date as one day after the selected start date
                String startDateString = sdate.getText().toString();
                SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
                try {
                    Date startDate = format.parse(startDateString);
                    c.setTime(startDate);
                    c.add(Calendar.DAY_OF_MONTH, 1); // Add one day
                    datePickerDialog.getDatePicker().setMinDate(c.getTimeInMillis());
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                datePickerDialog.show();
            }
        });


        FirebaseAuth mAuth = FirebaseAuth.getInstance();


        String currentUserId = mAuth.getCurrentUser().getUid();


        DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");


        usersRef.child(currentUserId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {

                    profileurl = dataSnapshot.child("profilepic").getValue(String.class);
                } else {
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = firebaseAuth.getCurrentUser();

        if (currentUser != null) {
            currentUserId = currentUser.getUid();
            DatabaseReference userRef = FirebaseDatabase.getInstance().getReference("Users").child(currentUserId);
            userRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    if (snapshot.exists()) {
                        eid = snapshot.child("empid").getValue(String.class);
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String type = spinner.getSelectedItem().toString();
                String strtdate = sdate.getText().toString();
                String edate = enddate.getText().toString();

                if (type.equals("Select Leave Type")) {
                    Toast.makeText(getActivity(), "Select the Leave type", Toast.LENGTH_SHORT).show();
                } else {
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

                    DatabaseReference usersRef = database.getReference("Users").child(userId);
                    usersRef.get().addOnSuccessListener(snapshot -> {
                        String empid = snapshot.child("empid").getValue(String.class);

                        leaveRef = database.getReference("leave_requests");
                        DatabaseReference newLeaveRef = leaveRef.push();
                        String lid = newLeaveRef.getKey();

                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                        String currentTime = dateFormat.format(new Date());

                        Leave leave = new Leave(lid, userId, type, strtdate, edate, daysCount, "not approved", empid,currentTime);
                        newLeaveRef.setValue(leave, (error, ref) -> {
                            if (error == null) {
                                Toast.makeText(AddleaveFragment.this.getActivity(), "Leave Added Successfully", Toast.LENGTH_SHORT).show();

                                String subject = empid + " Send a Leave Request";
                                DatabaseReference newNotRef = notificationRef.push();
                                String id = newNotRef.getKey();
                                Notification notification = new Notification("Leave Request", getCurrentDate(), getCurrentTime(), profileurl, id, subject, "unread");
                                newNotRef.setValue(notification);

                                String subjct =  "Leave Requested Successfully";
                                DatabaseReference newuserNotRef = usernotificationRef.push();
                                String nid = newuserNotRef.getKey();
                                userNotification Notification = new userNotification(nid,"Leave Request", getCurrentDate(), getCurrentTime(),imgsuccsess,eid, subjct, "unread");
                                newuserNotRef.setValue(Notification);

                                DatabaseReference leaveRef = FirebaseDatabase.getInstance().getReference("leave_requests");
                                Query query = leaveRef.orderByChild("empid").equalTo(eid);

                                query.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        long totalLeaveDays = 0;
                                        for (DataSnapshot leaveSnapshot : snapshot.getChildren()) {
                                            LeaveRequest leave = leaveSnapshot.getValue(LeaveRequest.class);
                                            if (leave != null) {
                                                totalLeaveDays += leave.getDays();
                                            }
                                        }

                                        if (totalLeaveDays > 50) {

                                            String subjct =  "Your Leave Balance finished";
                                            DatabaseReference newuserNotRef = usernotificationRef.push();
                                            String nid = newuserNotRef.getKey();
                                            userNotification Notification = new userNotification(nid,"Nopay Leave", getCurrentDate(), getCurrentTime(),imgnopay,eid, subjct, "unread");
                                            newuserNotRef.setValue(Notification);

                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        Log.e("LeaveDays", "Failed to retrieve leave requests data: " + error.getMessage());
                                    }
                                });

                                Intent intent2 = new Intent(AddleaveFragment.this.getActivity(), Employee.class);
                                startActivity(intent2);
                            } else {
                                System.err.println("Error adding leave: " + error.getMessage());
                            }
                        });
                    });
                }
            }
        });

        return view;
    }

    private String getCurrentDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        return sdf.format(new Date());
    }

    private String getCurrentTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return sdf.format(new Date());
    }

    // Method to update the days count when the start or end date is selected
    private void updateDaysCount() {
        String startDateString = sdate.getText().toString();
        String endDateString = enddate.getText().toString();

        if (!startDateString.isEmpty() && !endDateString.isEmpty()) {
            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            try {
                Date startDate = format.parse(startDateString);
                Date endDate = format.parse(endDateString);
                long diffInMillis = endDate.getTime() - startDate.getTime();
                daysCount = TimeUnit.MILLISECONDS.toDays(diffInMillis);

                // Set the calculated days count to the "days" TextView
                days.setText(String.valueOf(daysCount));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        // The existing code...
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        // The existing code...
    }
}