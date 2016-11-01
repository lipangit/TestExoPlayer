package com.bitebidaquan.bitdata.testexoplayer;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.TextureView;

/**
 * Created by Nathen on 2016/11/1.
 */

public class SecActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
    public static TextureView tv;
    final String LOG_TAG = "TestChangeSurface";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("bbbbb");
        setContentView(R.layout.activity_sec);
        tv = (TextureView) findViewById(R.id.sec_tv);
        tv.setSurfaceTextureListener(this);
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
//        if (!MainActivity.haveSetTexture) {
//            tv.setSurfaceTexture(MainActivity.mSavedSurfaceTexture);
//            MainActivity.haveSetTexture = true;
//        }
        Log.d(LOG_TAG, "onSurfaceTextureAvailable 2");
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.d(LOG_TAG, "onSurfaceTextureDestroyed 2");
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }
}
