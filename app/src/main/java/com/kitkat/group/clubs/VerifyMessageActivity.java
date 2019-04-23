package com.kitkat.group.clubs;

import android.graphics.Color;
import android.os.Build;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

public class VerifyMessageActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_message);

        setStatusBarTransparent();

        boolean isFailure = getIntent().getStringExtra("failure").equalsIgnoreCase("true");

        if (isFailure) {
            ConstraintLayout cl = findViewById(R.id.verifyContainer);
            ImageView iv = findViewById(R.id.verifyImage);
            TextView tv = findViewById(R.id.verifyText);

            cl.setBackgroundColor(getResources().getColor(R.color.colorFailure));
            iv.setImageDrawable(getResources().getDrawable(R.drawable.failure));
            tv.setText("Not a member");
        }
    }

    private void setStatusBarTransparent(){
        if(Build.VERSION.SDK_INT >= 21){
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE|View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
    }
}
