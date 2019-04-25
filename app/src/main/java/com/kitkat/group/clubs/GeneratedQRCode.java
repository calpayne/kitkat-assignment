package com.kitkat.group.clubs;

import android.content.Intent;
import android.graphics.Bitmap;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;
import com.kitkat.group.clubs.clubs.events.CreateEventActivity;
import com.kitkat.group.clubs.nfc.subTask;

/**
 * Created by Glenn on 14/03/2019.
 */

public class GeneratedQRCode extends AppCompatActivity {

    private ImageView generatedQRCode;
    private TextView setClubName;
    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_generated_qr_code);

        generatedQRCode = findViewById(R.id.generatedQRCode);
        String clubId = getIntent().getStringExtra("clubId");

        setClubName = findViewById(R.id.club_name);
        String clubName = getIntent().getStringExtra("clubName");
        setClubName.setText(clubName);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();

        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(clubId, BarcodeFormat.QR_CODE, 500, 500);
            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            generatedQRCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    //This this code to stop NFC restarting the app
    public void onResume() {
        super.onResume();
        if(isNfcSupported()) {
            subTask ob=new subTask();
            nfcAdapter=ob.Resume(this,nfcAdapter,new Intent(this,GeneratedQRCode.class));
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
