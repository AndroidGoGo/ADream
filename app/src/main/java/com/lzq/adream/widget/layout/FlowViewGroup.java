package com.lzq.adream.widget.layout;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lzq on 2017/6/1.
 */

public class FlowViewGroup extends ViewGroup {
    private Line currentLine;
    private List<Line> lines = new ArrayList<>();
    public int mVerticalSpace = 10;

    public FlowViewGroup(Context context) {
        this(context, null);
    }

    public FlowViewGroup(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        l = getPaddingLeft();
        t = getPaddingTop();

        for (Line line : lines) {
            //每次画一行  r和b通过计算得到
            line.layout(l, t);
            t += line.mHeight;
            t += mVerticalSpace;
        }

    }
   /* 父控件onMeasure->measureChildren`measureChildWithMargin->getChildMeasureSpec->
    子控件的measure->onMeasure->setMeasureDimension->
    父控件onMeasure结束调用setMeasureDimension`保存自己的大小*/

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        lines.clear();
        currentLine = null;
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int maxWidth = width - getPaddingLeft() - getPaddingRight();
        // 测量孩子
        int childCount = getChildCount();
       // Log.d(TAG, "onMeasure: ****************"+childCount);
        for (int i = 0; i < childCount; i++) {
            View child = getChildAt(i);
            measureChild(child, widthMeasureSpec, heightMeasureSpec);
            //添加到lines ;添加到currenTline
            if (currentLine == null) {
                currentLine = new Line(maxWidth);
                lines.add(currentLine);

                currentLine.addView(child);
            } else {
                if (currentLine.canAddChild(child)) {
                    currentLine.addView(child);
                } else {
                    currentLine = new Line(maxWidth);
                    lines.add(currentLine);
                    currentLine.addView(child);
                }
            }
        }
        //通过行数的变化来计算高度
        int height = getPaddingTop();
        for (Line line : lines) {
            height += line.mHeight;
            height += mVerticalSpace;
        }
        //父控件的长宽
        setMeasuredDimension(width, height);
    }

    private class Line {
        List<View> mViews = new ArrayList<>();
        private int maxWidth;
        private int usedWidth;
        private int mSpace = 10; //不同孩子之间要有空隙
        private int mHeight;

        public Line(int maxWidth) {
            this.maxWidth = maxWidth;
        }

        //****1.currentLine添加view;;而不是实现类的添加
        //*****实现类的添加在类里面,通过getChildAt()方法得到
        public void addView(View child) {
            int measuredWidth = child.getMeasuredWidth();
            int measuredHeight = child.getMeasuredHeight();

            usedWidth += measuredWidth;
            if (mViews.size() > 0) {
                usedWidth += mSpace;
            }
            mHeight = mHeight > measuredHeight ? mHeight : measuredHeight;
            mViews.add(child);
        }

        //2.判断该行是否还可添加
        public boolean canAddChild(View child) {
            if (mViews.size() == 0) {
                return true;
            }
            int childWidth = child.getMeasuredWidth();
            if (childWidth > (maxWidth - usedWidth - mSpace)) {
                return false;
            }
            return true;
        }

        public void layout(int l, int t) {
            int avg = (maxWidth - usedWidth) / mViews.size();
            //Log.d(TAG, "layout: **************"+mViews.size());
            //mView 为每一行的View
            for (int i = 0; i < mViews.size(); i++) {
                View child = mViews.get(i);
                int measuredWidth = child.getMeasuredWidth();
                int measuredHeight = child.getMeasuredHeight();
                //让孩子重新测量，要更新宽度+avg
                int specWidth = MeasureSpec.makeMeasureSpec(measuredWidth + avg, MeasureSpec.EXACTLY);
                int specHight = MeasureSpec.makeMeasureSpec(measuredHeight, MeasureSpec.EXACTLY);
                child.measure(specWidth, specHight);
                measuredWidth = child.getMeasuredWidth();

                //确定位置
                int left = l;
                int top = t;
                int right = left + measuredWidth;
                int bottom = t + measuredHeight;
                child.layout(left, top, right, bottom);
                //在循环内累加left值
                l += measuredWidth;
                l += mSpace;
            }
        }
    }
}
