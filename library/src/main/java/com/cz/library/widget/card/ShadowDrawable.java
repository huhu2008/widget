package com.cz.library.widget.card;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.OvalShape;
import android.graphics.drawable.shapes.RectShape;


/**
 * a oval shadow
 */
public class ShadowDrawable extends ShapeDrawable implements Cloneable{
    private static final int SHADOW_COLOR = 0x1E000000;
    public static final int RECT=0x00;
    public static final int OVAL=0x01;
    private ShadowDrawable cloneDrawable;
    private float shadowRadius;
    private float rectRadius;
    private float elevation;
    private int cardType;

    public ShadowDrawable() {
        super();
    }

    public void setBackgroundColor(int color){
        getPaint().setColor(color);
        invalidateSelf();
    }

    public void setShadowRadius(float radius){
        setShadowLayer(shadowRadius = radius, this.elevation);
    }

    public void setRectRadius(float radius){
        this.rectRadius=radius;
    }

    public void setElevation(float elevation){
        setShadowLayer(shadowRadius,this.elevation=elevation);
    }


    public void setCardType(int cardType) {
        this.cardType=cardType;
        if(RECT==cardType){
            setShape(new RectShadow());
        } else if(OVAL==cardType){
            setShape(new OvalShadow());
        }
        setShadowLayer(shadowRadius,elevation);
    }

    private void setShadowLayer(float shadowRadius,float elevation) {
        getPaint().setShadowLayer(shadowRadius, 0, elevation, SHADOW_COLOR);
        if(null!=cloneDrawable){
            cloneDrawable.getPaint().setShadowLayer(shadowRadius, 0, elevation, SHADOW_COLOR);
        }
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

    class RectShadow extends RectShape {

        public RectShadow() {
            super();
        }
        @Override
        public void draw(Canvas canvas, Paint paint) {
            canvas.drawRoundRect(new RectF(0,0,getWidth(),getHeight()),rectRadius,rectRadius, paint);
        }
    }

    @Override
    protected ShadowDrawable clone()  {
        ShadowDrawable drawable = new ShadowDrawable();
        drawable.getPaint().setShadowLayer(shadowRadius, 0, elevation, SHADOW_COLOR);
        drawable.setCardType(cardType);
        return cloneDrawable=drawable;
    }
}