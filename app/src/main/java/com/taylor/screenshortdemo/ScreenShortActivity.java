package com.taylor.screenshortdemo;

import android.app.Activity;
import android.os.Bundle;

import com.taylor.screenshortdemo.view.DrawingSurfaceView;

/**
 * Created by liuhq13 on 2017/6/17.
 */

public class ScreenShortActivity  extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new DrawingSurfaceView(this));

    }
}
