package com.kitkat.group.clubs.clubs.events;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.nfc.NfcAdapter;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kitkat.group.clubs.MainActivity;
import com.kitkat.group.clubs.R;
import com.kitkat.group.clubs.data.Event;
import com.kitkat.group.clubs.nfc.subTask;

import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity  implements DatePickerDialog.OnDateSetListener {

    private DatabaseReference databaseRef;
    private String clubID;
    private EditText eventName, eventDesc;
    private TextView eventDate;
    private ProgressDialog progressDialog;
    NfcAdapter nfcAdapter;
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
        eventDate = findViewById(R.id.selectDate);
        eventDesc = findViewById(R.id.eventDesc);

        eventDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        CreateEventActivity.this,
                        CreateEventActivity.this,
                        Calendar.getInstance().get(Calendar.YEAR),
                        Calendar.getInstance().get(Calendar.MONTH),
                        Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
    }

    //This this code to stop NFC restarting the app
    public void onResume() {
        super.onResume();
        if(isNfcSupported()) {
            subTask ob=new subTask();
            nfcAdapter=ob.Resume(this,nfcAdapter,new Intent(this,CreateEventActivity.class));
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

    public void createEvent(View view) {
        if (eventDate.getText().toString().equalsIgnoreCase("Click to select a date")) {
            Toast.makeText(CreateEventActivity.this, "You need to select a date for the event", Toast.LENGTH_SHORT).show();
        } else if (eventName.getText().toString().isEmpty() || eventDesc.getText().toString().isEmpty()) {
            Toast.makeText(CreateEventActivity.this, "You can't leave the event name or description boxes empty", Toast.LENGTH_SHORT).show();
        } else if (eventName.getText().toString().length() < 3 || eventName.getText().toString().length() > 25) {
            Toast.makeText(CreateEventActivity.this, "Your event name needs to be between 3 and 25 characters", Toast.LENGTH_SHORT).show();
        } else if (eventDesc.getText().toString().length() < 3 || eventDesc.getText().toString().length() > 100) {
            Toast.makeText(CreateEventActivity.this, "Your event description needs to be between 3 and 100 characters", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setMessage("Creating event...");
            progressDialog.show();
            final FirebaseUser fa = FirebaseAuth.getInstance().getCurrentUser();

            Event event = new Event(eventName.getText().toString(), eventDate.getText().toString(), eventDesc.getText().toString());

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

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        eventDate.setText(day + "/" + (month + 1) + "/" + year);
    }
}
