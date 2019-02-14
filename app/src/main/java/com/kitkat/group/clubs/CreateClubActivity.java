package com.kitkat.group.clubs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class CreateClubActivity extends AppCompatActivity {

    private static final String TAG = "CreateClubActivity";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);

        Log.d(TAG, "onCreate: started CreateClubActivity");
    }
}
