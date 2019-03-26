package com.kitkat.group.clubs;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

/**
 * Created by Glenn on 17/02/2019.
 * (Do not remove)
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

        view.findViewById(R.id.btn_user_terms).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Terms and Conditions", Toast.LENGTH_SHORT).show();
            }
        });

        view.findViewById(R.id.btn_user_delete_account).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Delete Account", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
