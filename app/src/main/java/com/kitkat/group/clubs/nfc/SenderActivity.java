package com.kitkat.group.clubs.nfc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
    String clubId, userId, clubName, outMessage, userName;
    TextView tvClubName, tvClubId, tvUserId, test, userNameImage, clubNameImage, question;
    //Runtime permission
    private TextView resultView;
    private View requestView;

    //Firebase data fetching
    private DatabaseReference db;
    private StorageReference storageRef;
    private Club club;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    public static final String preference="ref";
    public static final String saveit="savekey";

    @SuppressLint("SetTextI18n")
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
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");


        SharedPreferences sf4=getSharedPreferences(preference, Context.MODE_PRIVATE);

        SharedPreferences.Editor editor4=sf4.edit();
        editor4.putString(saveit,clubId);
        editor4.apply();

//        Intent intent = new Intent(SenderActivity.this, ReceiverActivity.class);
//        intent.putExtra("clubNameRec", clubNameRec);

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


        userNameImage=findViewById(R.id.userNameImage);
        clubNameImage=findViewById(R.id.clubNameImage);
        question=findViewById(R.id.textView3);

        String quest= "Are you a member of the club ";
        String qMark = " ?";
        question.setText(quest+clubName+qMark);
        userNameImage.setText(userName);
        clubNameImage.setText(clubName);


        // encapsulate sending logic in a separate class
        this.outcomingNfccallback = new OutcomingNfcManager(this);
        this.nfcAdapter.setOnNdefPushCompleteCallback(outcomingNfccallback, this);
        this.nfcAdapter.setNdefPushMessageCallback(outcomingNfccallback, this);

        initViews();
    }


    private void initViews() {
        //this.tvOutcomingMessage = findViewById(R.id.tv_out_message);
//        this.btnSetOutcomingMessage = findViewById(R.id.btn_set_out_message);
//        this.btnSetOutcomingMessage.setOnClickListener((v) -> setOutGoingMessage());
        Toast.makeText(this, "Tap on NFC device to verify details", Toast.LENGTH_SHORT).show();
        outMessage = clubName+"    "+clubId+"    "+userName+"    "+userId;
    }


    @Override
    protected void onNewIntent(Intent intent) {
        setIntent(intent);
    }

    private boolean isNfcSupported() {
        this.nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        return this.nfcAdapter != null;
    }


//    private void setOutGoingMessage() {
//
//        //textView.setText(outMessage);
//        //this.tvOutcomingMessage.setText(outMessage);
//    }

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
