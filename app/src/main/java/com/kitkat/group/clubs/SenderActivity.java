package com.kitkat.group.clubs;

import android.*;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kitkat.group.clubs.OutcomingNfcManager;


public class SenderActivity extends AppCompatActivity implements OutcomingNfcManager.NfcActivity {

    private TextView tvOutcomingMessage;
    private EditText etOutcomingMessage;
    private Button btnSetOutcomingMessage;

    private NfcAdapter nfcAdapter;
    private OutcomingNfcManager outcomingNfccallback;

    //Runtime permission
    private TextView resultView;
    private View requestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);


//        requestView.setOnClickListener(view -> myMethod());

        if (!isNfcSupported()) {
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_LONG).show();
            finish();
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC disabled on this device. Turn on to proceed", Toast.LENGTH_SHORT).show();
        }

        initViews();

        // encapsulate sending logic in a separate class
        this.outcomingNfccallback = new OutcomingNfcManager(this);
        this.nfcAdapter.setOnNdefPushCompleteCallback(outcomingNfccallback, this);
        this.nfcAdapter.setNdefPushMessageCallback(outcomingNfccallback, this);


    }


    private void initViews() {
        this.tvOutcomingMessage = findViewById(R.id.tv_out_message);
        this.etOutcomingMessage = findViewById(R.id.et_message);
        this.btnSetOutcomingMessage = findViewById(R.id.btn_set_out_message);
        this.btnSetOutcomingMessage.setOnClickListener((v) -> setOutGoingMessage());
    }

    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    private boolean isNfcSupported() {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        return this.nfcAdapter != null;
    }

    private void setOutGoingMessage() {
        String outMessage = this.etOutcomingMessage.getText().toString();
        this.tvOutcomingMessage.setText(outMessage);
    }

    //Screen rotation
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        outState.putString("my_text",tvOutcomingMessage.getText().toString());
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        tvOutcomingMessage.setText(savedInstanceState.getString("my_text"));
    }
    //Screen Rotation

    @Override
    public String getOutcomingMessage() {

        return this.tvOutcomingMessage.getText().toString();
    }

    @Override
    public void signalResult() {
        // this will be triggered when NFC message is sent to a device.
        // should be triggered on UI thread. We specify it explicitly
        // cause onNdefPushComplete is called from the Binder thread
        runOnUiThread(() ->
                Toast.makeText(SenderActivity.this, R.string.message_beaming_complete, Toast.LENGTH_SHORT).show());
    }
}