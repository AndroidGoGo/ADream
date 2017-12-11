package com.lzq.adream.widget.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.lzq.adream.widget.assembly.Tree;


/**
 * Created by ${廖昭启} on 2017/11/2.
 */

public class TreeView extends View {
    private static Tree tree;

    public TreeView(Context context) {
        super(context);
    }

    public TreeView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if(tree == null){
            tree = new Tree(getWidth(), getHeight());
        }
        tree.draw(canvas);

        // 这个函数只是标记view为invalidate状态，并不会马上触发重绘；
        // 标记invalidate状态后，下一个绘制周期(约16s), 会回调onDraw()；
        // 故此，要想动画平滑流畅，tree.draw(canvas)需在16s内完成。
        postInvalidate();
    }
}
