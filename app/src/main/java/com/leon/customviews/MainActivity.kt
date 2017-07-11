package com.leon.customviews

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import java.util.ArrayList


class MainActivity : AppCompatActivity() {



    private var mCustomView: CustomView? = null
    private var mPoints: ArrayList<Float>? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mCustomView = findViewById(R.id.customView) as CustomView
        mCustomView!!.setOnTouchListener(mCustomView)

        mPoints = ArrayList<Float>()


    }






}
