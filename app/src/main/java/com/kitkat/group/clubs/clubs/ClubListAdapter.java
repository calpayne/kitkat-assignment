package com.kitkat.group.clubs.clubs;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kitkat.group.clubs.R;
import com.kitkat.group.clubs.data.Club;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Admin on 24/02/2019.
 */

public class ClubListAdapter extends ArrayAdapter {

    private StorageReference storageRef;
    private final Activity context;
    private ArrayList<Club> data;

    public ClubListAdapter(@NonNull Activity context, ArrayList<Club> data) {
        super(context, R.layout.listview_row, data);
        storageRef = FirebaseStorage.getInstance().getReference("club-logos");
        this.context = context;
        this.data = data;
    }

    public View getView(final int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.listview_row, null,true);

        TextView clubNameText = rowView.findViewById(R.id.club_name);
        clubNameText.setText(data.get(position).getClubName());

        final ImageView imageView = rowView.findViewById(R.id.club_logo);
        storageRef.child(data.get(position).getClubID()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.with(context).load(uri).into(imageView);
            }
        });

        rowView.setOnClickListener(new View.OnClickListener() {
            public void onClick(final View v) {
                Intent intent = new Intent(context, ViewClubActivity.class);
                intent.putExtra("clubId",data.get(position).getClubID());
                context.startActivity(intent);
            }
        });

        return rowView;
    };
}
