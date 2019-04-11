package com.kitkat.group.clubs;

import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class VerifyMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_message);

        boolean isFailure = getIntent().getStringExtra("failure").equalsIgnoreCase("true");

        if (isFailure) {
            ConstraintLayout cl = findViewById(R.id.verifyContainer);
            ImageView iv = findViewById(R.id.verifyImage);
            TextView tv = findViewById(R.id.verifyText);

            cl.setBackgroundColor(getResources().getColor(R.color.colorFailure));
            iv.setImageDrawable(getResources().getDrawable(R.drawable.failure));
            tv.setText("This user is not in the club");
        }
    }
}
