package com.lzq.adream.widget.layout;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

/**
 * Created by ${廖昭启} on 2017/11/3.
 */

public class RightLinearLayout extends LinearLayout {
    private DrawarLayout  mDragLayout;
    public RightLinearLayout(Context context) {
        super(context);
    }

    public RightLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public RightLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    public void setDraglayout(DrawarLayout mDragLayout){
        this.mDragLayout = mDragLayout;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 如果当前是关闭状态, 按之前方法判断
        if(mDragLayout.getStatus() == DrawarLayout.Status.Close){
            return super.onInterceptTouchEvent(ev);
        }else {
            return true;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // 如果当前是关闭状态, 按之前方法处理
        if(mDragLayout.getStatus() == DrawarLayout.Status.Close){
            return super.onTouchEvent(event);
        }else {
            // 手指抬起, 执行关闭操作
            if(event.getAction() == MotionEvent.ACTION_UP){
              //  mDragLayout.close();
            }

            return true;
        }
    }

}
