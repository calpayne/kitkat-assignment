package com.kitkat.group.clubs;

import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kitkat.group.clubs.clubs.ViewClubActivity;
import com.kitkat.group.clubs.clubs.events.CreateEventActivity;
import com.kitkat.group.clubs.nfc.subTask;

public class ScanQRCodeActivity extends AppCompatActivity {

    private static final String TAG = "ScanQRCodeActivity";
    private DatabaseReference databaseRef;
    private String clubId, eventId;
    NfcAdapter nfcAdapter;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");

                if (clubId != null) {
                    databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            String isFailure = "true";
                            String userIdContents = null;
                            String userNameContents = null;

                            try {
                                String[] qrContents = contents.split(",");
                                userIdContents = qrContents[0];
                                userNameContents = qrContents[1];
                            } catch (Exception e) {
                                finish();
                            }

                            try {
                                if (userIdContents != null && dataSnapshot.child("clubs-members").child(clubId).child(userIdContents).exists()) {
                                    isFailure = "false";
                                }
                            } catch (Exception e) {}

                            if (eventId != null && isFailure.equalsIgnoreCase("false")) {
                                databaseRef.child("clubs").child(clubId).child("events").child(eventId).child("register").child(userIdContents).setValue(userNameContents);
                            }

                            Intent intent = new Intent(ScanQRCodeActivity.this, VerifyMessageActivity.class);
                            intent.putExtra("failure", isFailure);
                            startActivity(intent);
                            finish();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                            Intent intent = new Intent(ScanQRCodeActivity.this, VerifyMessageActivity.class);
                            intent.putExtra("failure", "true");
                            startActivity(intent);
                            finish();
                        }
                    });
                } else {
                    Intent intent = new Intent(this, ViewClubActivity.class);
                    intent.putExtra("clubId", contents);
                    startActivity(intent);
                    finish();
                }
            }
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "onCreate: started " + TAG);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_qrcode);

        databaseRef = FirebaseDatabase.getInstance().getReference();
        clubId = getIntent().getStringExtra("clubId");
        eventId = getIntent().getStringExtra("eventId");

        try {
            Intent intent = new Intent("com.google.zxing.client.android.SCAN");
            intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
            startActivityForResult(intent, 0);
        } catch (Exception e) {
            Uri marketUri = Uri.parse("market://details?id=com.google.zxing.client.android");
            Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
            startActivity(marketIntent);
        }
    }

    //This this code to stop NFC restarting the app
    public void onResume() {
        super.onResume();
        if(isNfcSupported()) {
            subTask ob=new subTask();
            nfcAdapter=ob.Resume(this,nfcAdapter,new Intent(this,ScanQRCodeActivity.class));
        }
    }
    public void onPause() {
        super.onPause();
        if(isNfcSupported()) {
            subTask ob=new subTask();
            nfcAdapter=ob.Pause(this,nfcAdapter);
        }
    }
    public void onNewIntent(Intent intent) {
        if(isNfcSupported()) {
            if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())) {
                // drop NFC events //No Nothing
                //Makes the activity stay same after NFC intent
            }
        }
    }
    private boolean isNfcSupported() {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        return this.nfcAdapter != null;
    }
    //This this code to stop NFC restarting the app
}