package com.kitkat.group.clubs.clubs;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.auth.data.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kitkat.group.clubs.R;
import com.kitkat.group.clubs.data.Club;
import com.kitkat.group.clubs.data.ClubUser;
import com.kitkat.group.clubs.nfc.SenderActivity;

public class ViewClubActivity extends AppCompatActivity {

    private DatabaseReference db;
    private Club club;
    private static final String VTAG = "ViewClubActivity";
    private NfcAdapter nfcAdapter;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    String userName, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_club);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseDatabase.getInstance().getReference();

        //DatabaseReference instance = db;

        //DocumentReference docRef = db.collection("cities").document(getIntent().getStringExtra("clubId"));

        mAuth=FirebaseAuth.getInstance();

        //noinspection ConstantConditions
        userId=mAuth.getCurrentUser().getUid();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userName = String.valueOf(snapshot.child(userId).getValue());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

        Button nfcbutton = findViewById(R.id.nfcbutton);
        nfcbutton.setOnClickListener(v -> NfcPermission());

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                final FirebaseUser fa = FirebaseAuth.getInstance().getCurrentUser();
                String clubId = getIntent().getStringExtra("clubId");

                DataSnapshot ds = dataSnapshot.child("clubs").child(clubId);
                club = ds.getValue(Club.class);

                System.out.println(ds.child("clubName").getValue(String.class));
                System.out.println(ds.child("clubDescription").getValue(String.class));
                System.out.println(ds.child("clubOwner").getValue(String.class));


                if(club == null)
                    System.out.println("Club instance is null.");
                else
                    System.out.print(club.toString());

                //ImageView imageView = (ImageView) findViewById(R.id.clubImage);
                //imageView.setImage();

                setTitle(club.getClubName());
                TextView textView = findViewById(R.id.textView);
                textView.setText(club.getClubDescription());
                textView.setOnClickListener(view -> {
                    Intent intent = new Intent(ViewClubActivity.this, ViewClubMembersActivity.class);
                    intent.putExtra("clubId",club.getClubID());
                    intent.putExtra("isAdmin","true");
                    startActivity(intent);
                });

                FloatingActionButton fab = findViewById(R.id.fab);
                assert fa != null;
                if(dataSnapshot.child("clubs-members").child(clubId).child(fa.getUid()).exists())
                    fab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                else
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

                fab.setOnClickListener(view -> {

                    if(dataSnapshot.child("members-clubs").child(fa.getUid()).child(clubId).exists() ||
                            dataSnapshot.child("clubs-members").child(clubId).child(fa.getUid()).exists()){

                        db.child("members-clubs").child(fa.getUid()).child(clubId).removeValue();
                        db.child("clubs-members").child(clubId).child(fa.getUid()).removeValue();

                        fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                        Snackbar.make(view, "Left Club.", Snackbar.LENGTH_LONG)
                                .setAction("Leave Club.", null).show();
                    }else{
                        db.child("members-clubs").child(fa.getUid()).child(clubId).setValue(club.getClubName());
                        db.child("clubs-members").child(clubId).child(fa.getUid() ).setValue(ClubUser.getInstance().getUsername());
                        fab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                        Snackbar.make(view, "Joined Club", Snackbar.LENGTH_LONG)
                                .setAction("Join Club", null).show();
                    }

                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Database error.");
                Log.e(VTAG, "onCancelled", databaseError.toException());
            }
        };
        db.addValueEventListener(valueEventListener);
    }

    public void NfcPermission(){
        nfcAdapter = NfcAdapter.getDefaultAdapter(this);
        if (!nfcAdapter.isEnabled())
        {
            new AlertDialog.Builder(this)
                    .setTitle("NFC Permission")
                    .setMessage("Go to settings and turn on NFC")
                    .setPositiveButton(android.R.string.yes, (dialog, which) -> startActivity(new Intent(Settings.ACTION_NFC_SETTINGS)))
                    .setNegativeButton(android.R.string.no, null)
                    .show();

//            Toast.makeText(getApplicationContext(), "Please activate NFC and press Back to return to the application!", Toast.LENGTH_LONG).show();


        }
        if(nfcAdapter.isEnabled()){
            Intent intent = new Intent(this, SenderActivity.class);
            intent.putExtra("clubId", club.getClubID());
            intent.putExtra("clubName", club.getClubName());
            intent.putExtra("userId",userId);
            intent.putExtra("userName",userName);
            startActivity(intent);
        }

    }


}
