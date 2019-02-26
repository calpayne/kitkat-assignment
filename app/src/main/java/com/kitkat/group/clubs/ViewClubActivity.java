package com.kitkat.group.clubs;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kitkat.group.clubs.data.Club;

public class ViewClubActivity extends AppCompatActivity {

    private DatabaseReference db;
    private Club club;
    private static final String VTAG = "ViewClubActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_club);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        db = FirebaseDatabase.getInstance().getReference();
        DatabaseReference instance = db.child("clubs");

        //DocumentReference docRef = db.collection("cities").document(getIntent().getStringExtra("clubId"));

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                DataSnapshot ds = dataSnapshot.child(getIntent().getStringExtra("clubId"));
                    club = new Club(
                            ds.child("clubName").getValue(String.class),
                            ds.child("clubDescription").getValue(String.class),
                            ds.child("clubOwner").getValue(String.class)
                    );
                System.out.println(ds.child("clubName"));
                System.out.println(ds.child("clubDescription").getValue(String.class));
                System.out.println(ds.child("clubOwner").getValue(String.class));

                if(club == null)
                    System.out.println("Club instance is null.");
                else
                    System.out.print(club.toString());

                //ImageView imageView = (ImageView) findViewById(R.id.clubImage);
                //imageView.setImage();

                setTitle(club.getClubName());
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(club.getClubDescription());

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Snackbar.make(view, club.getClubOwner(), Snackbar.LENGTH_LONG)
                                .setAction("Action", null).show();
                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Database error.");
                Log.e(VTAG, "onCancelled", databaseError.toException());
            }
        };
        instance.addListenerForSingleValueEvent(valueEventListener);
    }


}
