package com.kitkat.group.clubs;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static android.app.Activity.RESULT_OK;

/**
 * Created by Glenn on 13/02/2019.
 */

public class ClubsFragment extends Fragment {

    private static final String TAG = "ClubsFragment";

    public static final int REQUEST_CODE = 100;
    public static final int PERMISSIONS_REQUESTED = 200;

    EditText searchClub;

    DatabaseReference databaseReference;

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

        Button btn2 = (Button) view.findViewById(R.id.btn_test_profile);
        btn2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getActivity(), ViewClubActivity.class);
                intent.putExtra("clubId","d57561cb-f66b-4263-88d8-800d5d84b341");
                startActivity(intent);
            }
        });

        searchClub = (EditText) view.findViewById(R.id.searchClub);
        Button btn3 = (Button) view.findViewById(R.id.btn_scan_qr);
        if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.CAMERA}, PERMISSIONS_REQUESTED);
        }
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), ScanQRCodeActivity.class);
                startActivityForResult(intent, REQUEST_CODE);
            }
        });

        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d(TAG, "onActivityResult: started onActivityResult");
        if(requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
            if(data != null) {
                final Barcode barcode = data.getParcelableExtra("barcode");

                Intent intent = new Intent(getActivity(), ViewClubActivity.class);
                intent.putExtra("clubId", barcode.displayValue);
                getActivity().startActivity(intent);
                getActivity().finish();

                /**
                searchClub.post(new Runnable() {
                    @Override
                    public void run(){
                        searchClub.setText(barcode.displayValue);
                    }
                }); */
            }
        }
    }
}