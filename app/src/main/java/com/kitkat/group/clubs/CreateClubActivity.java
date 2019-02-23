package com.kitkat.group.clubs;
    
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;
import android.util.Log;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kitkat.group.clubs.data.Club;

import java.util.UUID;

public class CreateClubActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private StorageReference storageRef;
    private static final String TAG = "CreateClubActivity";
    private static final int GALLERY_INTENT = 2;
    private final String clubID = UUID.randomUUID().toString();
    private Uri image;
    private EditText clubName;
    private EditText clubDesc;
    private Switch isPublic;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_club);

        Log.d(TAG, "onCreate: started CreateClubActivity");
  
        databaseRef = FirebaseDatabase.getInstance().getReference();
        storageRef = FirebaseStorage.getInstance().getReference();
        clubName = findViewById(R.id.clubName);
        clubDesc = findViewById(R.id.clubDesc);
        isPublic = findViewById(R.id.isPublic);
        progressDialog = new ProgressDialog(this);
    }

    public void uploadLogo(View view) {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        startActivityForResult(intent, GALLERY_INTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT && resultCode == RESULT_OK) {
            progressDialog.setMessage("Uploading...");
            progressDialog.show();

            image = data.getData();

            StorageReference filePath = storageRef.child("club-logos").child(clubID);
            filePath.putFile(image).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(CreateClubActivity.this, "Upload done", Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            });
        }
    }

    public void createClub(View view) {
        if (clubName.getText().toString().isEmpty() || clubDesc.getText().toString().isEmpty()) {
            Toast.makeText(CreateClubActivity.this, "You can't leave the club name or description boxes empty", Toast.LENGTH_SHORT).show();
        } else if (image == null) {
            Toast.makeText(CreateClubActivity.this, "You need to add a club logo", Toast.LENGTH_SHORT).show();
        } else {
            final FirebaseUser fa = FirebaseAuth.getInstance().getCurrentUser();

            Club club = new Club(clubName.getText().toString(), clubDesc.getText().toString(), null, fa.getUid(), isPublic.isChecked(), ServerValue.TIMESTAMP);
            databaseRef.child("clubs").child(clubID).setValue(club, new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Toast.makeText(CreateClubActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        databaseRef.child("clubs-members").child(clubID).child(fa.getUid()).setValue(true);
                        databaseRef.child("members-clubs").child(fa.getUid()).child(clubID).setValue(true);

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
