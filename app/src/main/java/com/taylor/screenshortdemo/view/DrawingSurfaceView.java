package com.taylor.screenshortdemo.view;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Environment;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;

import com.taylor.screenshortdemo.ScreenShortApplication;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

/**
 * Created by liuhq13 on 2017/6/9.
 */

public class DrawingSurfaceView extends SurfaceView{
    private Activity mContex;
    private float mX;
    private float mY;

    private SurfaceHolder sfh;
    private Canvas canvas;
    private float mCurveEndX;
    private float mCurveEndY;

    private final Paint mGesturePaint = new Paint();
    private final Path mPath = new Path();
    private final Rect mInvalidRect = new Rect();

    private boolean isDrawing;
    private Bitmap mBitmap;

    public DrawingSurfaceView(Activity context) {
        super(context);
        mContex = context;
        sfh = getHolder();
        mGesturePaint.setAntiAlias(true);
        mGesturePaint.setStyle(Style.STROKE);
        mGesturePaint.setStrokeWidth(5);
        mGesturePaint.setColor(Color.RED);
        setBackgroundColor(Color.TRANSPARENT);
        setZOrderOnTop(true);//使surfaceview放到最顶层
        getHolder().setFormat(PixelFormat.TRANSLUCENT);//使窗口支持透明度
        // TODO Auto-generated constructor stub
    }

    Bitmap cutBitmap;

    public void drawCanvas() {
        Log.d("liuhq13","==========>drawCanvas(0");
        try {
            canvas = sfh.lockCanvas();
            if (canvas != null && ((ScreenShortApplication)(mContex.getApplication())).getCutBitmap() !=null) {
                canvas.drawBitmap(((ScreenShortApplication)(mContex.getApplication())).getCutBitmap() , 0 , 0, mGesturePaint);
                canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);//绘制透明色*/
                canvas.drawPath(mPath, mGesturePaint);
                //mGesturePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                Log.d("liuhq13", "bitmap cuted third");
            }
        } catch (Exception e) {
            // TODO: handle exception
        } finally {
            if (canvas != null)
                sfh.unlockCanvasAndPost(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        // TODO Auto-generated method stub
        switch (event.getAction())
        {
            case MotionEvent.ACTION_DOWN:
                touchDown(event);
                invalidate();
                return true;

            case MotionEvent.ACTION_MOVE:
                if (isDrawing) {
                    Rect rect = touchMove(event);
                    if (rect != null) {
                        invalidate(rect);
                    }
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isDrawing){
                    touchUp(event);
                    invalidate();
                    Log.d("liuhq13","Canvas.left = ");
                    return true;
                }
                break;
        }
        return super.onTouchEvent(event);
    }

    private void touchDown(MotionEvent event)
    {
        isDrawing = true;
        mPath.reset();
        float x = event.getX();
        float y = event.getY();

        mX = x;
        mY = y;

        mPath.moveTo(x, y);

        mInvalidRect.set((int) x, (int) y, (int) x , (int) y);
        mCurveEndX = x;
        mCurveEndY = y;
    }

    private Rect touchMove(MotionEvent event)
    {
        Rect areaToRefresh = null;

        final float x = event.getX();
        final float y = event.getY();

        final float previousX = mX;
        final float previousY = mY;

        final float dx = Math.abs(x - previousX);
        final float dy = Math.abs(y - previousY);

        if (dx >= 3 || dy >= 3)
        {
            areaToRefresh = mInvalidRect;
            areaToRefresh.set((int) mCurveEndX , (int) mCurveEndY ,
                    (int) mCurveEndX, (int) mCurveEndY);

            //设置贝塞尔曲线的操作点为起点和终点的一半
            float cX = mCurveEndX = (x + previousX) / 2;
            float cY = mCurveEndY = (y + previousY) / 2;

            //实现绘制贝塞尔平滑曲线；previousX, previousY为操作点，cX, cY为终点
            mPath.quadTo(previousX, previousY, cX, cY);
            //mPath.lineTo(x, y);

            // union with the control point of the new curve
            /*areaToRefresh矩形扩大了border(宽和高扩大了两倍border)，
             * border值由设置手势画笔粗细值决定
             */
            areaToRefresh.union((int) previousX, (int) previousY,
                    (int) previousX, (int) previousY);
           /* areaToRefresh.union((int) x, (int) y,
                    (int) x, (int) y);*/


            // union with the end point of the new curve
            areaToRefresh.union((int) cX, (int) cY ,
                    (int) cX, (int) cY);

            //第二次执行时，第一次结束调用的坐标值将作为第二次调用的初始坐标值
            mX = x;
            mY = y;
            drawCanvas();
        }
        return areaToRefresh;
    }

    private void touchUp(MotionEvent event)
    {
        isDrawing = false;
    }

    private Bitmap getScreenShot(){
        Bitmap newb = Bitmap.createBitmap( 320, 480, Bitmap.Config.ARGB_8888 );

        Canvas canvas = new Canvas(newb);

        ViewGroup rootView = (ViewGroup) mContex.getWindow().getDecorView().getRootView();
        rootView.draw(canvas);

        File file = new File(Environment.getExternalStorageDirectory()+ "/" + "1.png");

        FileOutputStream f = null;
        try {
            f = new FileOutputStream(file);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        boolean b = newb.compress(Bitmap.CompressFormat.PNG, 100, f);
        if (b){
            return newb;
        }else{
            return  null;
        }

    }
}
