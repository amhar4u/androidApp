package com.example.lms.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.lms.Fragments.AddleaveFragment;
import com.example.lms.Fragments.HomeFragment;
import com.example.lms.Fragments.LeaveBalanceFragment;
import com.example.lms.Fragments.NotificationFragment;
import com.example.lms.Fragments.ProfileFragment;
import com.example.lms.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Employee extends AppCompatActivity {
       BottomNavigationView bottomNavigationView;

       HomeFragment homeFragment =new HomeFragment();
       NotificationFragment notificationFragment= new NotificationFragment();
       AddleaveFragment addleaveFragment =new AddleaveFragment();
       LeaveBalanceFragment leaveBalanceFragment =new LeaveBalanceFragment();
       ProfileFragment profileFragment =new ProfileFragment();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_employee);

        bottomNavigationView=findViewById(R.id.botnav);

        getSupportFragmentManager().beginTransaction().replace(R.id.container,homeFragment).commit();

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected( MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, homeFragment).commit();
                        return true;
                    case R.id.notification:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, notificationFragment).commit();
                        return true;
                    case R.id.add:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,addleaveFragment).commit();
                        return true;
                    case R.id.report:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,leaveBalanceFragment).commit();
                        return true;
                    case R.id.profile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container,profileFragment).commit();
                        return true;
                }
                return false;
            }
        });

    }
    private static long back_pressed_time;
    private static long PERIOD = 500;

    @Override
    public void onBackPressed() {
        if (back_pressed_time + PERIOD > System.currentTimeMillis()) {
            // If the back button is pressed twice within the specified period, exit the app
            finishAffinity();
            System.exit(0);
        } else {
            // Show a toast to inform the user to press again to exit
            Toast.makeText(this, "Press once again to exit!", Toast.LENGTH_SHORT).show();
        }
        back_pressed_time = System.currentTimeMillis();
    }
}