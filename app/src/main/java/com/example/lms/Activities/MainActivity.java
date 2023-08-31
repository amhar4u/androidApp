package com.example.lms.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Button;

import com.example.lms.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);

        Checkbox();

        Button btn1=findViewById(R.id.btnlog);
        btn1.setOnClickListener(view -> {
            Intent intent1=new Intent(MainActivity.this, Login.class);
            startActivity(intent1);

        });
        Button btn2=findViewById(R.id.btnreg);
        btn2.setOnClickListener(view -> {
            Intent intent2=new Intent(MainActivity.this, Register.class);
            startActivity(intent2);

        });


    }
    public static final String SHARED_PREFS ="sharedPrefs";
    private void Checkbox() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS,MODE_PRIVATE);
        String check = sharedPreferences.getString("name", "");
        if (check.equals("trueUser")){
            startActivity(new Intent(MainActivity.this, Employee.class));
            finish();
        } else if (check.equals("trueAdmin")) {
            startActivity(new Intent(MainActivity.this, Manager.class));
            finish();

        }
    }
}