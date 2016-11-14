package com.bitebidaquan.bitdata.testexoplayer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Nathen on 2016/11/1.
 */

public class SecActivity extends AppCompatActivity {
    final String LOG_TAG = "TestChangeSurface";
    ViewGroup vg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("bbbbb");
        setContentView(R.layout.activity_sec);
        vg = (ViewGroup) findViewById(R.id.root);
        vg.addView(MainActivity.textureView);
        vg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.textureView.setSurfaceTexture(MainActivity.mSavedSurfaceTexture);
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        vg.removeView(MainActivity.textureView);
    }
}
