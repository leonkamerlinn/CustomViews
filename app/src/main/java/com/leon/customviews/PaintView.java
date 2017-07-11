package com.leon.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Leon on 7.7.2017..
 */

public class PaintView extends View {
    public static final int MAX_FINGERS = 5;
    private Path[] mFingerPaths = new Path[MAX_FINGERS];
    private Path mShapePath;
    private Paint mFingerPaint;
    private ArrayList<Path> mCompletedPaths;
    private RectF mPathBounds = new RectF();
    private ArrayList<MotionEvent> mEventList;
    private Point mStartPoint, mEndPoint;

    public PaintView(Context context) {
        super(context);
        init();
    }

    public PaintView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PaintView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }


    private void init() {
        mCompletedPaths = new ArrayList<>();
        mFingerPaint = new Paint();
        mFingerPaint.setAntiAlias(true);
        mFingerPaint.setColor(Color.BLUE);
        mFingerPaint.setStyle(Paint.Style.STROKE);
        mFingerPaint.setStrokeWidth(12);
        mFingerPaint.setStrokeCap(Paint.Cap.BUTT);
        mEventList = new ArrayList<>();
        setId(R.id.myCustomView);
        setSaveEnabled(true);
    }

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

        if (mShapePath != null) {
            canvas.drawPath(mShapePath, mFingerPaint);
        }


    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getActionMasked();
        getParent().requestDisallowInterceptTouchEvent(true);
        switch (action) {
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

    private void performEvent(MotionEvent event) {

        fingerDraw(event);
        //circleDraw(event);
    }

    private void fingerDraw(MotionEvent event) {
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

        } else if (action == MotionEvent.ACTION_MOVE && id < MAX_FINGERS) {
            for(int i = 0; i < cappedPointerCount; i++) {
                if(mFingerPaths[i] != null) {
                    int index = event.findPointerIndex(i);
                    mFingerPaths[i].lineTo(event.getX(index), event.getY(index));
                    mFingerPaths[i].computeBounds(mPathBounds, true);
                    invalidate((int) mPathBounds.left, (int) mPathBounds.top, (int) mPathBounds.right, (int) mPathBounds.bottom);
                }
            }
        }




    }




    private void circleDraw(MotionEvent event) {
        int pointerCount = event.getPointerCount();
        int cappedPointerCount = pointerCount > MAX_FINGERS ? MAX_FINGERS : pointerCount;
        int actionIndex = event.getActionIndex();
        int action = event.getActionMasked();
        int id = event.getPointerId(actionIndex);
        float x = event.getX(actionIndex);
        float y = event.getY(actionIndex);



        if ((action == MotionEvent.ACTION_DOWN || action == MotionEvent.ACTION_POINTER_DOWN)) {


            if (id == 0) {
                mShapePath = new Path();
                mStartPoint = new Point((int)x, (int)y);
                mEndPoint = new Point((int)x, (int)y);


            } else if (id == 1) {
                mEndPoint.x = (int)x;
                mEndPoint.y = (int)y;
            }

            mShapePath.reset();
            mShapePath.addCircle(mStartPoint.x, mStartPoint.y ,distanceBetweenTwoPoints(mStartPoint, mEndPoint), Path.Direction.CW);





        } else if ((action == MotionEvent.ACTION_POINTER_UP || action == MotionEvent.ACTION_UP)) {
            if (id == 0) {

                    mStartPoint.x = (int)x;
                    mStartPoint.y = (int)y;

            } else if (id == 1) {
                mEndPoint.x = (int)x;
                mEndPoint.y = (int)y;
            }

            mShapePath.reset();
            mShapePath.addCircle(mStartPoint.x, mStartPoint.y ,distanceBetweenTwoPoints(mStartPoint, mEndPoint), Path.Direction.CW);
            mShapePath.computeBounds(mPathBounds, true);
            invalidate((int) mPathBounds.left, (int) mPathBounds.top, (int) mPathBounds.right, (int) mPathBounds.bottom);
            mCompletedPaths.add(mShapePath);
            //mShapePath = null;




        } else if(action == MotionEvent.ACTION_MOVE) {
            if (id == 0) {



                mStartPoint.x = (int)x;
                mStartPoint.y = (int)y;

            } else if (id == 1) {
                mEndPoint.x = (int)x;
                mEndPoint.y = (int)y;
            }

            mShapePath.reset();
            mShapePath.addCircle(mStartPoint.x, mStartPoint.y ,distanceBetweenTwoPoints(mStartPoint, mEndPoint), Path.Direction.CW);
            mShapePath.computeBounds(mPathBounds, true);
            invalidate((int) mPathBounds.left, (int) mPathBounds.top, (int) mPathBounds.right, (int) mPathBounds.bottom);
        }








    }
    private float distanceBetweenTwoPoints(Point a, Point b) {
        return (float) Math.sqrt((a.x-b.x)*(a.x-b.x) + (a.y-b.y)*(a.y-b.y));
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
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        for(MotionEvent event: mEventList) {
            performEvent(event);
        }
    }

    static class SavedState extends BaseSavedState {
        public ArrayList<MotionEvent> eventList;


        SavedState(Parcelable superState) {
            super(superState);
        }


        @Override
        public void writeToParcel(Parcel out, int flags) {
            super.writeToParcel(out, flags);
        }

        //required field that makes Parcelables from a Parcel
        public static final Parcelable.Creator<DrawingView.SavedState> CREATOR = new Parcelable.Creator<DrawingView.SavedState>() {
            public DrawingView.SavedState createFromParcel(Parcel in) {
                return new DrawingView.SavedState(in);
            }
            public DrawingView.SavedState[] newArray(int size) {
                return new DrawingView.SavedState[size];
            }
        };
    }



}

