package com.kitkat.group.clubs.clubs.events;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kitkat.group.clubs.MainActivity;
import com.kitkat.group.clubs.R;
import com.kitkat.group.clubs.data.Event;

public class CreateEventActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private String clubID;
    private EditText eventName, eventDesc;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        clubID = getIntent().getStringExtra("clubId");

        if (clubID == null) {
            finish();
        }

        databaseRef = FirebaseDatabase.getInstance().getReference();
        progressDialog = new ProgressDialog(this);
        eventName = findViewById(R.id.eventName);
        eventDesc = findViewById(R.id.eventDesc);
    }

    public void createEvent(View view) {
        if (eventName.getText().toString().isEmpty() || eventDesc.getText().toString().isEmpty()) {
            Toast.makeText(CreateEventActivity.this, "You can't leave the event name or description boxes empty", Toast.LENGTH_SHORT).show();
        } else if (eventName.getText().toString().length() < 3 || eventName.getText().toString().length() > 25) {
            Toast.makeText(CreateEventActivity.this, "Your event name needs to be between 3 and 25 characters", Toast.LENGTH_SHORT).show();
        } else if (eventDesc.getText().toString().length() < 3 || eventDesc.getText().toString().length() > 100) {
            Toast.makeText(CreateEventActivity.this, "Your event description needs to be between 3 and 100 characters", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("Creating event...");
            progressDialog.show();
            final FirebaseUser fa = FirebaseAuth.getInstance().getCurrentUser();

            Event event = new Event(eventName.getText().toString(), eventDesc.getText().toString());

            databaseRef.child("clubs").child(clubID).child("events").push().setValue(event, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Toast.makeText(CreateEventActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateEventActivity.this, "Event added", Toast.LENGTH_SHORT).show();
                    }

                    progressDialog.dismiss();
                    finish();
                }
            });
        }
    }

    public void goBack(View view) {
        finish();
    }
}
