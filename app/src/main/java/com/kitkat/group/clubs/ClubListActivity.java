package com.kitkat.group.clubs;

import android.app.Activity;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kitkat.group.clubs.data.Club;

import java.util.ArrayList;

public class ClubListActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private ArrayList<Club> clubs;
    private ListView clubsListView;
    private EditText searchText;
    private Activity context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);

        clubs = new ArrayList<>();
        databaseRef = FirebaseDatabase.getInstance().getReference("clubs");
        final ClubListAdapter listAdapter = new ClubListAdapter(this, clubs);
        clubsListView = findViewById(R.id.clubs_list);
        clubsListView.setAdapter(listAdapter);
        searchText = findViewById(R.id.editText);
        context = this;

        databaseRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Club data = dataSnapshot.getValue(Club.class);
                clubs.add(data);
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Club data = dataSnapshot.getValue(Club.class);

                for (Club c : clubs) {
                    if (c.getClubID().equalsIgnoreCase(data.getClubID())) {
                        c.setClubName(data.getClubName());
                        break;
                    }
                }

                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                Club data = dataSnapshot.getValue(Club.class);

                for (Club c : clubs) {
                    if (c.getClubID().equalsIgnoreCase(data.getClubID())) {
                        clubs.remove(c);
                        break;
                    }
                }

                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void search(View view) {
        if (searchText.getText().toString().isEmpty()) {
            Toast.makeText(ClubListActivity.this, "You can't leave the search box empty", Toast.LENGTH_SHORT).show();
        } else {
            databaseRef.orderByChild("clubName").equalTo(searchText.getText().toString()).addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    Club data = null;
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        data = ds.getValue(Club.class);
                    }

                    if (data == null) {
                        Toast.makeText(ClubListActivity.this, "None found", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = new Intent(context, ViewClubActivity.class);
                        intent.putExtra("clubId",data.getClubID());
                        startActivity(intent);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
