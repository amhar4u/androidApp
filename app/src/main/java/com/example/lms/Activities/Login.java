package com.example.lms.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lms.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
 Button log;
 EditText user,pword;
 boolean passVisible;
 public static final String SHARED_PREFS ="sharedPrefs";
 FirebaseAuth mAuth;
 FirebaseUser mUser;

    private ProgressDialog progressDialog;


// DBHelper db;
//    private  DBHelper databaseHelper;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);



        user=(EditText)findViewById(R.id.txtmail) ;
        pword=(EditText) findViewById(R.id.txtpass);
        log=(Button) findViewById(R.id.btnlogin) ;
        mAuth=FirebaseAuth.getInstance();
        mUser=mAuth.getCurrentUser();
        CheckBox checkBox=findViewById(R.id.checkBox);
        TextView forgot=(TextView)findViewById(R.id.txtForgotPassword);




        log.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String mail=user.getText().toString();
                String pass=pword.getText().toString();



               if(TextUtils.isEmpty(mail)){
                   Toast.makeText(Login.this,"Enter The Email",Toast.LENGTH_SHORT).show();
                }
               else if(TextUtils.isEmpty(pass)){
                   Toast.makeText(Login.this,"Enter The Password",Toast.LENGTH_SHORT).show();
                } else if (pass.length()<6) {

                   Toast.makeText(Login.this,"Password Must Be 6 Characters Or More",Toast.LENGTH_SHORT).show();
               } else
                {
                    showProgressDialog("Loading...");
                     mAuth.signInWithEmailAndPassword(mail,pass).addOnCompleteListener( (task )-> {
                         if (task.isSuccessful()){
                               if(mail.equals("manager@gmail.com")){
                                   if(checkBox.isChecked()){
                                       SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                       SharedPreferences.Editor editor = sharedPreferences.edit();
                                       editor.putString("name", "trueAdmin");
                                       editor.apply();
                                   }
                                   Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent7 = new Intent(getApplicationContext(), Manager.class);
                                        startActivity(intent7);
                                        dismissProgressDialog();
                               }
                               else{
                                   if(checkBox.isChecked()){
                                       SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                                       SharedPreferences.Editor editor = sharedPreferences.edit();
                                       editor.putString("name", "trueUser");
                                       editor.apply();
                                   }
                                   Toast.makeText(Login.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent6 = new Intent(getApplicationContext(), Employee.class);
                                        startActivity(intent6);
                                        dismissProgressDialog();
                               }

                         }
                         else {
                             dismissProgressDialog();
                             Toast.makeText(Login.this, "Login Failed Check Email and Password ", Toast.LENGTH_SHORT).show();

                         }
                     });

               }
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3=new Intent(Login.this, Forgotpassword.class);
                startActivity(intent3);
            }
        });
        TextView textView1=(TextView) findViewById(R.id.txtregister);
        textView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2=new Intent(Login.this, Register.class);
                startActivity(intent2);
            }
        });

    }
    private static long back_pressed_time;
    private static long PERIOD = 2000;

    @Override
    public void onBackPressed()
    {
//        if (back_pressed_time + PERIOD > System.currentTimeMillis()) super.onBackPressed();
//        else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
//        back_pressed_time = System.currentTimeMillis();
    }
    @Override
    protected void onResume() {
        super.onResume();
        user.setText("");
        pword.setText("");
    }

    private void showProgressDialog(String message) {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(Login.this);
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