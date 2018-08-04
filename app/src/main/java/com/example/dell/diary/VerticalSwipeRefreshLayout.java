package com.example.dell.diary;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewConfiguration;

/**
 * Created by kx63 on 2018/8/4.
 */

public class VerticalSwipeRefreshLayout extends SwipeRefreshLayout {
    private int scaleTouchSlop;
    private float preX;
    public VerticalSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        scaleTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                preX = ev.getX();
                break;

            case MotionEvent.ACTION_MOVE:
                float moveX = ev.getX();
                float instanceX = Math.abs(moveX - preX);
                Log.i("refresh...", "move: instanceX:" + instanceX + "=(moveX:" + moveX + " - preX:" + preX + ") , scaleTouchSlop:" + scaleTouchSlop);

                // 容差值大概是24，再加上60
                if (instanceX > scaleTouchSlop + 60) {
                    return false;
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
}