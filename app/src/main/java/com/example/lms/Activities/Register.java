package com.example.lms.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lms.Classes.Notification;
import com.example.lms.Classes.Users;
import com.example.lms.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Register extends AppCompatActivity {
    EditText id, mail, pass, repass, name;
    boolean passVisible;
    Button regster;
    FirebaseAuth mAuth;
    private ProgressDialog progressDialog;

    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference notificationRef;

    private final String profileurl = "https://firebasestorage.googleapis.com/v0/b/lmsdb-988a7.appspot.com/o/download.jpeg?alt=media&token=8fbf419c-68db-404e-8b53-c09c0ed839cb";
    private String contact = "Contact";
    private String address = "Address";

    private final String empidPattern = "IT-\\d{6}";

    //      DBHelper db;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        setContentView(R.layout.activity_register);
        id = (EditText) findViewById(R.id.txtemployeeid);
        name = (EditText) findViewById(R.id.txtempid);
        mail = (EditText) findViewById(R.id.txtemail);
        pass = (EditText) findViewById(R.id.txtpassword);
        repass = (EditText) findViewById(R.id.txtcpassword);
        regster = (Button) findViewById(R.id.btnregister);
        mAuth = FirebaseAuth.getInstance();
        notificationRef = FirebaseDatabase.getInstance().getReference("manager_notification");


        regster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String empid = id.getText().toString();
                String username = name.getText().toString();
                String email = mail.getText().toString();
                String paswd = pass.getText().toString();
                String repaswd = repass.getText().toString();

                if (!empid.matches(empidPattern)) {
                    Toast.makeText(Register.this, "Invalid empid format. Please use IT-XXXXX pattern.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!paswd.equals(repaswd)) {
                    Toast.makeText(Register.this, "Password does not match", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(username) || TextUtils.isEmpty(email) || TextUtils.isEmpty(paswd) || TextUtils.isEmpty(repaswd)) {
                    Toast.makeText(Register.this, "Please enter all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                DatabaseReference usersRef = FirebaseDatabase.getInstance().getReference("Users");

                // Check if empid already exists
                usersRef.orderByChild("empid").equalTo(empid).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists()) {
                            // empid already exists
                            Toast.makeText(Register.this, "User already exists with this empid", Toast.LENGTH_SHORT).show();
                        } else {
                            // Check if email already exists
                            usersRef.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.exists()) {
                                        // email already exists
                                        Toast.makeText(Register.this, "User already exists with this email", Toast.LENGTH_SHORT).show();
                                    } else {

                                        showProgressDialog("Loading...");
                                        // User does not exist, proceed with registration
                                        mAuth.createUserWithEmailAndPassword(email, paswd).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                            @Override
                                            public void onComplete(@NonNull Task<AuthResult> task) {
                                                if (task.isSuccessful()) {
                                                    String uid = mAuth.getCurrentUser().getUid();

                                                    Users users = new Users(uid, username, email, paswd, profileurl, contact, address, empid);
                                                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
                                                    ref.child(uid).setValue(users);

                                                    String subject = empid + " is Registered to LMS";
                                                    DatabaseReference newNotRef = notificationRef.push();
                                                    String id = newNotRef.getKey();
                                                    Notification notification = new Notification("Registration", getCurrentDate(), getCurrentTime(), profileurl, id, subject, "unread");
                                                    newNotRef.setValue(notification);

                                                    Toast.makeText(Register.this, "Registered Successfully. You can now login.", Toast.LENGTH_SHORT).show();
                                                    Intent intent2 = new Intent(Register.this, Login.class);
                                                    startActivity(intent2);
                                                    finish();
                                                    dismissProgressDialog();
                                                } else {
                                                    Toast.makeText(Register.this, "Registration Failed", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    Toast.makeText(Register.this, "Failed to check email", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(Register.this, "Failed to check empid", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });


        pass.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= pass.getRight() - pass.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection = pass.getSelectionEnd();

                        if (passVisible) {
                            pass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0);

                            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passVisible = false;
                        } else {
                            pass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24, 0);

                            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passVisible = true;
                        }
                        pass.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });


        repass.setOnTouchListener(new View.OnTouchListener() {
            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                final int Right = 2;
                if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    if (motionEvent.getRawX() >= repass.getRight() - repass.getCompoundDrawables()[Right].getBounds().width()) {
                        int selection = repass.getSelectionEnd();

                        if (passVisible) {
                            repass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_off_24, 0);

                            repass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                            passVisible = false;
                        } else {
                            repass.setCompoundDrawablesRelativeWithIntrinsicBounds(0, 0, R.drawable.baseline_visibility_24, 0);

                            repass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                            passVisible = true;
                        }
                        repass.setSelection(selection);
                        return true;
                    }
                }
                return false;
            }
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView textView2 = (TextView) findViewById(R.id.txtlogin);
        textView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Register.this, Login.class);
                startActivity(intent1);
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
    private void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(Register.this);
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