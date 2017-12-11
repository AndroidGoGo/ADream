package com.lzq.adream.widget.layout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.lzq.adream.R;
import com.lzq.adream.utils.forview.ColorUtils;
import com.lzq.adream.utils.forview.EvaluateUtils;
import com.nineoldandroids.view.ViewHelper;

/**
 * Created by ${廖昭启} on 2017/11/3.
 */

public class DrawarMenuLayout extends RelativeLayout {
    private final float mSensitivity = 1.0f;
    private ViewDragHelper mDragHelper;
    private ViewGroup mLeftContent;//左边容器
    private ViewGroup mMainContent;//右边容器
    private Status mStatus = Status.Close;//初始抽屉状态
    private volatile int mRange;

    private boolean mToogle = true;
    private int mXMLRange;
    private boolean first = true;
    private int mHeight;
    private int mWidth;


    public DrawarMenuLayout(@NonNull Context context) {
        this(context,null);


    }

    public DrawarMenuLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttars(context,attrs);
        mDragHelper = ViewDragHelper.create(this, mSensitivity, mCallback);
    }



    private void initAttars(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.DragLayout);
        mXMLRange = typedArray.getInteger(R.styleable.DragLayout_range, 0);
        typedArray.recycle();
    }

    /***
     * 将事件交给mDragHelper处理
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        try {
            mDragHelper.processTouchEvent(event);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // 返回true, 持续接受事件
        return true;
    }

    // b.传递触摸事件
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // 传递给mDragHelper
        return mDragHelper.shouldInterceptTouchEvent(ev);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        // 容错性检查 (至少有俩子View, 子View必须是ViewGroup的子类)
        if (getChildCount() < 2) {
            throw new IllegalStateException("布局至少有俩孩子. Your ViewGroup must have 2 children at " +
                    "least.");
        }
        if (!(getChildAt(0) instanceof ViewGroup && getChildAt(1) instanceof ViewGroup)) {
            throw new IllegalArgumentException("子View必须是ViewGroup的子类. Your children must be an " +
                    "instance of ViewGroup");
        }

        mLeftContent = (ViewGroup) getChildAt(0);
        mMainContent = (ViewGroup) getChildAt(1);
    }

    public Status getStatus() {
        return mStatus;
    }

    public void setStatus(Status mStatus) {
        this.mStatus = mStatus;
    }


    /**
     * 状态枚举
     */
    public static enum Status {
        Close, Open, Draging;
    }

    /**
     * 抽屉开关的监听器
     */
    public interface OnDragStatusChangeListener {
        void onClose();

        void onOpen();

        void onDraging(float percent);
    }

    private OnDragStatusChangeListener mChangeListener;

    public void setOnDragStatusChangeListener(OnDragStatusChangeListener listener) {
        mChangeListener = listener;
    }

    ViewDragHelper.Callback mCallback = new ViewDragHelper.Callback() {
        // d. 重写事件

        // 1. 根据返回结果决定当前child是否可以拖拽
        // child 当前被拖拽的View
        // pointerId 区分多点触摸的id
        @Override
        public boolean tryCaptureView(View child, int pointerId) {
            return mToogle;
        }

        @Override
        public void onViewCaptured(View capturedChild, int activePointerId) {
            // 当capturedChild被捕获时,调用.
            super.onViewCaptured(capturedChild, activePointerId);
        }

        @Override
        public int getViewHorizontalDragRange(View child) {
            // 返回拖拽的范围, 不对拖拽进行真正的限制. 仅仅决定了动画执行速度
            return mRange;
        }

        // 2. 根据建议值 修正将要移动到的(横向)位置   (重要)
        // 此时没有发生真正的移动
        public int clampViewPositionHorizontal(View child, int left, int dx) {
            // child: 当前拖拽的View
            // left 新的位置的建议值, dx 位置变化量
            // left = oldLeft + dx;


            if (child == mMainContent) {
                left = fixLeft(left);
            }
            return left;
        }

        // 3. 当View位置改变的时候, 处理要做的事情 (更新状态, 伴随动画, 重绘界面)
        // 此时,View已经发生了位置的改变
        @Override
        public void onViewPositionChanged(View changedView, int left, int top,
                                          int dx, int dy) {
            // changedView 改变位置的View
            // left 新的左边值
            // dx 水平方向变化量
            super.onViewPositionChanged(changedView, left, top, dx, dy);


            int newLeft = left;
            if (changedView == mLeftContent) {
                // 把当前变化量传递给mMainContent
                newLeft = mMainContent.getLeft() + dx;
            }

            // 进行修正
            newLeft = fixLeft(newLeft);

            if (changedView == mLeftContent) {
                // 当左面板移动之后, 再强制放回去.
                mLeftContent.layout(0, 0, 0 + mWidth, 0 + mHeight);
                mMainContent.layout(newLeft, 0, newLeft + mWidth, 0 + mHeight);
            }
            // 更新状态,执行动画
            dispatchDragEvent(newLeft);

            // 为了兼容低版本, 每次修改值之后, 进行重绘
            invalidate();
        }

        // 4. 当View被释放的时候, 处理的事情(执行动画)
        @Override
        public void onViewReleased(View releasedChild, float xvel, float yvel) {
            // View releasedChild 被释放的子View
            // float xvel 水平方向的速度, 向右为+
            // float yvel 竖直方向的速度, 向下为+
            super.onViewReleased(releasedChild, xvel, yvel);

            // 判断执行 关闭/开启
            // 先考虑所有开启的情况,剩下的就都是关闭的情况
            if (xvel == 0 && mMainContent.getLeft() > mRange / 2.0f) {
                open();
            } else if (xvel > 0) {
                open();
            } else {
                close();
            }

        }

        @Override
        public void onViewDragStateChanged(int state) {
            // TODO Auto-generated method stub
            super.onViewDragStateChanged(state);
        }

    };

    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {

        // 进行修正
        int newLeft = fixLeft(left);

        if (changedView == mLeftContent) {
            // 当左面板移动之后, 再强制放回去.
            mLeftContent.layout(0, 0, 0 + mWidth, 0 + mHeight);
            mMainContent.layout(newLeft, 0, newLeft + mWidth, 0 + mHeight);

            if (changedView == mLeftContent) {
                // 把当前变化量传递给mMainContent
                newLeft = mMainContent.getLeft() + dx;
            }

        }


        // 更新状态,执行动画
        dispatchDragEvent(newLeft);

        // 为了兼容低版本, 每次修改值之后, 进行重绘
        invalidate();
    }

    protected void dispatchDragEvent(int newLeft) {
        float percent = newLeft * 1.0f / mRange;
        //0.0f -> 1.0f

        if (mChangeListener != null) {
            mChangeListener.onDraging(percent);
        }

        // 更新状态, 执行回调
        Status preStatus = mStatus;
        mStatus = updateStatus(percent);
        if (mStatus != preStatus) {
            // 状态发生变化
            if (mStatus == Status.Close) {
                // 当前变为关闭状态
                if (mChangeListener != null) {
                    mChangeListener.onClose();
                }
            } else if (mStatus == Status.Open) {
                if (mChangeListener != null) {
                    mChangeListener.onOpen();
                }
            }
        }

        //    * 伴随动画:
        animViews(percent);

    }

    private Status updateStatus(float percent) {
        if (percent == 0f) {
            return Status.Close;
        } else if (percent == 1.0f) {
            return Status.Open;
        }
        return Status.Draging;
    }


    /**
     * 根据范围修正左边值
     *
     * @param left
     * @return
     */
    private int fixLeft(int left) {
        if (left < 0) {
            return 0;
        } else if (left > mRange) {
            return mRange;
        }
        return left;
    }

    private void animViews(float percent) {
        //		> 1. 左面板: 缩放动画, 平移动画, 透明度动画
        // 缩放动画 0.0 -> 1.0 >>> 0.5f -> 1.0f  >>> 0.5f * percent + 0.5f
        //		mLeftContent.setScaleX(0.5f + 0.5f * percent);
        //		mLeftContent.setScaleY(0.5f + 0.5f * percent);
        ViewHelper.setScaleX(mLeftContent,
                EvaluateUtils.evaluate(percent, 0.5f, 1.0f));
        ViewHelper.setScaleY(mLeftContent, 0.5f + 0.5f * percent);
        // 平移动画: -mWidth / 2.0f -> 0.0f
        ViewHelper.setTranslationX(mLeftContent,
                EvaluateUtils.evaluate(percent, -mWidth / 2.0f, 0));
        // 透明度: 0.5 -> 1.0f
        ViewHelper.setAlpha(mLeftContent,
                EvaluateUtils.evaluate(percent, 0.5f, 1.0f));

        //		> 2. 主面板: 缩放动画
        // 1.0f -> 0.8f
        ViewHelper.setScaleX(mMainContent,
                EvaluateUtils.evaluate(percent, 1.0f, 0.8f));
        ViewHelper.setScaleY(mMainContent,
                EvaluateUtils.evaluate(percent, 1.0f, 0.8f));

        //		> 3. 背景动画: 亮度变化 (颜色变化)
        getBackground().setColorFilter(
                (Integer) ColorUtils.evaluateColor(percent,
                        Color.BLACK, Color.TRANSPARENT), PorterDuff.Mode.SRC_OVER);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();

        // 2. 持续平滑动画 (高频率调用)
        if (mDragHelper.continueSettling(true)) {
            //  如果返回true, 动画还需要继续执行
            ViewCompat.postInvalidateOnAnimation(this);
        }
    }

    public void close() {
        close(true);
    }

    /**
     * 关闭
     */
    public void close(boolean isSmooth) {
        int finalLeft = 0;
        if (isSmooth) {
            // 1. 触发一个平滑动画
            if (mDragHelper.smoothSlideViewTo(mMainContent, finalLeft, 0)) {
                // 返回true代表还没有移动到指定位置, 需要刷新界面.
                // 参数传this(child所在的ViewGroup)
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            mMainContent.layout(finalLeft, 0, finalLeft + mWidth, 0 + mHeight);
        }
    }

    public void open() {
        open(true);
    }

    /**
     * 开启
     */
    public void open(boolean isSmooth) {
        int finalLeft = mRange;
        if (isSmooth) {
            // 1. 触发一个平滑动画
            if (mDragHelper.smoothSlideViewTo(mMainContent, finalLeft, 0)) {
                // 返回true代表还没有移动到指定位置, 需要刷新界面.
                // 参数传this(child所在的ViewGroup)
                ViewCompat.postInvalidateOnAnimation(this);
            }
        } else {
            mMainContent.layout(finalLeft, 0, finalLeft + mWidth, 0 + mHeight);
        }
    }

    public int getRange() {
        return mRange;
    }

    public void setRange(int range) {
        this.mRange = range;
    }

    int mPostion;



}
