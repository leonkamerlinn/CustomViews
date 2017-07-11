package com.leon.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Leon on 2.7.2017..
 */

public class CustomView extends View implements View.OnTouchListener {

    private GestureDetector mGestureDetector;
    private GestureListener mGesture;
    private float aX, aY, bX, bY;
    private Path mPath;
    private Paint mPaint;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private Paint mBitmapPaint;
    private static final float TOUCH_TOLERANCE = 4;

    public CustomView(Context context) {
        super(context);
        init(null);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(attrs);
    }

    private void init(@Nullable AttributeSet attributeSet) {
        Context context1 = getContext();
        mGesture = new GestureListener();
        mGestureDetector = new GestureDetector(context1, mGesture);
        mPath = new Path();
        mPaint = new Paint(Color.RED);
        mBitmapPaint = new Paint(Paint.DITHER_FLAG);
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        Paint paint = new Paint();
        Rect rect = new Rect();


        rect.left = 20;
        rect.top = 20;

        rect.bottom = 120;
        rect.right = 120;
        paint.setColor(Color.GREEN);

        canvas.drawRect(rect, paint);
        canvas.drawLine(20, 20, 120, 120, new Paint(Color.RED));


        //canvas.drawBitmap();
        canvas.drawPath(mPath,  mPaint);
    }

    private void startDraw(float x, float y) {
        mPath.reset();
        mPath.moveTo(x, y);
        aX = x;
        aY = y;
    }

    private void moveDraw(float x, float y) {
        float dx = Math.abs(x - aX);
        float dy = Math.abs(y - aY);

        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            mPath.quadTo(aX, aY, (x + aX) / 2, (y + aY) / 2);
            aX = x;
            aY = y;
        }




    }


    private void endDraw(float x, float y) {
        mPath.lineTo(x, y);

        // commit the path to our offscreen
        mCanvas.drawPath(mPath,  mPaint);
        // kill this so we don't double draw
        mPath.reset();



    }






    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
        float x = motionEvent.getX();
        float y = motionEvent.getY();


        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startDraw(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_MOVE:
                moveDraw(x, y);
                invalidate();
                break;

            case MotionEvent.ACTION_UP:
                endDraw(x, y);
                invalidate();
                break;

        }



        //mGestureDetector.onTouchEvent(motionEvent);
        //draw(aX, aY, bX, bY, mCanvas);

        return true;
    }




    private class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public void onLongPress(MotionEvent e) {
            float x = e.getX();
            float y = e.getY();

            System.out.println("onLongPress");
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            System.out.println("onSingleTapUp");
            return true;
        }

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            System.out.println("onSingleTapConfirmed");
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            System.out.println("onDoubleTap");
            return true;
        }

        @Override
        public boolean onDown(MotionEvent e) {
            System.out.println("onDown");

            aX = e.getX();
            aY = e.getY();
            return true;
        }

        @Override
        public void onShowPress(MotionEvent e) {
            System.out.println("MotionEvent");
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            System.out.println("onScrolle1: " + e1.getX());
            System.out.println("onScroll:e2 " + e2.getX());
            System.out.println("onScrolldistance: " + distanceX);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            System.out.println("onFling: " + e1.getX());
            return true;
        }

        @Override
        public boolean onDoubleTapEvent(MotionEvent e) {
            System.out.println("onDoubleTapEvent");
            return true;
        }

        @Override
        public boolean onContextClick(MotionEvent e) {
            return super.onContextClick(e);
        }
    }
}


