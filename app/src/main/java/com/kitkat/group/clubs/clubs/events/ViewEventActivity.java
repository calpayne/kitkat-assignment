package com.kitkat.group.clubs.clubs.events;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kitkat.group.clubs.R;
import com.kitkat.group.clubs.ScanQRCodeActivity;
import com.kitkat.group.clubs.data.Event;
import com.kitkat.group.clubs.data.Member;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewEventActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private ArrayList<Member> members;
    private ListView membersListView;
    private MemberListAdapter listAdapter;

    private String clubId, eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_event);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        clubId = getIntent().getStringExtra("clubId");
        eventId = getIntent().getStringExtra("eventId");
        boolean isAdmin = getIntent().getStringExtra("ownerId").equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid());

        members = new ArrayList<>();
        listAdapter = new MemberListAdapter(this, members);
        membersListView = findViewById(R.id.registerList);
        membersListView.setAdapter(listAdapter);

        databaseRef = FirebaseDatabase.getInstance().getReference();
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Event event = dataSnapshot.child("clubs").child(clubId).child("events").child(eventId).getValue(Event.class);

                if (event == null) {
                    finish();
                }

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(ViewEventActivity.this, ScanQRCodeActivity.class);
                        intent.putExtra("clubId", clubId);
                        intent.putExtra("eventId", eventId);
                        startActivity(intent);
                    }
                });

                TextView info = findViewById(R.id.eventInfo);
                info.setText(event.toString());

                members.clear();
                listAdapter.clear();
                listAdapter.notifyDataSetChanged();
                if (isAdmin) {
                    for (DataSnapshot postSnapshot: dataSnapshot.child("clubs").child(clubId).child("events").child(eventId).child("register").getChildren()) {
                        members.add(new Member(postSnapshot.getKey(), postSnapshot.getValue(String.class)));
                        listAdapter.notifyDataSetChanged();
                    }
                } else {
                    membersListView.setVisibility(View.INVISIBLE);
                    fab.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                finish();
            }
        });
    }

    private class MemberListAdapter extends ArrayAdapter {

        private final Activity context;
        private ArrayList<Member> data;
        private StorageReference storageRef;

        public MemberListAdapter(@NonNull Activity context, ArrayList<Member> data) {
            super(context,R.layout.listview_row, data);
            storageRef = FirebaseStorage.getInstance().getReference("member-avatars");
            this.context = context;
            this.data = data;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.listview_row, null,true);

            TextView memberNameText = rowView.findViewById(R.id.row_name);
            memberNameText.setText(data.get(position).getMemberName());
            memberNameText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AlertDialog.Builder alert = new AlertDialog.Builder(ViewEventActivity.this);
                    alert.setTitle("Mark as left");
                    alert.setMessage("Are you sure " + data.get(position).getMemberName() + " has left the event?");
                    alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            databaseRef.child("clubs").child(clubId).child("events").child(eventId).child("register").child(data.get(position).getMemberRef()).removeValue();
                            data.remove(position);
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });
                    alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    alert.show();
                }
            });

            final ImageView imageView = rowView.findViewById(R.id.row_image);
            storageRef.child(data.get(position).getMemberRef()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.with(context).load(uri).into(imageView);
                }
            });

            return rowView;
        };
    }

}
