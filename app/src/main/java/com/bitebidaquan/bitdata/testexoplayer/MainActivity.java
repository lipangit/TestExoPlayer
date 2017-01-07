package com.bitebidaquan.bitdata.testexoplayer;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.LinearLayout;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener, MediaPlayer.OnPreparedListener {
    public static TextureView textureView;
    LinearLayout root;
    private MediaPlayer player;
    Uri contentUri = Uri.parse("http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4");
    public static SurfaceTexture mSavedSurfaceTexture;
    final String LOG_TAG = "TestChangeSurface";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("aaaaa");
        setContentView(R.layout.activity_main);
        root = (LinearLayout) findViewById(R.id.root);
        textureView = new TextureView(this);
        textureView.setSurfaceTextureListener(this);

        player = new MediaPlayer();
        player.setAudioStreamType(AudioManager.STREAM_MUSIC);
//              mediaPlayer.setDataSource(context, Uri.parse(url), mapHeadData);
        try {
            player.setDataSource(MainActivity.this, contentUri);
        } catch (IOException e) {
            e.printStackTrace();
        }
        player.setOnPreparedListener(MainActivity.this);
        player.setScreenOnWhilePlaying(true);
        player.prepareAsync();
        textureView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                root.removeView(textureView);
                startActivity(new Intent(MainActivity.this, SecActivity.class));
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        root.addView(textureView);
    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.d(LOG_TAG, "onSurfaceTextureAvailable 1");
        if (mSavedSurfaceTexture == null) {
            mSavedSurfaceTexture = surface;
            player.setSurface(new Surface(mSavedSurfaceTexture));
        } else {
            textureView.setSurfaceTexture(mSavedSurfaceTexture);
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.d(LOG_TAG, "onSurfaceTextureDestroyed 1");
        return (mSavedSurfaceTexture == null);
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy ");
    }

    /**
     * Called when the media file is ready for playback.
     *
     * @param mp the MediaPlayer that is ready for playback
     */
    @Override
    public void onPrepared(MediaPlayer mp) {
        player.start();
        player.setSurface(new Surface(mSavedSurfaceTexture));
    }
}
