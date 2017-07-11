package com.leon.customviews.parcelables;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Leon on 6.7.2017..
 */

public class FreeHandLineParcelable implements Parcelable {

    protected FreeHandLineParcelable(Parcel in) {
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }

    public static final Creator<FreeHandLineParcelable> CREATOR = new Creator<FreeHandLineParcelable>() {
        @Override
        public FreeHandLineParcelable createFromParcel(Parcel in) {
            return new FreeHandLineParcelable(in);
        }

        @Override
        public FreeHandLineParcelable[] newArray(int size) {
            return new FreeHandLineParcelable[size];
        }
    };
}
