package com.kitkat.group.clubs;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Glenn on 13/02/2019.
 */

public class ClubsFragment extends Fragment {

    private static final String TAG = "ClubsFragment";

    public ClubsFragment() {
        // Empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started ClubsFragment");

        View view = inflater.inflate(R.layout.fragment_clubs, container, false);

        Button btn1 = (Button) view.findViewById(R.id.btn_clubs_create);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateClubActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
