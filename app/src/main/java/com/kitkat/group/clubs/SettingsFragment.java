package com.kitkat.group.clubs;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Glenn on 17/02/2019.
 */

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";

    public SettingsFragment() {
        // Empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started SettingsFragment");

        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);

        return view;
    }
}