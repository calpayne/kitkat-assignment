package com.kitkat.group.clubs.clubs;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kitkat.group.clubs.R;
import com.kitkat.group.clubs.data.Member;

import java.util.ArrayList;

public class ViewClubMembersActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private ArrayList<Member> members;
    private ListView membersListView;
    private MemberListAdapter listAdapter;
    private boolean isAdmin;
    private String clubId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_club_members);

        members = new ArrayList<>();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        listAdapter = new MemberListAdapter(this, members);
        membersListView = findViewById(R.id.members_list);
        membersListView.setAdapter(listAdapter);

        isAdmin = getIntent().getStringExtra("isAdmin").equalsIgnoreCase("true");
        clubId = getIntent().getStringExtra("clubId");

        loadIntoListView(clubId);
    }

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

        public MemberListAdapter(@NonNull Activity context, ArrayList<Member> data) {
            super(context,R.layout.listview_row, data);
            this.context = context;
            this.data = data;
        }

        public View getView(final int position, View view, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View rowView = inflater.inflate(R.layout.listview_row, null,true);

            TextView clubNameText = rowView.findViewById(R.id.club_name);
            clubNameText.setText(data.get(position).getMemberName());

            if (isAdmin) {
                clubNameText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseRef.child("members-clubs").child(data.get(position).getMemberRef()).child(clubId).removeValue();
                        databaseRef.child("clubs-members").child(clubId).child(data.get(position).getMemberRef()).removeValue();
                        data.remove(position);
                        notifyDataSetChanged();
                    }
                });
            }

            ImageView imageView = rowView.findViewById(R.id.club_logo);
            imageView.setEnabled(false);
            imageView.setVisibility(View.INVISIBLE);

            return rowView;
        };
    }

}
