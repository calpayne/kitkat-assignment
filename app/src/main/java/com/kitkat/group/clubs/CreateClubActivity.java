package com.kitkat.group.clubs;
    
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kitkat.group.clubs.data.Club;

public class CreateClubActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private static final String TAG = "CreateClubActivity";
    private EditText clubName;
    private EditText clubDesc;
    private Switch isPublic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);

        Log.d(TAG, "onCreate: started CreateClubActivity");
  
        databaseRef = FirebaseDatabase.getInstance().getReference();
        clubName = findViewById(R.id.clubName);
        clubDesc = findViewById(R.id.clubDesc);
        isPublic = findViewById(R.id.isPublic);
    }

    public void createClub(View view) {
        if (clubName.getText().toString().isEmpty() || clubDesc.getText().toString().isEmpty()) {
            Toast.makeText(CreateClubActivity.this, "You can't leave the club name or description boxes empty", Toast.LENGTH_SHORT).show();
        } else {
            FirebaseUser fa = FirebaseAuth.getInstance().getCurrentUser();
            Club club = new Club(clubName.getText().toString(), clubDesc.getText().toString(), null, fa.getUid(), isPublic.isChecked());

            String id = databaseRef.push().getKey();

            databaseRef.child("clubs").child(id).setValue(club, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Toast.makeText(CreateClubActivity.this, "Something went wrong and your club couldn't be added", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateClubActivity.this, "Club added", Toast.LENGTH_SHORT).show();
                    }

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            });
        }
    }

    public void goBack(View view) {
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intent);
        finish();
    }
}
