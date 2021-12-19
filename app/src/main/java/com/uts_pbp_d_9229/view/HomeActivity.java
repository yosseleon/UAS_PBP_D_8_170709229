package com.uts_pbp_d_9229.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.uts_pbp_d_9229.R;

import android.os.Bundle;
import android.view.MenuItem;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener{

    BottomNavigationView bottomNavigationView;
    HomeFragment homeFragment           = new HomeFragment();
    CategoryFragment categoryFragment   = new CategoryFragment();
    PostFragment postFragment           = new PostFragment();
    ProfileFragment profileFragment     = new ProfileFragment();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(this);
        bottomNavigationView.setSelectedItemId(R.id.home);
        loadFragment(homeFragment);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.home:
                loadFragment(homeFragment);
                return true;
            case R.id.post:
                loadFragment(postFragment);
                return true;
            case R.id.category:
                loadFragment(categoryFragment);
                return true;
            case R.id.profile:
                loadFragment(profileFragment);
                return true;
        }
        return false;
    }

    public void loadFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
