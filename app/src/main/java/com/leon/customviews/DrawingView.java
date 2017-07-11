package com.leon.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Build;
import android.os.Handler;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.EventLog;
import android.view.ActionMode;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.IllegalFormatCodePointException;
import java.util.concurrent.TimeUnit;

/**
 * Created by Leon on 3.7.2017..
 */

public class DrawingView extends View {
    private static final float TOUCH_TOLERANCE = 4;
    public static final int MAX_FINGERS = 5;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    //private Paint mBitmapPaint, mPaint;
    private float mX, mY;
    private Context mContext;
    private ArrayList<MotionEvent> mEventList;
    private Path[] mFingerPaths = new Path[MAX_FINGERS];
    private RectF mPathBounds = new RectF();
    private ArrayList<Path> mCompletedPaths;
    private Paint mFingerPaint;


    public DrawingView(Context context) {
        super(context);
        //init(context);
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        //init(context);
    }

    public DrawingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //init(context);
    }





    /*@Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        for(MotionEvent event: mEventList) {
           performEvent(event);
        }

    }*/



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (Path completedPath : mCompletedPaths) {
            canvas.drawPath(completedPath, mFingerPaint);
        }

        for (Path fingerPath : mFingerPaths) {
            if (fingerPath != null) {
                canvas.drawPath(fingerPath, mFingerPaint);
            }
        }
    }


    @Override
    public Parcelable onSaveInstanceState() {
        //begin boilerplate code that allows parent classes to save state
        Parcelable superState = super.onSaveInstanceState();

        SavedState savedState = new SavedState(superState);
        //end



        savedState.eventList = mEventList;
        return savedState;
    }



    @Override
    public void onRestoreInstanceState(Parcelable state) {

        SavedState savedState = (SavedState)state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mEventList = savedState.eventList;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_POINTER_UP:
                performEvent(event);
                mEventList.add(MotionEvent.obtain(event));
        }
        return true;
    }

    private void init(Context context) {
        mContext=context;
        /*mBitmapPaint = new Paint(Paint.DITHER_FLAG);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setColor(Color.BLUE);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(12);*/
        mEventList = new ArrayList<>();
        mCompletedPaths = new ArrayList<Path>();

        setId(R.id.myCustomView);
        setSaveEnabled(true);

    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        mCompletedPaths = new ArrayList<Path>();
        mFingerPaint = new Paint();
        mFingerPaint.setAntiAlias(true);
        mFingerPaint.setColor(Color.BLACK);
        mFingerPaint.setStyle(Paint.Style.STROKE);
        mFingerPaint.setStrokeWidth(6);
        mFingerPaint.setStrokeCap(Paint.Cap.BUTT);
        mEventList = new ArrayList<>();
    }


    private void startDraw(float x, float y, Path path) {
        path.reset();
        path.moveTo(x, y);
        mX = x;
        mY = y;

    }

    private void moveDraw(float x, float y, Path path) {
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if (dx >= TOUCH_TOLERANCE || dy >= TOUCH_TOLERANCE) {
            path.quadTo(mX, mY, (x + mX)/2, (y + mY)/2);
            mX = x;
            mY = y;
        }

    }

    private void endDraw(float x, float y, Path path) {

        path.lineTo(x, y);
        // commit the path to our offscreen
        mCanvas.drawPath(path,  mFingerPaint);
        // kill this so we don't double draw
        path.reset();


    }

    private void performEvent(MotionEvent event) {


        int pointerCount = event.getPointerCount();
        int cappedPointerCount = pointerCount > MAX_FINGERS ? MAX_FINGERS : pointerCount;
        int actionIndex = event.getActionIndex();
        int action = event.getActionMasked();
        int id = event.getPointerId(actionIndex);

        if ((action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN) && id < MAX_FINGERS) {

            mFingerPaths[id] = new Path();
            mFingerPaths[id].moveTo(event.getX(actionIndex), event.getY(actionIndex));


        } else if ((action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_UP) && id < MAX_FINGERS) {


            mFingerPaths[id].setLastPoint(event.getX(actionIndex), event.getY(actionIndex));
            mCompletedPaths.add(mFingerPaths[id]);
            mFingerPaths[id].computeBounds(mPathBounds, true);
            invalidate((int) mPathBounds.left, (int) mPathBounds.top, (int) mPathBounds.right, (int) mPathBounds.bottom);
            mFingerPaths[id] = null;

        }

        for(int i = 0; i < cappedPointerCount; i++) {
            if(mFingerPaths[i] != null) {
                int index = event.findPointerIndex(i);
                mFingerPaths[i].lineTo(event.getX(index), event.getY(index));
                mFingerPaths[i].computeBounds(mPathBounds, true);
                invalidate((int) mPathBounds.left, (int) mPathBounds.top,
                        (int) mPathBounds.right, (int) mPathBounds.bottom);
            }
        }


    }


    static class SavedState extends BaseSavedState {
        public ArrayList<MotionEvent> eventList;


        SavedState(Parcelable superState) {
            super(superState);
        }



        public SavedState(Parcel in) {
            super(in);
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
    }
}