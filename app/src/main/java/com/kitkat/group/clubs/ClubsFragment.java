package com.kitkat.group.clubs;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kitkat.group.clubs.data.Club;

import java.util.ArrayList;

/**
 * Created by Admin on 13/02/2019.
 */

public class ClubsFragment extends Fragment {

    private static final String TAG = "ClubsFragment";
    private DatabaseReference databaseRef;
    private ArrayList<Club> clubs;
    private ListView clubsListView;
    private ClubListAdapter listAdapter;
    private EditText searchText;

    public ClubsFragment() {
        // Empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started ClubsFragment");

        View view = inflater.inflate(R.layout.fragment_clubs, container, false);

        clubs = new ArrayList<>();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        listAdapter = new ClubListAdapter(getActivity(), clubs);
        clubsListView = view.findViewById(R.id.clubs_list_view);
        clubsListView.setAdapter(listAdapter);
        searchText = view.findViewById(R.id.searchText);

        loadIntoListView(null, false);

        view.findViewById(R.id.btn_clubs_create).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CreateClubActivity.class);
                startActivity(intent);
            }
        });

        view.findViewById(R.id.search_clubs_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (searchText.getText().toString().isEmpty()) {
                    loadIntoListView(null, false);
                } else {
                    loadIntoListView(searchText.getText().toString(), false);
                }
            }
        });

        view.findViewById(R.id.btn_my_clubs).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadIntoListView(FirebaseAuth.getInstance().getCurrentUser().getUid(), true);
            }
        });

        return view;
    }

    public void loadIntoListView(String search, boolean myClubs) {
        clubs.clear();
        listAdapter.clear();
        listAdapter.notifyDataSetChanged();

        if (myClubs) {
            databaseRef.child("members-clubs").child(search).addListenerForSingleValueEvent(new ClubListListener(true));
        } else {
            if (search == null) {
                databaseRef.child("clubs").addListenerForSingleValueEvent(new ClubListListener(false));
            } else {
                databaseRef.child("clubs").orderByChild("clubName").startAt(searchText.getText().toString()).endAt(searchText.getText().toString() + "\uf8ff").addListenerForSingleValueEvent(new ClubListListener(false));
            }
        }
    }

    private class ClubListListener implements ValueEventListener {

        private boolean ownClubs;

        public ClubListListener(boolean ownClubs) {
            this.ownClubs = ownClubs;
        }

        @Override
        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
            Club data = null;
            for (DataSnapshot ds : dataSnapshot.getChildren()) {
                if (ownClubs) {
                    data = new Club();
                    data.setClubID(ds.getKey());
                    data.setClubName(ds.getValue(String.class));
                } else {
                    data = ds.getValue(Club.class);
                }
                
                clubs.add(data);
                listAdapter.notifyDataSetChanged();
            }

            if (data == null) {
                Toast.makeText(getActivity(), "None found", Toast.LENGTH_SHORT).show();
            }
        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    }
}