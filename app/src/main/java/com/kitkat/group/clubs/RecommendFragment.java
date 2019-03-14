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

public class RecommendFragment extends Fragment {

    private static final String TAG = "RecommendFragment";

    public RecommendFragment() {
        // Empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started RecommendFragment");

        View view = inflater.inflate(R.layout.fragment_home_recommended, container, false);

        return view;
    }
}