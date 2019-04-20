package com.kitkat.group.clubs;

import android.content.Intent;
import android.net.Uri;
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

public class ScanQRCodeActivity extends AppCompatActivity {

    private static final String TAG = "ScanQRCodeActivity";
    private DatabaseReference databaseRef;
    private String clubId;

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
                            try {
                                if (dataSnapshot.child("clubs-members").child(clubId).child(contents).exists()) {
                                    isFailure = "false";
                                }
                            } catch (Exception e) {}

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
}