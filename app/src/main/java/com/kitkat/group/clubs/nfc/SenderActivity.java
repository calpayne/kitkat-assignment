package com.kitkat.group.clubs.nfc;

import android.content.Intent;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kitkat.group.clubs.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.kitkat.group.clubs.data.Club;
import com.squareup.picasso.Picasso;


public class SenderActivity extends AppCompatActivity implements OutcomingNfcManager.NfcActivity {

    //private TextView tvOutcomingMessage;
    Button btnSetOutcomingMessage;
    private NfcAdapter nfcAdapter;
    OutcomingNfcManager outcomingNfccallback;
    String clubId, userId, clubName, outMessage, userName, clubNameRec;
    TextView tvClubName, tvClubId, tvUserId, tvUserName;
    //Runtime permission
    private TextView resultView;
    private View requestView;

    //Firebase data fetching
    private DatabaseReference db;
    private StorageReference storageRef;
    private Club club;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sender);

        Toolbar toolbar = findViewById(R.id.toolbar5);
        setSupportActionBar(toolbar);

//        db = FirebaseDatabase.getInstance().getReference();
//        mAuth=FirebaseAuth.getInstance();
//        userId=mAuth.getCurrentUser().getUid();
//        mDatabase = FirebaseDatabase.getInstance().getReference();
//        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                userName = String.valueOf(snapshot.child(userId).getValue());
//            }
//            @Override
//            public void onCancelled(@NonNull DatabaseError databaseError) {
//            }
//        });
//
//        clubName=club.getClubName();
//        clubId=club.getClubID();

        clubName = getIntent().getStringExtra("clubName");
        clubId = getIntent().getStringExtra("clubId");
        clubNameRec=getIntent().getStringExtra("clubName");
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");

        Intent intent = new Intent(SenderActivity.this, ReceiverActivity.class);
        intent.putExtra("clubNameRec", clubNameRec);

        StorageReference storageRef = FirebaseStorage.getInstance().getReference("member-avatars");
        final ImageView imageView = findViewById(R.id.nfc_user_image);
        try {
            storageRef.child(userId).getDownloadUrl().addOnSuccessListener(uri -> {
                if (uri != null) {
                    Picasso.with(SenderActivity.this).load(uri).into(imageView);
                }
            });
        } catch (Exception ignored) {

        }


        StorageReference storageRefClub = FirebaseStorage.getInstance().getReference("club-logos");
        final ImageView imageViewClub = findViewById(R.id.nfc_club_image);
        try {
            storageRefClub.child(clubId).getDownloadUrl().addOnSuccessListener(uri -> {
                if (uri != null) {
                    Picasso.with(SenderActivity.this).load(uri).into(imageViewClub);
                }
            });
        } catch (Exception ignored) {

        }

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
        //tvClubId = findViewById(R.id.tv_clubId);
        //tvUserId = findViewById(R.id.tv_userId);
        tvUserName = findViewById(R.id.tv_userName);
        //textView = findViewById(R.id.textView7);
        //test = findViewById(R.id.button7);
        tvClubName.setText(clubName);
        //tvClubId.setText(clubId);
        //tvUserId.setText(userId);
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
        outMessage = clubName+"    "+clubId+"    "+userName+"    "+userId;
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
