package com.example.dell.diary;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class WelcomeActivity extends AppCompatActivity {

    private final int SPLASH_DISPLAY_LENGHT = 1000;  //延迟秒

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

//        Intent intent = new Intent(WelcomeActivity.this, TimeLineActivity.class);
//        startActivity(intent);
        Log.d("welcome","????");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_DISPLAY_LENGHT);
    }

}
