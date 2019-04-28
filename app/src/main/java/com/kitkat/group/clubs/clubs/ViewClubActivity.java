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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.kitkat.group.clubs.MainActivity;
import com.kitkat.group.clubs.R;
import com.kitkat.group.clubs.ScanQRCodeActivity;
import com.kitkat.group.clubs.clubs.events.CreateEventActivity;
import com.kitkat.group.clubs.clubs.events.ViewEventActivity;
import com.kitkat.group.clubs.data.Club;
import com.kitkat.group.clubs.data.ClubUser;
import com.kitkat.group.clubs.nfc.TutorialActivity;
import com.kitkat.group.clubs.nfc.subTask;
import com.kitkat.group.clubs.view.models.EventViewModel;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;
import java.util.Collections;

public class ViewClubActivity extends AppCompatActivity {

    private DatabaseReference db;
    private StorageReference storageRef;
    private Club club;
    private static final String VTAG = "ViewClubActivity";
    NfcAdapter nfcAdapter;
    DatabaseReference mDatabase;
    FirebaseAuth mAuth;
    String userName, userId;

    private ListView eventsListView;
    private ArrayList<EventViewModel> events;
    private EventListAdapter listAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_club);

        events = new ArrayList<>();
        listAdapter = new EventListAdapter(events);
        eventsListView = findViewById(R.id.eventsList);
        eventsListView.setAdapter(listAdapter);

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

                events.clear();
                listAdapter.clear();
                listAdapter.notifyDataSetChanged();
                boolean hasEvents = false;
                for (DataSnapshot postSnapshot: dataSnapshot.child("clubs").child(clubId).child("events").getChildren()) {
                    EventViewModel event = postSnapshot.getValue(EventViewModel.class);
                    event.setEventId(postSnapshot.getKey());
                    event.setClubId(club.getClubID());
                    event.setOwnerId(club.getClubOwner());
                    events.add(event);
                    hasEvents = true;
                }
                Collections.reverse(events);
                listAdapter.notifyDataSetChanged();

                String events = hasEvents ? "\n\n\nEvents:" : "\n\n\nEvents:\nNo events";

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
                textView.setText(club.toString() + events);

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
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                System.out.println("Database error.");
                Log.e(VTAG, "onCancelled", databaseError.toException());
            }
        };
        db.addValueEventListener(valueEventListener);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent myIntent = new Intent(ViewClubActivity.this, MainActivity.class);
        ViewClubActivity.this.startActivity(myIntent);
     }

    //This this code to stop NFC restarting the app
    public void onResume() {
        super.onResume();
        if(isNfcSupported()) {
            subTask ob=new subTask();
            nfcAdapter=ob.Resume(this,nfcAdapter,new Intent(this,ViewClubActivity.class));
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
            Intent intent = new Intent(this, TutorialActivity.class);
            intent.putExtra("clubId", club.getClubID());
            intent.putExtra("clubName", club.getClubName());
            intent.putExtra("userId",userId);
            intent.putExtra("userName",userName);
            startActivity(intent);
        }

    }

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
            menu.findItem(R.id.action_manage).setVisible(false);
            menu.findItem(R.id.action_create_event).setVisible(false);
        }
        if(!isNfcSupported()) {
            menu.findItem(R.id.action_nfc).setVisible(false);
        }
        else{
            menu.findItem(R.id.action_nfc).setVisible(true);
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
                intent = new Intent(getApplicationContext(), GeneratedQRCode.class);
                intent.putExtra("clubId", club.getClubID());
                intent.putExtra("clubName", club.getClubName());
                startActivity(intent);
                break;
            case R.id.action_create_event:
                intent = new Intent(this, CreateEventActivity.class);
                intent.putExtra("clubId", getIntent().getStringExtra("clubId"));
                startActivity(intent);
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

    private class EventListAdapter extends ArrayAdapter {

        private ArrayList<EventViewModel> data;

        public EventListAdapter(ArrayList<EventViewModel> data) {
            super(ViewClubActivity.this,R.layout.listview_event_row, data);
            this.data = data;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = ViewClubActivity.this.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.listview_event_row, null,true);

            TextView eventText = rowView.findViewById(R.id.row_name);
            eventText.setText(data.get(position).getEventName() + " on " + data.get(position).getEventDate());
            eventText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(ViewClubActivity.this, ViewEventActivity.class);
                    intent.putExtra("clubId", data.get(position).getClubId());
                    intent.putExtra("eventId", data.get(position).getEventId());
                    intent.putExtra("ownerId", data.get(position).getOwnerId());
                    intent.putExtra("FROM_ACTIVITY","view");
                    startActivity(intent);
                }
            });

            return rowView;
        };
    }
}
