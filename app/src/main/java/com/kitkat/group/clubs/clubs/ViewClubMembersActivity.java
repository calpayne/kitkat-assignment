package com.kitkat.group.clubs.clubs;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.nfc.NfcAdapter;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.kitkat.group.clubs.data.Member;
import com.kitkat.group.clubs.nfc.subTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ViewClubMembersActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private ArrayList<Member> members;
    private ListView membersListView;
    private MemberListAdapter listAdapter;
    private boolean isAdmin;
    private String clubId;
    NfcAdapter nfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_club_members);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        members = new ArrayList<>();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        listAdapter = new MemberListAdapter(this, members);
        membersListView = findViewById(R.id.members_list);
        membersListView.setAdapter(listAdapter);

        isAdmin = getIntent().getStringExtra("isAdmin").equalsIgnoreCase("true");
        clubId = getIntent().getStringExtra("clubId");

        loadIntoListView(clubId);
    }

    //This this code to stop NFC restarting the app
    public void onResume() {
        super.onResume();
        if(isNfcSupported()) {
            subTask ob=new subTask();
            nfcAdapter=ob.Resume(this,nfcAdapter,new Intent(this,ViewClubMembersActivity.class));
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

    private void loadIntoListView(String clubId) {
        members.clear();
        listAdapter.clear();
        listAdapter.notifyDataSetChanged();
        databaseRef.child("clubs-members").child(clubId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String data = null;
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    members.add(new Member(ds.getKey(), ds.getValue(String.class)));
                    listAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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

            if (isAdmin && !data.get(position).getMemberRef().equalsIgnoreCase(FirebaseAuth.getInstance().getCurrentUser().getUid())) {
                memberNameText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder alert = new AlertDialog.Builder(ViewClubMembersActivity.this);
                        alert.setTitle("Kick");
                        alert.setMessage("Are you sure you want to kick " + data.get(position).getMemberName() + "?");
                        alert.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                databaseRef.child("members-clubs").child(data.get(position).getMemberRef()).child(clubId).removeValue();
                                databaseRef.child("clubs-members").child(clubId).child(data.get(position).getMemberRef()).removeValue();
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
            }

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
