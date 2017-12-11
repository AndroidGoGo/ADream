package com.lzq.adream.widget.assembly;

import android.graphics.Bitmap;
import android.graphics.Canvas;

/**
 * Created by ${廖昭启} on 2017/11/2.
 */

public class Snapshot {
    Canvas canvas;
    Bitmap bitmap;
    public Snapshot(Bitmap bitmap){
        this.bitmap = bitmap;
        this.canvas = new Canvas(bitmap);
    }
}
