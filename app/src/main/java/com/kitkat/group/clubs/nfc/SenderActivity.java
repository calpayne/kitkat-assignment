package com.kitkat.group.clubs.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.kitkat.group.clubs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kitkat.group.clubs.nfc.OutcomingNfcManager;
import com.kitkat.group.clubs.data.Club;


public class SenderActivity extends AppCompatActivity implements OutcomingNfcManager.NfcActivity {

    //private TextView tvOutcomingMessage;
    private Button btnSetOutcomingMessage;

    private NfcAdapter nfcAdapter;
    private OutcomingNfcManager outcomingNfccallback;




    String clubId, userId, clubName, outMessage, userName;
    TextView tvClubName, tvClubId, tvUserId, tvUserName, textView;

    //Runtime permission
    private TextView resultView;
    private View requestView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);

        clubName = getIntent().getStringExtra("clubName");
        clubId = getIntent().getStringExtra("clubId");
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");
        //userId = getIntent().getStringExtra("userId");


//        requestView.setOnClickListener(view -> myMethod());

        if (!isNfcSupported()) {
            Toast.makeText(this, "Nfc is not supported on this device", Toast.LENGTH_LONG).show();
            finish();
        }
        if (!nfcAdapter.isEnabled()) {
            Toast.makeText(this, "NFC disabled on this device. Turn on to proceed", Toast.LENGTH_SHORT).show();
        }

        initViews();

        tvClubName = findViewById(R.id.tv_clubName);
        tvClubId = findViewById(R.id.tv_clubId);
        tvUserId = findViewById(R.id.tv_userId);
        tvUserName = findViewById(R.id.tv_userName);
        textView = findViewById(R.id.textView7);

        tvClubName.setText(clubName);
        tvClubId.setText(clubId);
        tvUserId.setText(userId);
        tvUserName.setText(userName);


        // encapsulate sending logic in a separate class
        this.outcomingNfccallback = new OutcomingNfcManager(this);
        this.nfcAdapter.setOnNdefPushCompleteCallback(outcomingNfccallback, this);
        this.nfcAdapter.setNdefPushMessageCallback(outcomingNfccallback, this);

    }


    private void initViews() {
        //this.tvOutcomingMessage = findViewById(R.id.tv_out_message);
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
        Toast.makeText(this, "Details verified! Tap on NFC device.", Toast.LENGTH_SHORT).show();
        outMessage = clubName+"*"+clubId+"*"+userId+"*"+userName;
        //textView.setText(outMessage);
        //this.tvOutcomingMessage.setText(outMessage);
    }

    /*Screen rotation*******************
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
    Screen Rotation****************************/

    @Override
    public String getOutcomingMessage() {

        return outMessage;
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
