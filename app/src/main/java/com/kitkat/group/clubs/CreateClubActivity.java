package com.kitkat.group.clubs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kitkat.group.clubs.data.Club;

public class CreateClubActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private EditText clubName;
    private EditText clubDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);

        databaseRef = FirebaseDatabase.getInstance().getReference("clubs");
        clubName = (EditText) findViewById(R.id.clubName);
        clubDesc = (EditText) findViewById(R.id.clubDesc);
    }

    public void createClub(View view) {
        FirebaseUser fa = FirebaseAuth.getInstance().getCurrentUser();
        Club club = new Club(clubName.getText().toString(), clubDesc.getText().toString(), fa.getUid());

        String id = databaseRef.push().getKey();
        databaseRef.child(id).setValue(club);
    }
}
