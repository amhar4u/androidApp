package com.example.lms.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lms.R;
import com.google.firebase.auth.FirebaseAuth;


public class Forgotpassword extends AppCompatActivity {

            private EditText registeredEmail;
            private Button resetButton;
            private FirebaseAuth firebaseAuth;

    @SuppressLint({"WrongViewCast", "MissingInflatedId"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_forgotpassword);

        TextView back=(TextView)findViewById(R.id.back);

        registeredEmail = findViewById(R.id.txtforgotemail);
        resetButton = findViewById(R.id.btnreset);
        firebaseAuth = FirebaseAuth.getInstance();

        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = registeredEmail.getText().toString().trim();

                if (email.isEmpty()) {
                    Toast.makeText(Forgotpassword.this, "Please enter your registered email", Toast.LENGTH_SHORT).show();
                } else {
                    firebaseAuth.sendPasswordResetEmail(email)
                            .addOnCompleteListener(task -> {
                                if (task.isSuccessful()) {
                                    Toast.makeText(Forgotpassword.this, "Password reset email sent", Toast.LENGTH_SHORT).show();
                                    Intent intent=new Intent(Forgotpassword.this, Login.class);
                                    startActivity(intent);
                                } else {
                                    Toast.makeText(Forgotpassword.this, "Failed to send password reset email", Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3=new Intent(Forgotpassword.this,Login.class);
                startActivity(intent3);
            }
        });
    }
}