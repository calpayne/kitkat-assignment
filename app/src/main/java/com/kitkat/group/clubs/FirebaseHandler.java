package com.kitkat.group.clubs;

import android.app.Application;

import com.google.firebase.database.FirebaseDatabase;

/**
 * Created by Admin on 21/04/2019.
 */

public class FirebaseHandler extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

}