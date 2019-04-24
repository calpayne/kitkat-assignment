package com.kitkat.group.clubs;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kitkat.group.clubs.clubs.ViewClubActivity;
import com.kitkat.group.clubs.clubs.events.ViewEventActivity;
import com.kitkat.group.clubs.data.Club;
import com.kitkat.group.clubs.view.models.EventViewModel;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by Glenn on 17/02/2019.
 */

public class NotificationFragment extends Fragment {

    private static final String TAG = "NotificationFragment";
    private DatabaseReference databaseRef;
    private ListView eventsListView;
    private ArrayList<EventViewModel> events;
    private EventListAdapter listAdapter;

    public NotificationFragment() {
        // Empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: started NotificationFragment");

        View view = inflater.inflate(R.layout.fragment_home_notification, container, false);

        events = new ArrayList<>();
        listAdapter = new EventListAdapter(events);
        eventsListView = view.findViewById(R.id.upcomingList);
        eventsListView.setAdapter(listAdapter);

        databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                events.clear();
                listAdapter.clear();
                listAdapter.notifyDataSetChanged();

                for (DataSnapshot postSnapshot: dataSnapshot.child("members-clubs").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).getChildren()) {
                    DataSnapshot clubSnapshot = dataSnapshot.child("clubs").child(postSnapshot.getKey());
                    Club club = clubSnapshot.getValue(Club.class);

                    for (DataSnapshot eventSnapshot: dataSnapshot.child("clubs").child(club.getClubID()).child("events").getChildren()) {
                        EventViewModel event = eventSnapshot.getValue(EventViewModel.class);
                        event.setEventId(eventSnapshot.getKey());
                        event.setClubId(club.getClubID());
                        event.setOwnerId(club.getClubOwner());
                        events.add(event);
                    }
                }
                Collections.reverse(events);
                listAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return view;
    }

    private class EventListAdapter extends ArrayAdapter {

        private ArrayList<EventViewModel> data;

        public EventListAdapter(ArrayList<EventViewModel> data) {
            super(NotificationFragment.this.getActivity(),R.layout.listview_event_row, data);
            this.data = data;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = NotificationFragment.this.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.listview_event_row, null,true);

            TextView eventText = rowView.findViewById(R.id.row_name);
            eventText.setText(data.get(position).getEventName() + " on " + data.get(position).getEventDate());
            eventText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(NotificationFragment.this.getActivity(), ViewEventActivity.class);
                    intent.putExtra("clubId", data.get(position).getClubId());
                    intent.putExtra("eventId", data.get(position).getEventId());
                    intent.putExtra("ownerId", data.get(position).getOwnerId());
                    startActivity(intent);
                }
            });

            return rowView;
        };
    }
}