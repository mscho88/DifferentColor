package com.example.jmentertainment.differentcolor;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Kevin on 2015-07-01.
 */
public class SplashActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_splash);
        Handler handler = new Handler () {
            @Override
            public void handleMessage(Message msg)
            {
                super.handleMessage(msg);
                finish();
            }
        };
        handler.sendEmptyMessageDelayed(0, 3000);
    }
}