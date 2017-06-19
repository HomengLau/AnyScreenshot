package com.taylor.screenshortdemo;

import android.app.Application;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.projection.MediaProjectionManager;

/**
 * Created by liuhq13 on 2017/6/14.
 */

public class ScreenShortApplication extends Application {


    private Intent mResultIntent = null;
    private int mResultCode = 0;
    private MediaProjectionManager mMediaProjectionMgr;
    private boolean isCapture =false;
    private Bitmap cutBitmap;

    //private ScreenShortService mScreenShortService;

    /*public ScreenShortService getmScreenShortService() {
        return mScreenShortService;
    }

    public void setmScreenShortService(ScreenShortService mScreenShortService) {
        this.mScreenShortService = mScreenShortService;
    }*/

    public MediaProjectionManager getMediaProjectionMgr() {
        return mMediaProjectionMgr;
    }

    public void setMediaProjectionMgr(MediaProjectionManager mMpmngr) {
        this.mMediaProjectionMgr = mMpmngr;
    }
    public boolean isCapture() {
        return isCapture;
    }

    public void setCapture(boolean capture) {
        isCapture = capture;
    }

    public Intent getResultIntent() {
        return mResultIntent;
    }

    public void setResultIntent(Intent mResultIntent) {
        this.mResultIntent = mResultIntent;
    }

    public int getResultCode() {
        return mResultCode;
    }

    public void setResultCode(int mResultCode) {
        this.mResultCode = mResultCode;
    }

    public Bitmap getCutBitmap() {
        return cutBitmap;
    }

    public void setCutBitmap(Bitmap cutBitmap) {
        this.cutBitmap = cutBitmap;
    }




    @Override
    public void onCreate() {
        super.onCreate();
    }


}
