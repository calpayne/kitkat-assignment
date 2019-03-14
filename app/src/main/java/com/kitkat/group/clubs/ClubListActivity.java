package com.kitkat.group.clubs;

import android.app.Activity;
import android.app.ProgressDialog;
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
    private ClubListAdapter listAdapter;
    private EditText searchText;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club_list);

        clubs = new ArrayList<>();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        listAdapter = new ClubListAdapter(this, clubs);
        clubsListView = findViewById(R.id.clubs_list);
        clubsListView.setAdapter(listAdapter);
        searchText = findViewById(R.id.editText);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Loading all clubs...");
        progressDialog.show();

        loadIntoListView(null);
    }

    public void loadIntoListView(String search) {
        clubs.clear();
        listAdapter.clear();
        listAdapter.notifyDataSetChanged();

        if (search == null) {
            databaseRef.child("clubs").addListenerForSingleValueEvent(new ClubListListener());
        } else {
            databaseRef.child("clubs").orderByChild("clubName").equalTo(searchText.getText().toString()).addListenerForSingleValueEvent(new ClubListListener());
        }
    }

    public void search(View view) {
        if (searchText.getText().toString().isEmpty()) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Loading all clubs...");
            progressDialog.show();

            loadIntoListView(null);
        } else {
            progressDialog.setMessage("Searching clubs...");
            progressDialog.show();

            loadIntoListView(searchText.getText().toString());
        }
    }

    private class ClubListListener implements ValueEventListener {

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Club data = null;
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                data = ds.getValue(Club.class);
                clubs.add(data);
                listAdapter.notifyDataSetChanged();
            }

            progressDialog.dismiss();

            if (data == null) {
                Toast.makeText(ClubListActivity.this, "None found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
}
