package com.bitebidaquan.bitdata.testexoplayer;

import android.content.Intent;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.widget.LinearLayout;

import com.bitebidaquan.bitdata.testexoplayer.player.DashRendererBuilder;
import com.bitebidaquan.bitdata.testexoplayer.player.DemoPlayer;
import com.bitebidaquan.bitdata.testexoplayer.player.ExtractorRendererBuilder;
import com.bitebidaquan.bitdata.testexoplayer.player.HlsRendererBuilder;
import com.bitebidaquan.bitdata.testexoplayer.player.SmoothStreamingRendererBuilder;
import com.bitebidaquan.bitdata.testexoplayer.player.SmoothStreamingTestMediaDrmCallback;
import com.bitebidaquan.bitdata.testexoplayer.player.WidevineTestMediaDrmCallback;
import com.google.android.exoplayer.metadata.id3.Id3Frame;
import com.google.android.exoplayer.text.Cue;
import com.google.android.exoplayer.util.Util;

import java.util.List;

public class MainActivity extends AppCompatActivity implements TextureView.SurfaceTextureListener {
    public static TextureView textureView;
    LinearLayout root;
    private DemoPlayer player;
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

        player = new DemoPlayer(getRendererBuilder());
        player.prepare();
        player.setPlayWhenReady(true);
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
            new Thread(new Runnable() {
                @Override
                public void run() {
//                    try {
//                        Thread.sleep(0);
//                    } catch (InterruptedException e) {
//                        e.printStackTrace();
//                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textureView.setSurfaceTexture(mSavedSurfaceTexture);
                        }
                    });
                }
            }).start();
        }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.d(LOG_TAG, "onSurfaceTextureDestroyed 1");
//        SecActivity.tv.setSurfaceTexture(mSavedSurfaceTexture);
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


    private static int inferContentType(Uri uri, String fileExtension) {
        String lastPathSegment = !TextUtils.isEmpty(fileExtension) ? "." + fileExtension
                : uri.getLastPathSegment();
        return Util.inferContentType(lastPathSegment);
    }

    private DemoPlayer.RendererBuilder getRendererBuilder() {
        String userAgent = Util.getUserAgent(this, "ExoPlayerDemo");
        int contentType = inferContentType(contentUri, "");
        switch (contentType) {
            case Util.TYPE_SS:
                return new SmoothStreamingRendererBuilder(this, userAgent, contentUri.toString(),
                        new SmoothStreamingTestMediaDrmCallback());
            case Util.TYPE_DASH:
                return new DashRendererBuilder(this, userAgent, contentUri.toString(),
                        new WidevineTestMediaDrmCallback("", ""));
            case Util.TYPE_HLS:
                return new HlsRendererBuilder(this, userAgent, contentUri.toString());
            case Util.TYPE_OTHER:
                return new ExtractorRendererBuilder(this, userAgent, contentUri);
            default:
                throw new IllegalStateException("Unsupported type: " + contentType);
        }
    }


}
