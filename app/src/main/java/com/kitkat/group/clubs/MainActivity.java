package com.kitkat.group.clubs;

import android.app.PendingIntent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;

import android.content.Intent;
import android.view.View;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.kitkat.group.clubs.auth.LoginActivity;
import com.kitkat.group.clubs.clubs.ClubsFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    final Fragment homeFragment = new HomeFragment();
    final Fragment clubsFragment = new ClubsFragment();
    final Fragment userFragment = new UserFragment();
    final FragmentManager fm = getSupportFragmentManager();
    Fragment active = homeFragment;
    private NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: Started MainActivity.");

        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fm.beginTransaction().add(R.id.fragment_container, userFragment, "3").hide(userFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, clubsFragment, "2").hide(clubsFragment).commit();
        fm.beginTransaction().add(R.id.fragment_container, homeFragment, "1").commit();
    }

    public void onResume() {
        super.onResume();
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
        nfcAdapter.enableForegroundDispatch(this, pendingIntent, null, null);
    }
    public void onPause() {
        super.onPause();
        NfcAdapter nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        nfcAdapter.disableForegroundDispatch(this);
    }

    public void onNewIntent(Intent intent) {
        if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
            // drop NFC events
        }
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch(item.getItemId()) {
                case R.id.navigation_home:
                    fm.beginTransaction().hide(active).show(homeFragment).commit();
                    active = homeFragment;
                    return true;
                case R.id.navigation_clubs:
                    fm.beginTransaction().hide(active).show(clubsFragment).commit();
                    active = clubsFragment;
                    return true;
                case R.id.navigation_user:
                    fm.beginTransaction().hide(active).show(userFragment).commit();
                    active = userFragment;
                    return true;
            }
            return false;
        }
    };
  
    public void logout(View view) {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    public void onComplete(@NonNull Task<Void> task) {
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
    }
}
