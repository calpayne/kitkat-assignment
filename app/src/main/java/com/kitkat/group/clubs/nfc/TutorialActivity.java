package com.kitkat.group.clubs.nfc;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.kitkat.group.clubs.R;

public class TutorialActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout layoutDot;
    TextView[]dotstv;
    private int[]layouts;
    private Button btnSkip;
    private Button btnNext;
    MyPagerAdapter pagerAdapter;
    String clubId, userId, clubName, outMessage, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        clubName = getIntent().getStringExtra("clubName");
        clubId = getIntent().getStringExtra("clubId");
        userId = getIntent().getStringExtra("userId");
        userName = getIntent().getStringExtra("userName");

        if(!isFirstTimeStartApp()){
            startSenderActivity();
            finish();
        }



        setStatusBarTransparent();

        setContentView(R.layout.activity_tutorial);


        viewPager = findViewById(R.id.view_pager_tutorial);
        layoutDot = findViewById(R.id.dotLayout);
        btnNext = findViewById(R.id.btn_next);
        btnSkip = findViewById(R.id.btn_skip);

        btnSkip.setOnClickListener(v -> startSenderActivity());

        btnNext.setOnClickListener(v -> {

            int currentPage = viewPager.getCurrentItem()+1;
            if(currentPage<layouts.length){
                viewPager.setCurrentItem(currentPage);
            }
            else{
                startSenderActivity();
            }
        });

        layouts = new int[]{R.layout.tutorial_slider1,R.layout.tutorial_slider2,R.layout.tutorial_slider3,R.layout.tutorial_slider4};
        pagerAdapter = new MyPagerAdapter(layouts,getApplicationContext());
        viewPager.setAdapter(pagerAdapter);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void onPageSelected(int position) {
                if(position == layouts.length-1){
                    btnNext.setText("START");
                    btnSkip.setVisibility(View.GONE);
                }
                else{
                    btnNext.setText("NEXT");
                    btnSkip.setVisibility(View.VISIBLE);
                }
                setDotStatus(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setDotStatus(0);
    }

    private boolean isFirstTimeStartApp(){
        SharedPreferences ref = getApplicationContext().getSharedPreferences("IntroSliderApp", Context.MODE_PRIVATE);
        return ref.getBoolean("FirstTimeStartFlag",true);
    }

    private void setFirstTimeStartStatus(){
        SharedPreferences ref = getApplicationContext().getSharedPreferences("IntroSliderApp", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = ref.edit();
        editor.putBoolean("FirstTimeStartFlag", true);
        editor.apply();
    }

    private void setDotStatus(int page){
        layoutDot.removeAllViews();
        dotstv = new TextView[layouts.length];
        for(int i=0;i<dotstv.length;i++){
            dotstv[i]= new TextView(this);
            dotstv[i].setText(Html.fromHtml("&#8226;"));
            dotstv[i].setTextSize(30);
            dotstv[i].setTextColor(Color.parseColor("#a9b4bb"));
            layoutDot.addView(dotstv[i]);
        }
        if(dotstv.length>0){
            dotstv[page].setTextColor(Color.parseColor("#ffffff"));
        }
    }
    private void startSenderActivity(){
        setFirstTimeStartStatus();
        Intent intent = new Intent(TutorialActivity.this,SenderActivity.class);
        intent.putExtra("clubId", clubId);
        intent.putExtra("clubName", clubName);
        intent.putExtra("userId",userId);
        intent.putExtra("userName",userName);
        startActivity(intent);
        finish();
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
