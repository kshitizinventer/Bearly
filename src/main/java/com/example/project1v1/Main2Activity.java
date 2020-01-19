package com.example.project1v1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Switch;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Main2Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        BottomNavigationView bottomnav = findViewById(R.id.bottom_navigation);
        bottomnav.setOnNavigationItemSelectedListener(navlistner);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new profileFragment()).commit();

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navlistner =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    Fragment selectedfragment = null;

                    switch (menuItem.getItemId())
                    {
                        case R.id.nav_profile :
                            selectedfragment = new profileFragment();
                            break;
                        case R.id.nav_like :
                            selectedfragment = new discoverFragment();
                            break;
                        case R.id.nav_crushes :
                            selectedfragment = new crushFragment();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedfragment).commit();



                    return true;
                }
            };
}
