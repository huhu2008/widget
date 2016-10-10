package com.cz.library.widget.card;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;


/**
 * a rect shadow
 */
public class RectShadowDrawable extends ShapeDrawable implements IShadowDrawable{
    private static final int SHADOW_COLOR = 0x1E000000;
    private int shadowRadius;
    private int diameter;

    public RectShadowDrawable() {
        super();
        setShape(new RectShadow());
        getPaint().setColor(Color.WHITE);
        getPaint().setShadowLayer(8, 2, 5, 0x1E000000);
    }

    @Override
    public void setBackgroundColor(int color){
        getPaint().setColor(color);
        invalidateSelf();
    }

    @Override
    public void setShadowRadius(int radius){
        this.shadowRadius=radius;
        getPaint().setShadowLayer(20, 0, 10, SHADOW_COLOR);
    }
    @Override
    public void setElevation(float elevation){
        getPaint().setShadowLayer(20, 0, elevation, SHADOW_COLOR);
    }

    class RectShadow extends RectShape {

        public RectShadow() {
            super();
        }
        @Override
        public void draw(Canvas canvas, Paint paint) {
            Rect bounds = getBounds();
            canvas.drawRect(0,0,bounds.width(),bounds.height(), paint);
        }
    }
}