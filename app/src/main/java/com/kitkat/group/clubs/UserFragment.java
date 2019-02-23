package com.kitkat.group.clubs;

import android.*;
import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Glenn on 13/02/2019.
 */

public class UserFragment extends Fragment {


    public Button nfcbutton;
    private static final String TAG = "UserFragment";


    public UserFragment() {
        // Empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started UserFragment");


        View view = inflater.inflate(R.layout.fragment_user, container, false);


        nfcbutton = view.findViewById(R.id.nfcbutton);


        //Handling button press
        nfcbutton.setOnClickListener(v -> {


            //Checking for runtime permisssion
            if(ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.NFC) != PackageManager.PERMISSION_GRANTED) {



                //TO DO: alter box (for user)



                //Setting permissions
                String[] permissions = new String[1];
                permissions[0] = Manifest.permission.NFC;


                //Requesting permission
                ActivityCompat.requestPermissions(getActivity(), permissions, 1 );



            } else {


                //Switching from this fragment to Sender Activity
                Intent intent = new Intent(getActivity(), SenderActivity.class);
                startActivity(intent);


            }


        });


        return view;
    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        //Handling the runtime permission
        String permission = permissions[0];
        if(android.Manifest.permission.NFC.equals(permission)) {
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                //Switching from this fragment to Sender Activity
                Intent intent = new Intent(getActivity(), SenderActivity.class);
                startActivity(intent);


            }
        }



        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



}
