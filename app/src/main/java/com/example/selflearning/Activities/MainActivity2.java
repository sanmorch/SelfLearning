package com.example.selflearning.Activities;

import static com.example.selflearning.Constant.SHARED_PREFS;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.selflearning.Fragments.AboutUsFragment;
import com.example.selflearning.Fragments.FirstCourseFragment;
import com.example.selflearning.Fragments.FourthCourseFragment;
import com.example.selflearning.Fragments.HomeFragment;
import com.example.selflearning.R;
import com.example.selflearning.Fragments.SecondCourseFragment;
import com.example.selflearning.Fragments.SettingsFragment;
import com.example.selflearning.Fragments.ThirdCouseFragment;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainActivity2 extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new HomeFragment()).commit();
                break;

            case R.id.nav_first_course:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FirstCourseFragment()).commit();
                break;

            case R.id.nav_second_course:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SecondCourseFragment()).commit();
                break;

            case R.id.nav_third_course:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ThirdCouseFragment()).commit();
                break;

            case R.id.nav_fourth_course:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new FourthCourseFragment()).commit();
                break;

            case R.id.nav_settings:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new SettingsFragment()).commit();
                break;

            case R.id.nav_about_us:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new AboutUsFragment()).commit();
                break;

            case R.id.nav_log_out:
                // for not login user automatically
                sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("name", "false");
                editor.apply();

                // go to the login page
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);



                //Toast.makeText(this, "ЗАГЛУШКА ДЛЯ ВЫХОДА ИЗ АККАУНТА", Toast.LENGTH_SHORT).show();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    protected void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav, R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

    }
}