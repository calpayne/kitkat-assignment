package com.kitkat.group.clubs;

import android.content.Intent;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.kitkat.group.clubs.clubs.events.CreateEventActivity;
import com.kitkat.group.clubs.nfc.subTask;

public class VerifyMessageActivity extends AppCompatActivity {

    NfcAdapter nfcAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_message);

        setStatusBarTransparent();

        boolean isFailure = getIntent().getStringExtra("failure").equalsIgnoreCase("true");

        if (isFailure) {
            ConstraintLayout cl = findViewById(R.id.verifyContainer);
            ImageView iv = findViewById(R.id.verifyImage);
            TextView tv = findViewById(R.id.verifyText);

            cl.setBackgroundColor(getResources().getColor(R.color.colorFailure));
            iv.setImageDrawable(getResources().getDrawable(R.drawable.failure));
            String str="User is not a member";
            tv.setText(str);
        }
    }

    //This this code to stop NFC restarting the app
    public void onResume() {
        super.onResume();
        if(isNfcSupported()) {
            subTask ob=new subTask();
            nfcAdapter=ob.Resume(this,nfcAdapter,new Intent(this,VerifyMessageActivity.class));
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

    private void setStatusBarTransparent(){
        if(Build.VERSION.SDK_INT >= 21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
