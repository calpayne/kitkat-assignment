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

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    public Button nfcbutton;

    public HomeFragment() {
        // Empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started HomeFragment");

        View view = inflater.inflate(R.layout.fragment_home, container, false);

        nfcbutton = (Button) view.findViewById(R.id.nfcbutton);
        nfcbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SenderActivity.class);
                //intent.putExtra("text",text);
                startActivity(intent);
            }
        });

        return view;
    }
}
