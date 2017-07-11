package com.leon.customviews;

import android.graphics.Point;
import android.os.Bundle;

import android.os.Parcelable;
import android.os.PersistableBundle;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
import com.leon.customviews.parcelables.DrawingParcelable;
import com.leon.customviews.parcelables.Foo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Created by Leon on 3.7.2017..
 */

public class DrawActivity extends AppCompatActivity {
    DrawingView mDrawingView;
    private PaintView mPaintView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDrawingView = new DrawingView(this);
        mPaintView = new PaintView(this);


        List<String> someList = new ArrayList<String>();

        for (Iterator<String> i = someList.iterator(); i.hasNext();) {
            String item = i.next();
            System.out.println(item);
        }
        setContentView(mPaintView);
        if (savedInstanceState != null) {
           // ArrayList<Parcelable> pList = savedInstanceState.getParcelableArrayList("data");
            //mDrawingView.init(pList);
        }


    }










}