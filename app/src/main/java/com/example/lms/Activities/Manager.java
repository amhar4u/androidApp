package com.example.lms.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.lms.Fragments.DashFragment;
import com.example.lms.Fragments.LeaveapproveFragment;
import com.example.lms.Fragments.ManagerprofileFragment;
import com.example.lms.Fragments.RegisterapproveFragment;
import com.example.lms.Fragments.ReportFragment;
import com.example.lms.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class Manager extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;

    DashFragment dashFragment = new DashFragment();

    RegisterapproveFragment registerapproveFragment =new RegisterapproveFragment();

    LeaveapproveFragment leaveapproveFragment = new LeaveapproveFragment();

    ReportFragment reportFragment = new ReportFragment();

    ManagerprofileFragment managerprofileFragment = new ManagerprofileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(getWindow().FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_manager);

        bottomNavigationView=findViewById(R.id.bottomnav);
        getSupportFragmentManager().beginTransaction().replace(R.id.container,dashFragment).commit();
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.dashboard :
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, dashFragment).commit();
                        return true;
                    case R.id.approvereg:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, registerapproveFragment).commit();
                        return true;
                    case R.id.approveleave:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, leaveapproveFragment).commit();
                        return true;
                    case R.id.report:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, reportFragment).commit();
                        return true;
                    case R.id.managerprofile:
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, managerprofileFragment).commit();
                        return true;
                }
                return false;
            }
        });
    }
    private static long back_pressed_time;
    private static long PERIOD = 500;

    @Override
    public void onBackPressed()
    {
        if (back_pressed_time + PERIOD > System.currentTimeMillis()) super.onBackPressed();
        else Toast.makeText(getBaseContext(), "Press once again to exit!", Toast.LENGTH_SHORT).show();
        back_pressed_time = System.currentTimeMillis();
    }
}