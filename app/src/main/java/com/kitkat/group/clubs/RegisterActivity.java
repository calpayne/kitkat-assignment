package com.kitkat.group.clubs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity {

    private DatabaseReference databaseRef;
    private TextView username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final FirebaseUser fa = FirebaseAuth.getInstance().getCurrentUser();
        databaseRef = FirebaseDatabase.getInstance().getReference();
        username = findViewById(R.id.username);
    }

    public void btnContinue(View view) {
        if (username.getText().toString().isEmpty()) {
            Toast.makeText(RegisterActivity.this, "You can't leave the username field empty", Toast.LENGTH_SHORT).show();
        } else if (username.getText().toString().length() > 20 || username.getText().toString().length() < 3) {
            Toast.makeText(RegisterActivity.this, "Your username needs to be between 3 and 20 characters", Toast.LENGTH_SHORT).show();
        } else {
            final FirebaseUser fa = FirebaseAuth.getInstance().getCurrentUser();

            databaseRef.child("users").child(fa.getUid()).setValue(username.getText().toString(), new DatabaseReference.CompletionListener() {
                @Override
                public void onComplete(@Nullable DatabaseError databaseError, @NonNull DatabaseReference databaseReference) {
                    if (databaseError != null) {
                        Toast.makeText(RegisterActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Successfully registered", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }
            });
        }
    }
}
