package com.cz.library.widget.card;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RadialGradient;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.os.Build;
import android.view.View;


/**
 * a oval shadow
 */
public class OvalShadowDrawable extends ShapeDrawable implements IShadowDrawable{
    private static final int SHADOW_COLOR = 0x1E000000;
    private int shadowRadius;
    private int diameter;

    public OvalShadowDrawable() {
        super();
        setShape(new OvalShadow());
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

    class OvalShadow extends OvalShape {
        public OvalShadow() {
            super();
        }
        @Override
        public void draw(Canvas canvas, Paint paint) {
            Rect bounds = getBounds();
            final float viewWidth = bounds.width();
            final float viewHeight = bounds.height();
            canvas.drawCircle(viewWidth / 2, viewHeight / 2, (Math.min(viewWidth,viewHeight) / 2), paint);
        }
    }
}