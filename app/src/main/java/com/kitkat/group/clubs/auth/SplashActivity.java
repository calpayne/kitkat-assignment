package com.kitkat.group.clubs.auth;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kitkat.group.clubs.MainActivity;
import com.kitkat.group.clubs.R;
import com.kitkat.group.clubs.data.ClubUser;

public class SplashActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private long time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        time = System.currentTimeMillis();
        databaseRef = FirebaseDatabase.getInstance().getReference();

        Thread myThread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1500);
                    redirect();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();
    }

    @Override
    public void onBackPressed() {
            if(System.currentTimeMillis() > time+2000){
                super.onBackPressed();
                return;
            }
        }
    private void redirect() {
        if (FirebaseAuth.getInstance().getCurrentUser() != null) {
            final FirebaseUser fa = FirebaseAuth.getInstance().getCurrentUser();

            databaseRef.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    DataSnapshot ds = dataSnapshot.child(fa.getUid());
                    Intent intent;

                    if (ds.exists() && ds.getValue(String.class) != null) {
                        ClubUser.getInstance().setUsername(ds.getValue(String.class));
                        intent = new Intent(getApplicationContext(), MainActivity.class);
                    } else {
                        intent = new Intent(getApplicationContext(), RegisterActivity.class);
                    }

                    startActivity(intent);
                    finish();
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        } else {
            Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
