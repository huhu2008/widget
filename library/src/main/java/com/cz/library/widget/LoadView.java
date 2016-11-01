package com.cz.library.widget;

import android.animation.AnimatorSet;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import com.cz.library.R;

import static android.view.View.MeasureSpec.makeMeasureSpec;

/**
 * Created by cz on 11/1/16.
 */

public class LoadView extends View {
    private static final int AUTO_WIDTH=-1;
    private static final int AUTO_HEIGHT=-1;

    private final Paint circlePaint;
    private final Paint outRingPaint;
    private final Paint ringPaint;
    private final Paint arcPaint;

    private AnimatorSet animationSet;
    private int drawableHeight;
    private int drawableWidth;
    private float drawablePadding;
    private Drawable drawable;
    private int ringAngle;
    private int rotateAnimDuration;
    private int scaleAnimDuration;
    private float fraction;
    private float scale;

    public LoadView(Context context) {
        this(context,null,R.attr.loadView);
    }

    public LoadView(Context context, AttributeSet attrs) {
        this(context, attrs,R.attr.loadView);
    }

    public LoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        circlePaint=new Paint(Paint.ANTI_ALIAS_FLAG);

        outRingPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        outRingPaint.setStyle(Paint.Style.STROKE);
        ringPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        ringPaint.setStyle(Paint.Style.STROKE);
        arcPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        arcPaint.setStyle(Paint.Style.STROKE);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.LoadView,R.attr.loadView,R.style.LoadView);
        setDrawableWidth(a.getLayoutDimension(R.styleable.LoadView_lv_drawableWidth,AUTO_WIDTH));
        setDrawableHeight(a.getLayoutDimension(R.styleable.LoadView_lv_drawableHeight,AUTO_HEIGHT));
        setDrawablePadding(a.getDimension(R.styleable.LoadView_lv_drawablePadding,0));
        setDrawableBackgroundColor(a.getColor(R.styleable.LoadView_lv_drawableBackgroundColor, Color.WHITE));
        setRingColor(a.getColor(R.styleable.LoadView_lv_ringColor,Color.WHITE));
        setRingArcColor(a.getColor(R.styleable.LoadView_lv_ringArcColor,Color.WHITE));
        setOutRingColor(a.getColor(R.styleable.LoadView_lv_outRingColor,Color.WHITE));
        setRingAngle(a.getInteger(R.styleable.LoadView_lv_ringAngle,0));
        setRingSize(a.getDimension(R.styleable.LoadView_lv_ringSize,0));
        setOutRingSize(a.getDimension(R.styleable.LoadView_lv_outRingSize,0));
        setDrawable(a.getDrawable(R.styleable.LoadView_lv_drawable));
        setRotateAnimDuration(a.getInteger(R.styleable.LoadView_lv_rotateAnimDuration,2000));
        setScaleAnimDuration(a.getInteger(R.styleable.LoadView_lv_scaleAnimDuration,1000));
        setScale(a.getFloat(R.styleable.LoadView_lv_scale,0f));
        a.recycle();
    }

    public void setScale(float scale) {
        this.scale=scale;
    }

    public void setDrawableWidth(int width) {
        this.drawableWidth=width;
        requestLayout();
    }

    public void setDrawableHeight(int height) {
        this.drawableHeight=height;
        requestLayout();
    }

    public void setDrawablePadding(float padding) {
        this.drawablePadding=padding;
        invalidate();
    }

    public void setDrawableBackgroundColor(int color) {
        circlePaint.setColor(color);
    }

    public void setRingColor(int color) {
        ringPaint.setColor(color);
    }

    public void setRingArcColor(int color) {
        arcPaint.setColor(color);
    }

    public void setOutRingColor(int color) {
        outRingPaint.setColor(color);
    }

    public void setRingAngle(int angle) {
        this.ringAngle=angle;
    }

    public void setRingSize(float size) {
        arcPaint.setStrokeWidth(size);
        ringPaint.setStrokeWidth(size);
    }

    public void setOutRingSize(float size) {
        outRingPaint.setStrokeWidth(size);
    }

    public void setDrawable(Drawable drawable) {
        this.drawable=drawable;
        invalidate();
    }

    public void setRotateAnimDuration(int duration) {
        this.rotateAnimDuration =duration;
    }

    public void setScaleAnimDuration(int duration) {
        this.scaleAnimDuration =duration;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0, height = 0;
        if (null != drawable) {
            width = AUTO_WIDTH == drawableWidth ? drawable.getIntrinsicWidth() : drawableWidth;
            height = AUTO_HEIGHT == drawableHeight ? drawable.getIntrinsicHeight() : drawableHeight;
        }
        width += (drawablePadding*2 + ringPaint.getStrokeWidth() + outRingPaint.getStrokeWidth());
        height += (drawablePadding*2 + ringPaint.getStrokeWidth() + outRingPaint.getStrokeWidth());
        setMeasuredDimension(makeMeasureSpec(width, MeasureSpec.EXACTLY), makeMeasureSpec(height, MeasureSpec.EXACTLY));
    }

    public void startRotateAnimation(){
        if(null==animationSet){
            animationSet=new AnimatorSet();
            ValueAnimator valueAnimator1 = ValueAnimator.ofFloat(1.0f);
            valueAnimator1.setDuration(rotateAnimDuration);
            valueAnimator1.setInterpolator(new LinearInterpolator());
            valueAnimator1.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator1.setRepeatMode(ValueAnimator.RESTART);
            valueAnimator1.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    fraction=animation.getAnimatedFraction();
                    invalidate();
                }
            });

            ValueAnimator valueAnimator2 = ValueAnimator.ofFloat(1.0f);
            valueAnimator2.setDuration(scaleAnimDuration);
            valueAnimator2.setRepeatCount(ValueAnimator.INFINITE);
            valueAnimator2.setRepeatMode(ValueAnimator.REVERSE);
            valueAnimator2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    setScaleX(1f+scale*animation.getAnimatedFraction());
                    setScaleY(1f+scale*animation.getAnimatedFraction());
                }
            });
            animationSet.play(valueAnimator1).with(valueAnimator2);
        }
        animationSet.start();
    }

    public void stopRotateAnimation(){
        if(null!=animationSet){
            animationSet.cancel();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if(null!=animationSet){
            if(hasWindowFocus&&isShown()){
                startRotateAnimation();
            } else {
                stopRotateAnimation();
            }
        }
    }


    @Override
    protected void onWindowVisibilityChanged(int visibility) {
        super.onWindowVisibilityChanged(visibility);
        if(null!=animationSet){
            if(isShown()){
                startRotateAnimation();
            } else {
                stopRotateAnimation();
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centerX=getWidth()/2;
        int centerY=getHeight()/2;
        //绘drawable
        int drawableWidth=0,drawableHeight=0;
        if(null!=drawable){
            drawableWidth=AUTO_WIDTH==this.drawableWidth?drawable.getIntrinsicWidth():this.drawableWidth;
            drawableHeight=AUTO_HEIGHT==this.drawableHeight?drawable.getIntrinsicHeight():this.drawableHeight;
        }
        float radius = Math.min(drawableWidth, drawableHeight)/2+drawablePadding;
        float ringSize = ringPaint.getStrokeWidth();
        //画外环
        canvas.drawCircle(centerX,centerY,radius+ringSize/2,outRingPaint);
        //画内圆
        canvas.drawCircle(centerX,centerY,radius,circlePaint);
        //画内环
        canvas.drawCircle(centerX,centerY,radius,ringPaint);
        //画扇形
        int angle= (int) (fraction*360);
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
            canvas.drawArc(new RectF(centerX-radius,centerY-radius,centerX+radius,centerY+radius),angle,ringAngle,false,arcPaint);
        } else {
            canvas.drawArc(centerX-radius,centerY-radius,centerX+radius,centerY+radius,angle,ringAngle,false,arcPaint);
        }
        if(null!=drawable){
            drawable.setBounds(centerX-drawableWidth/2,centerY-drawableHeight/2,centerX+drawableWidth/2,centerY+drawableHeight/2);
            drawable.draw(canvas);
        }
    }

    @Override
    protected Parcelable onSaveInstanceState() {
        Bundle bundle=new Bundle();
        Parcelable parcelable = super.onSaveInstanceState();
        bundle.putParcelable("super",parcelable);
        bundle.putInt("drawableWidth",drawableWidth);
        bundle.putInt("drawableHeight",drawableHeight);
        bundle.putInt("drawableBackgroundColor",circlePaint.getColor());
        bundle.putInt("ringColor",ringPaint.getColor());
        bundle.putInt("ringArcColor",arcPaint.getColor());
        bundle.putInt("ringAngle",ringAngle);
        bundle.putInt("outRingColor",outRingPaint.getColor());
        bundle.putFloat("outRingSize",outRingPaint.getStrokeWidth());
        bundle.putInt("rotateAnimDuration",rotateAnimDuration);
        bundle.putInt("scaleAnimDuration",scaleAnimDuration);
        bundle.putFloat("scale",scale);
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        Bundle bundle= (Bundle) state;
        Parcelable parcelable=bundle.getParcelable("super");
        super.onRestoreInstanceState(parcelable);
        drawableWidth=bundle.getInt("drawableWidth",AUTO_WIDTH);
        drawableHeight=bundle.getInt("drawableHeight",AUTO_HEIGHT);
        circlePaint.setColor(bundle.getInt("drawableBackgroundColor"));
        ringPaint.setColor(bundle.getInt("ringColor"));
        arcPaint.setColor(bundle.getInt("ringArcColor"));
        ringAngle=bundle.getInt("ringAngle");
        outRingPaint.setColor(bundle.getInt("outRingColor"));
        outRingPaint.setStrokeWidth(bundle.getFloat("outRingSize"));
        rotateAnimDuration=bundle.getInt("rotateAnimDuration");
        scaleAnimDuration=bundle.getInt("scaleAnimDuration");
        scale=bundle.getFloat("scale");
        requestLayout();
    }
}
