package com.bitebidaquan.bitdata.testexoplayer;

import android.annotation.TargetApi;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Surface;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

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

public class MainActivity extends AppCompatActivity implements DemoPlayer.Listener, DemoPlayer.CaptionListener, DemoPlayer.Id3MetadataListener, TextureView.SurfaceTextureListener {
    private TextureView surfaceView, surfaceView1;
    private DemoPlayer player;
    Uri contentUri = Uri.parse("http://2449.vod.myqcloud.com/2449_43b6f696980311e59ed467f22794e792.f20.mp4");
    public static SurfaceTexture mSavedSurfaceTexture;
    final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        surfaceView = (TextureView) findViewById(R.id.surface_view);
        surfaceView1 = (TextureView) findViewById(R.id.surface_view1);

        surfaceView.setSurfaceTextureListener(this);

        player = new DemoPlayer(getRendererBuilder());
        player.addListener(this);
        player.setCaptionListener(this);
        player.setMetadataListener(this);
        player.prepare();
        player.setPlayWhenReady(true);
        surfaceView.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                if (mSavedSurfaceTexture == null) {
                    mSavedSurfaceTexture = surfaceView.getSurfaceTexture();
                }
//                surfaceView.setSurfaceTexture(mSavedSurfaceTexture);
                player.setSurface(new Surface(mSavedSurfaceTexture));

//                player.setSurface(surfaceView.getHolder().getSurface());
            }
        });
        surfaceView1.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                ((ViewGroup) surfaceView.getParent()).removeView(surfaceView);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            Thread.sleep(1000);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    surfaceView1.setSurfaceTexture(mSavedSurfaceTexture);
                                }
                            });
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();
//                player.setSurface(new Surface(mSavedSurfaceTexture));
//                player.setSurface(surfaceView1.getHolder().getSurface());
            }
        });
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

    @Override
    public void onStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onError(Exception e) {

    }

    @Override
    public void onVideoSizeChanged(int width, int height, int unappliedRotationDegrees, float pixelWidthHeightRatio) {

    }

    @Override
    public void onCues(List<Cue> cues) {

    }

    @Override
    public void onId3Metadata(List<Id3Frame> id3Frames) {

    }

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {
        Log.d(LOG_TAG, "onSurfaceTextureAvailable size=" + width + "x" + height + ", st=" + surface);
        mSavedSurfaceTexture = surface;
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture surface, int width, int height) {

    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture surface) {
        Log.d(LOG_TAG, "onSurfaceTextureDestroyed ");
        return false;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture surface) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(LOG_TAG, "onDestroy ");
    }
}
