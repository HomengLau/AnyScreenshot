package com.taylor.screenshortdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.nio.ByteBuffer;


public class MainActivity extends Activity  implements View.OnClickListener{

    private static final int REQUEST_MEDIA_PROJECTION = 1;
    private Intent mResultIntent = null;
    private int mResultCode = 0;
    public static final String TAG = "MainAc";
    private View mTouchView;
    boolean isCapture;
    private  MediaProjectionManager mMediaProjectionManager;
    private MediaProjection mMediaProjection;
    private ImageView mCaptureIv;
    private LinearLayout mCaptureLl;
    private ImageReader mImageReader;
    private String mImageName;
    private String mImagePath;
    private int screenDensity;
    private int windowWidth;
    private int windowHeight;
    private VirtualDisplay mVirtualDisplay;
    private WindowManager wm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mMediaProjectionManager = (MediaProjectionManager) getApplicationContext().getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        ((ScreenShortApplication) getApplication()).setMediaProjectionMgr(mMediaProjectionManager);
        mResultIntent = ((ScreenShortApplication) getApplication()).getResultIntent();
        mResultCode = ((ScreenShortApplication) getApplication()).getResultCode();
        createEnvironment();
        startIntent();
        findViewById(R.id.screenshort).setOnClickListener(this);
       // mTouchView = findViewById(R.id.touch_view);

    }

    private void startIntent() {
        if (mResultIntent != null && mResultCode != 0) {
            //startService(new Intent(getApplicationContext(), ScreenShortService.class));
            startVirtual();
            ((ScreenShortApplication)getApplication()).setCutBitmap(startCapture());
        } else {
            if (mMediaProjectionManager != null)
            startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (resultCode == RESULT_OK) {
                Log.e(TAG,"get capture permission success!");
                mResultCode = resultCode;
                mResultIntent = data;
                ((ScreenShortApplication) getApplication()).setResultCode(resultCode);
                ((ScreenShortApplication) getApplication()).setResultIntent(data);
                ((ScreenShortApplication) getApplication()).setMediaProjectionMgr(mMediaProjectionManager);
                startIntent();
                //startService(new Intent(getApplicationContext(),ScreenShortService.class));

            }
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.screenshort:
                intent.setClass(getApplicationContext(),ScreenShortActivity.class);
                startActivity(intent);
        }
    }
    private void createEnvironment() {
        //mImagePath = Environment.getExternalStorageDirectory().getPath() + "/screenshort/";
        mMediaProjectionManager = ((ScreenShortApplication) getApplication()).getMediaProjectionMgr();
        wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        windowWidth = 1920; //wm.getDefaultDisplay().getWidth();
        windowHeight = 1200; //wm.getDefaultDisplay().getHeight();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(displayMetrics);
        screenDensity = displayMetrics.densityDpi;
        mImageReader = ImageReader.newInstance(windowWidth, windowHeight, 0x1, 2);

    }
    private void stopVirtual() {
        if (mVirtualDisplay != null) {
            mVirtualDisplay.release();
            mVirtualDisplay = null;
        }
    }


    public Bitmap startCapture() {
        // mImageName = System.currentTimeMillis() + ".png";
        Log.e(TAG, "image name is : " + mImageName);
        if (mImageReader == null)
            return null;
        Image image = mImageReader.acquireLatestImage();
        if (image == null){
            return null;
        }
        int width = image.getWidth();
        int height = image.getHeight();
        final Image.Plane[] planes = image.getPlanes();
        final ByteBuffer buffer = planes[0].getBuffer();
        int pixelStride = planes[0].getPixelStride();
        int rowStride = planes[0].getRowStride();
        int rowPadding = rowStride - pixelStride * width;
        Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
        bitmap.copyPixelsFromBuffer(buffer);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        image.close();

        return bitmap;
    }

    private void startVirtual() {
        if (mMediaProjection != null) {
            virtualDisplay();
        } else {
            setUpMediaProjection();
            virtualDisplay();
        }
    }

    private void setUpMediaProjection() {
        int resultCode = ((ScreenShortApplication) getApplication()).getResultCode();
        Intent data = ((ScreenShortApplication) getApplication()).getResultIntent();
        mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
    }
    private void virtualDisplay() {
        mVirtualDisplay = mMediaProjection.createVirtualDisplay("capture_screen", windowWidth, windowHeight, screenDensity,
                DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR, mImageReader.getSurface(), null, null);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCaptureLl != null) {
            wm.removeView(mCaptureLl);
        }
        if (mMediaProjection != null) {
            mMediaProjection.stop();
            mMediaProjection = null;
        }
        stopVirtual();
    }


}
