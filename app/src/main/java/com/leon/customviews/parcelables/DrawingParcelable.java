package com.leon.customviews.parcelables;

import android.graphics.Point;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by Leon on 6.7.2017..
 */

public class DrawingParcelable implements Parcelable {

    public ArrayList<Point> mPoints = new ArrayList<>();

    public DrawingParcelable(ArrayList<Point> points) {
        mPoints = points;
    }


    public DrawingParcelable(Parcel in) {
        mPoints = in.createTypedArrayList(Point.CREATOR);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        for(Point p: mPoints) {

        }
    }

    public static final Creator<DrawingParcelable> CREATOR = new Creator<DrawingParcelable>() {
        @Override
        public DrawingParcelable createFromParcel(Parcel in) {
            return new DrawingParcelable(in);
        }

        @Override
        public DrawingParcelable[] newArray(int size) {
            return new DrawingParcelable[size];
        }
    };
}
