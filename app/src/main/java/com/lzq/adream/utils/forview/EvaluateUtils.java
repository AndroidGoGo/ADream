package com.lzq.adream.utils.forview;

/**
 * Created by ${廖昭启} on 2017/11/3.
 */

public class EvaluateUtils {
    /**
     * 估值器
     *
     * @param fraction
     * @param startValue
     * @param endValue
     * @return
     */
    public static float evaluate(float fraction, Number startValue, Number endValue) {
        float startFloat = startValue.floatValue();
        return startFloat + fraction * (endValue.floatValue() - startFloat);
    }
}
