package com.kitkat.group.clubs;

import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Created by Glenn on 17/02/2019.
 */

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";
    private NfcAdapter nfcAdapter;

    public SettingsFragment() {
        // Empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started SettingsFragment");

        View view = inflater.inflate(R.layout.fragment_user_settings, container, false);

        Button btn1 = (Button) view.findViewById(R.id.tos);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ClubListActivity.class);
                startActivity(intent);
            }
        });

        Button nfcbutton = view.findViewById(R.id.nfcbutton);
        nfcbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NfcPermission();

            }
        });

        return view;
    }

    public void NfcPermission(){
        nfcAdapter = NfcAdapter.getDefaultAdapter(getActivity());
        if (!nfcAdapter.isEnabled())
        {
            new AlertDialog.Builder(getActivity())
                    .setTitle("NFC Permission")
                    .setMessage("Go to settings and turn on NFC")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startActivity(new Intent(Settings.ACTION_NFC_SETTINGS));
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .show();

//            Toast.makeText(getApplicationContext(), "Please activate NFC and press Back to return to the application!", Toast.LENGTH_LONG).show();


        }
        if(nfcAdapter.isEnabled()){
            Intent intent = new Intent(getActivity(), SenderActivity.class);
            startActivity(intent);
        }

    }
}