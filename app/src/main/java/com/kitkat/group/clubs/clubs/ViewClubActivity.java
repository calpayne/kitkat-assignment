package com.kitkat.group.clubs.clubs;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.internal.Objects;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kitkat.group.clubs.GeneratedQRCode;
import com.kitkat.group.clubs.R;
import com.kitkat.group.clubs.ScanQRCodeActivity;
import com.kitkat.group.clubs.data.Club;
import com.kitkat.group.clubs.data.ClubUser;
import com.kitkat.group.clubs.nfc.SenderActivity;
import com.squareup.picasso.Picasso;

public class ViewClubActivity extends AppCompatActivity {

    private DatabaseReference db;
    private StorageReference storageRef;
    private Club club;
    private static final String VTAG = "ViewClubActivity";
    NfcAdapter nfcAdapter;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    String userName, userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_club);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        db = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference("club-logos");

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

                final ImageView imageView = findViewById(R.id.clubImage);
                storageRef.child(club.getClubID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Picasso.with(ViewClubActivity.this).load(uri).into(imageView);
                    }
                });

                setTitle(club.getClubName());
                TextView textView = findViewById(R.id.textView);
                textView.setText(club.toString());

                FloatingActionButton fab = findViewById(R.id.fab);
                assert fa != null;
                if(dataSnapshot.child("clubs-members").child(clubId).child(fa.getUid()).exists())
                    fab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                else
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

                fab.setOnClickListener(view -> {
                    if(!club.getClubOwner().equalsIgnoreCase(fa.getUid())) {
                        if(dataSnapshot.child("members-clubs").child(fa.getUid()).child(clubId).exists() ||
                                dataSnapshot.child("clubs-members").child(clubId).child(fa.getUid()).exists()){

                            db.child("members-clubs").child(fa.getUid()).child(clubId).removeValue();
                            db.child("clubs-members").child(clubId).child(fa.getUid()).removeValue();

                            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                            Snackbar.make(view, "Left Club.", Snackbar.LENGTH_LONG)
                                    .setAction("Leave Club.", null).show();
                        } else{
                            db.child("members-clubs").child(fa.getUid()).child(clubId).setValue(club.getClubName());
                            db.child("clubs-members").child(clubId).child(fa.getUid() ).setValue(ClubUser.getInstance().getUsername());
                            fab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                            Snackbar.make(view, "Joined Club", Snackbar.LENGTH_LONG)
                                    .setAction("Join Club", null).show();
                        }
                    }
                });

                /*
                FloatingActionButton qrCode = findViewById(R.id.fab2);
                qrCode.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(getApplicationContext(), GeneratedQRCode.class);
                        String clubId = ds.child("clubID").getValue(String.class);
                        intent.putExtra("clubId", clubId);
                        String clubName = ds.child("clubName").getValue(String.class);
                        intent.putExtra("clubName", clubName);
                        startActivity(intent);
                    }
                });
                */

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

    /**
     *  Unfinished - displays the settings menu with Club's QR and Manage
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view_club, menu);

        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu (Menu menu) {
        // disable admin options if user isn't admin
        if (club != null && !club.getClubOwner().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
            menu.findItem(R.id.action_scan_qr).setVisible(false);
            menu.findItem(R.id.action_nfc).setVisible(false);
            menu.findItem(R.id.action_manage).setVisible(false);
        }

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;
        switch(item.getItemId()) {
            case R.id.action_manage:
                System.out.println(club.getClubOwner());
                System.out.println(mAuth.getUid());
                if(Objects.equal(club.getClubOwner(),mAuth.getUid())){
                    Intent management = new Intent(this,ClubSettingsActivity.class);
                    management.putExtra("clubId", club.getClubID());
                    startActivity(management);
                }else
                    Toast.makeText(this,"Access Denied",Toast.LENGTH_SHORT).show();
                break;
            case R.id.action_view_members:
                intent = new Intent(ViewClubActivity.this, ViewClubMembersActivity.class);
                intent.putExtra("clubId", club.getClubID());
                intent.putExtra("isAdmin", club.getClubOwner().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid()) ? "true" : "false");
                startActivity(intent);
                break;
            case R.id.action_generate_qr:
                break;
            case R.id.action_scan_qr:
                intent = new Intent(this, ScanQRCodeActivity.class);
                intent.putExtra("clubId", getIntent().getStringExtra("clubId"));
                startActivity(intent);
                break;
            case R.id.action_nfc:
                NfcPermission();
                break;
            default:
                //unknown error
        }
        return super.onOptionsItemSelected(item);
    }
}
