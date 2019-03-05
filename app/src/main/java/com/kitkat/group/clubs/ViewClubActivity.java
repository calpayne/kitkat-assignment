package com.kitkat.group.clubs;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
        DatabaseReference instance = db;

        //DocumentReference docRef = db.collection("cities").document(getIntent().getStringExtra("clubId"));

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                final FirebaseUser fa = FirebaseAuth.getInstance().getCurrentUser();
                String clubId = getIntent().getStringExtra("clubId");

                DataSnapshot ds = dataSnapshot.child("clubs")
                                              .child(clubId);
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
                TextView textView = (TextView) findViewById(R.id.textView);
                textView.setText(club.getClubDescription());

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                if(dataSnapshot.child("clubs-members").child(clubId).child(fa.getUid()).exists())
                    fab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                else
                    fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));

                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(dataSnapshot.child("members-clubs").child(fa.getUid()).child(clubId).exists() ||
                           dataSnapshot.child("clubs-members").child(clubId).child(fa.getUid()).exists()){

                            db.child("members-clubs").child(fa.getUid()).child(clubId).removeValue();
                            db.child("clubs-members").child(clubId).child(fa.getUid()).removeValue();

                            fab.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.colorAccent)));
                            Snackbar.make(view, "Left Club.", Snackbar.LENGTH_LONG)
                                    .setAction("Leave Club.", null).show();
                        }else{
                            db.child("members-clubs").child(fa.getUid()).child(clubId).setValue(true);
                            db.child("clubs-members").child(clubId).child(fa.getUid() ).setValue(true);

                            fab.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                            Snackbar.make(view, "Joined Club", Snackbar.LENGTH_LONG)
                                    .setAction("Join Club", null).show();
                        }

                    }
                });
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                System.out.println("Database error.");
                Log.e(VTAG, "onCancelled", databaseError.toException());
            }
        };
        db.addValueEventListener(valueEventListener);
    }


}
