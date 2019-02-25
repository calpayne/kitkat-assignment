package com.kitkat.group.clubs;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kitkat.group.clubs.data.Club;

import java.util.ArrayList;

public class ClubListActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private ArrayList<Club> clubs;
    private ListView clubsListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);

        clubs = new ArrayList<>();
        databaseRef = FirebaseDatabase.getInstance().getReference("clubs");
        final ClubListAdapter listAdapter = new ClubListAdapter(this, clubs);
        clubsListView = findViewById(R.id.clubs_list);
        clubsListView.setAdapter(listAdapter);

        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Club data = dataSnapshot.getValue(Club.class);
                clubs.add(data);
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
