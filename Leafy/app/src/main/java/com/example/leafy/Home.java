package com.example.leafy;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.navigation.NavigationView;

public class Home extends AppCompatActivity {
    DrawerLayout drawerlayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView bottomNavigationView;
        Toolbar toolbar = findViewById(R.id.toolbar);;
        NavigationView navigationView = findViewById(R.id.navigation);;
        drawerlayout = findViewById(R.id.drawer);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,drawerlayout,toolbar,R.string.OpenDrawer,R.string.CloseDrawer);
        drawerlayout.addDrawerListener(toggle);

        toggle.syncState();
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if(id == R.id.setting){
                    Intent intent = new Intent(Home.this, SettingActivity.class);
                    startActivity(intent);
                } else if(id == R.id.aboutapp){
                    Intent intent = new Intent(Home.this, Aboutus.class);
                    startActivity(intent);
                }else if(id == R.id.logout){
                    // Clear session or user data
                    SharedPreferences preferences = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.clear(); // or editor.remove("key"); if you just want to remove specific data
                    editor.apply();

                    // Redirect to SelectAccountActivity
                    Intent intent = new Intent(Home.this, StartAccount.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK); // clear back stack
                    startActivity(intent);
                    finish();
                }

                drawerlayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.home) {
                    getfrag(new Homeie(),true);
                } else if (id == R.id.my_plants) {
                    getfrag(new GardenFragment(),false);

                } else if (id == R.id.disease) {
                    getfrag(new Diseases(),false);
                } else if (id == R.id.search) {
                    getfrag(new SearchFragment(),false);
                } else if (id == R.id.acc) {
                    getfrag(new Settings(),false);
                }

                return true;
            }
        });
        bottomNavigationView.setSelectedItemId(R.id.home);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.task) {
            // Open TaskActivity
            Intent intent = new Intent(this, TaskActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getfrag(Fragment fragment, boolean flag){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        if (flag)
            ft.add(R.id.container,fragment);
        else
            ft.replace(R.id.container,fragment);
        ft.commit();
    }
    @Override
    public void onBackPressed() {
        if(drawerlayout.isDrawerOpen(GravityCompat.START)){
            drawerlayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();}
    }
    public void loadfrag(Fragment fragment){
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.container, fragment);
        ft.commit();
    }
}