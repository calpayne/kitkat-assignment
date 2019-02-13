package com.kitkat.group.clubs;

<<<<<<< HEAD
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
=======
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
>>>>>>> feature
import android.util.Log;
import android.view.MenuItem;

import com.kitkat.group.clubs.Fragments.ClubsFragment;
import com.kitkat.group.clubs.Fragments.HomeFragment;
import com.kitkat.group.clubs.Fragments.UserFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    final Fragment fragment1 = new HomeFragment();
    final Fragment fragment2 = new ClubsFragment();
    final Fragment fragment3 = new UserFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

<<<<<<< HEAD
        Log.d(TAG, "onCreate: Started MainActivity.");

        BottomNavigationView navigation =  findViewById(R.id.navigation);
=======
        Log.d(TAG, "onCreate: started MainActivity");

        BottomNavigationView navigation = findViewById(R.id.navigation);
>>>>>>> feature
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.fragment_container, fragment3, "3").hide(fragment3).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment2, "2").hide(fragment2).commit();
        fm.beginTransaction().add(R.id.fragment_container, fragment1, "1").commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
<<<<<<< HEAD

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
=======
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()) {
>>>>>>> feature
                case R.id.navigation_home:
                    fm.beginTransaction().hide(active).show(fragment1).commit();
                    active = fragment1;
                    return true;
<<<<<<< HEAD

=======
>>>>>>> feature
                case R.id.navigation_clubs:
                    fm.beginTransaction().hide(active).show(fragment2).commit();
                    active = fragment2;
                    return true;
<<<<<<< HEAD

=======
>>>>>>> feature
                case R.id.navigation_user:
                    fm.beginTransaction().hide(active).show(fragment3).commit();
                    active = fragment3;
                    return true;
            }
            return false;
        }
    };
}
