package com.kitkat.group.clubs;

import android.*;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import static com.firebase.ui.auth.AuthUI.getApplicationContext;

/**
 * Created by Glenn on 13/02/2019.
 */

public class UserFragment extends Fragment {

    public Button nfcbutton;
    public int NFC_CODE=0;
    private static final String TAG = "UserFragment";
    private NfcAdapter nfcAdapter;

    public UserFragment() {
        // Empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started UserFragment");


        View view = inflater.inflate(R.layout.fragment_user, container, false);

        nfcbutton = view.findViewById(R.id.nfcbutton);
        nfcbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                NfcPermission();

            }
        });
      
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.viewPagerId);
        setupViewPager(viewPager);

        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabsId);
        tabs.setupWithViewPager(viewPager);

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

    private void setupViewPager(ViewPager viewPager) {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getChildFragmentManager());
        adapter.addFragment(new ActivityFragment(), "Activity");
        adapter.addFragment(new SettingsFragment(), "Settings");
        viewPager.setAdapter(adapter);
    }

}

