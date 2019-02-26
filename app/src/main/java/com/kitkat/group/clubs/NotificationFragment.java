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

public class NotificationFragment extends Fragment {

    private static final String TAG = "NotificationFragment";

    public NotificationFragment() {
        // Empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started NotificationFragment");

        View view = inflater.inflate(R.layout.fragment_home_notification, container, false);

        return view;
    }
}