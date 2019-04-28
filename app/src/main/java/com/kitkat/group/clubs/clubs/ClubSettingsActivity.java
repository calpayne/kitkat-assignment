package com.kitkat.group.clubs.clubs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kitkat.group.clubs.R;
import com.kitkat.group.clubs.data.Club;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class ClubSettingsActivity extends AppCompatActivity {

    private DatabaseReference db;
    private static final String TAG = "ClubDetailsActivity";
    private Club club,temp;
    private Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("Screen Accessed");
        setContentView(R.layout.activity_club_settings);

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

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                club = dataSnapshot.child("clubs").child(getIntent().getStringExtra("clubId")).getValue(Club.class);
                System.out.println("Club \n" + club.toString());
                temp = dataSnapshot.child("clubs").child(getIntent().getStringExtra("clubId")).getValue(Club.class);
                System.out.println("Temp \n" + temp.toString());
                String ownerName = dataSnapshot.child("users").child(club.getClubOwner()).getValue(String.class);

                TextView clubName = findViewById(R.id.txt_nameContent);
                TextView clubOwner = findViewById(R.id.txt_ownerContent);
                TextView clubDesc = findViewById(R.id.txt_descContent);
                TextView clubLoc = findViewById(R.id.txt_locationContent);

                clubName.setText(club.getClubName());
                clubOwner.setText(ownerName);
                clubDesc.setText(club.getClubDescription());

                if(club.getClubLocation() == null)
                    clubLoc.setText("No Location Set");
                else
                    clubLoc.setText(club.getClubLocation());
                clubLoc.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        if(club.getClubLocation() != null){
                            Intent  map = new Intent(getApplicationContext(), MapViewActivity.class);
                            map.putExtra("locCoOrds",club.getClubLocation());
                            map.putExtra("locName","Test Location");
                            startActivity(map);
                        }else
                            Toast.makeText(ClubSettingsActivity.this,"Cannot view map with no location set.", Toast.LENGTH_SHORT).show();
                    }
                });

                findViewById(R.id.btn_editName).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View view) {
                        EditText editText = new EditText(context);

                        new AlertDialog.Builder(context)
                                .setTitle("Edit Club Name")
                                .setView(editText)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        club.setClubName(editText.getText().toString());
                                        clubName.setText(club.getClubDescription());
                                    }
                                })
                                .setNegativeButton("CANCEL", (dialog, i) -> dialog.cancel())
                                .show();

                    }
                });

                findViewById(R.id.btn_location).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText editText = new EditText(context);

                        new AlertDialog.Builder(context)
                                .setTitle("Set Club Location")
                                .setView(editText)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {

                                        Geocoder geocoder = new Geocoder(context,Locale.UK);
                                        System.out.println("Successfully created Geocoder");
                                        List<Address> addresses;

                                            try {
                                                addresses = geocoder.getFromLocationName(editText.getText().toString(),10);
                                                if(addresses != null){
                                                    Address location = addresses.get(0);
                                                    double lat = location.getLatitude();
                                                    double lng = location.getLongitude();

                                                    club.setClubLocation(lat + "," + lng);
                                                    clubLoc.setText(club.getClubLocation());
                                                }
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                    }
                                })
                                .setNegativeButton("CANCEL", (dialog, i) -> dialog.cancel())
                                .show();
                    }
                });

                findViewById(R.id.btn_editDesc).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        EditText editText = new EditText(context);

                        new AlertDialog.Builder(context)
                                .setTitle("Edit Club Description")
                                .setView(editText)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        club.setClubDescription(editText.getText().toString());
                                        clubDesc.setText(club.getClubDescription());
                                    }
                                })
                                .setNegativeButton("CANCEL", (dialog, i) -> dialog.cancel())
                                .show();
                    }
                });

                Switch publicity = findViewById(R.id.switch_publicity);
                publicity.setChecked(club.getIsPublic());
                publicity.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        club.setIsPublic(publicity.isChecked());
                    }
                });

                findViewById(R.id.btn_disband).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        EditText editText = new EditText(context);

                        new AlertDialog.Builder(context)
                                .setTitle("Type the club's name to disband. There is no going back.")
                                .setView(editText)
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        if(Objects.equals(editText.getText().toString(),club.getClubName())){
                                            Toast.makeText(ClubSettingsActivity.this,"Club Disbanded.",Toast.LENGTH_SHORT).show();
                                            db.child("clubs").child(club.getClubID()).removeValue();
                                            db.child("clubs-members").child(club.getClubID()).removeValue();
                                            db.child("members-clubs").addListenerForSingleValueEvent(new ValueEventListener() {
                                                @Override
                                                public void onDataChange(DataSnapshot snapshot) {
                                                    for(DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                                        if(dataSnapshot.hasChild(club.getClubID())){
                                                            dataSnapshot.getRef().child(club.getClubID()).removeValue();
                                                        }
                                                    }
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {
                                                    Log.w("TAG: ", databaseError.getMessage());
                                                }
                                            });
                                            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new ClubsFragment()).commit();
                                        }
                                    }
                                })
                                .setNegativeButton("CANCEL", (dialog, i) -> dialog.cancel())
                                .show();


                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Database error.");
                Log.e(TAG, "onCancelled", databaseError.toException());
            }
        };

        db.addListenerForSingleValueEvent(valueEventListener);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_save);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                save();
            }
        });
    }

    private void save(){
        if(!Objects.equals(club.toString(),temp.toString())){
           new AlertDialog.Builder(this)
                   .setTitle("Save Changes?")
                   .setPositiveButton("OK", (dialogInterface, i) -> updateClubInDatabase())
                   .setNegativeButton("CANCEL", (dialogInterface, i) -> dialogInterface.cancel())
                   .show();
        }
    }

    private void updateClubInDatabase() {
        db.child("clubs").child(club.getClubID()).child("clubName").setValue(club.getClubName());
        db.child("clubs").child(club.getClubID()).child("clubNameSearch").setValue(club.getClubName().toLowerCase());
        db.child("clubs").child(club.getClubID()).child("clubDescription").setValue(club.getClubDescription());
        db.child("clubs").child(club.getClubID()).child("clubLocation").setValue(club.getClubLocation());
        db.child("clubs").child(club.getClubID()).child("isPublic").setValue(club.getIsPublic());
    }

}
