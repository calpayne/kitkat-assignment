package com.kitkat.group.clubs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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

        databaseRef = FirebaseDatabase.getInstance().getReference();
        clubName = findViewById(R.id.clubName);
        clubDesc = findViewById(R.id.clubDesc);
    }

    public void createClub(View view) {
        if (clubName.getText().toString().isEmpty() || clubDesc.getText().toString().isEmpty()) {
            Toast.makeText(CreateClubActivity.this, "You can't leave the club name or description boxes empty", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseUser fa = FirebaseAuth.getInstance().getCurrentUser();
            Club club = new Club(clubName.getText().toString(), clubDesc.getText().toString(), fa.getUid());

            String id = databaseRef.push().getKey();

            databaseRef.child("clubs").child(id).setValue(club);
            Toast.makeText(CreateClubActivity.this, "Club added", Toast.LENGTH_SHORT).show();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    public void goBack(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
