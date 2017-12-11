package com.lzq.adream.widget.assembly;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.lzq.adream.utils.forview.CommonUtil;


/**
 * Created by ${廖昭启} on 2017/11/2.
 */

public class Bloom {
    protected static float sMaxScale = 0.2f;
    protected static int sMaxRadius = Math.round(sMaxScale * Heart.getRadius());
    protected static float sFactor;

    /**
     * 初始化显示参数
     * @param resolutionFactor 根据屏幕分辨率设定缩放因子
     */
    public static void initDisplayParam(float resolutionFactor){
        sFactor = resolutionFactor;
        sMaxScale = 0.2f * resolutionFactor;
        sMaxRadius = Math.round(sMaxScale * Heart.getRadius());
    }

    Point position;
    int color;
    float angle;
    float scale;

    // 调速器，控制开花动画的快慢
    int governor = 0;

    public Bloom(Point position) {
        this.position = position;
        this.color = Color.argb(CommonUtil.random(76, 255), 0xff, CommonUtil.random(255), CommonUtil.random(255));
        this.angle = CommonUtil.random(360);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public boolean grow(Canvas canvas) {
        if (scale <= sMaxScale) {
            if((governor & 1) == 0) {
                scale += 0.0125f * sFactor;
                draw(canvas);
            }
            governor++;
            return true;
        } else {
            return false;
        }
    }

    protected float getRadius() {
        return Heart.getRadius() * scale;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void draw(Canvas canvas) {
        Paint paint = CommonUtil.getPaint();
        paint.setColor(color);
        float r = getRadius();

        canvas.save();
        canvas.translate(position.x, position.y);
        canvas.saveLayerAlpha(-r, -r, r, r, Color.alpha(color));
        canvas.save();
        canvas.rotate(angle);
        canvas.scale(scale, scale);
        canvas.drawPath(Heart.getPath(), paint);
        canvas.restore();
        canvas.restore();
        canvas.restore();
    }
}
