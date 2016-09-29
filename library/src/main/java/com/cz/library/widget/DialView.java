package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.cz.library.R;

/**
 * Created by czz on 2016/9/26.
 */
public class DialView extends View{
    private static final String TAG = "DialView";
    private final boolean DEBUG=true;
    private final Paint paint;
    private final Paint textPaint;
    private final Path path;
    private final Path textPath;
    private float dialPadding;

    private float[] pos;
    private float[] tan;
    private PathMeasure pathMeasure;
    private PathMeasure textPathMeasure;
    private Drawable progressDrawable;
    private RectF arcRect;
    private Rect textRect;
    private float dialInnerPadding1;
    private float dialInnerPadding2;
    private int intervalItemCount;
    private float fraction;
    private int itemIntervalDegrees;
    private int itemCount;

    private final int  textBottomPadding=20;

    public DialView(Context context) {
        this(context,null,0);
    }

    public DialView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public DialView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        paint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint=new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setTextSize(20);
        textRect=new Rect();
        path=new Path();
        textPath=new Path();
        pos=new float[2];
        tan=new float[2];

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DialView);
        setStrokeWidth(a.getDimension(R.styleable.DialView_dv_strokeWidth,0));
        setPaintColor(a.getColor(R.styleable.DialView_dv_paintColor, Color.WHITE));
        setDialPadding(a.getDimension(R.styleable.DialView_dv_dialPadding,0));
        setDialProgressDrawable(a.getDrawable(R.styleable.DialView_dv_dialProgressDrawable));
        setDialInnerPadding1(a.getDimension(R.styleable.DialView_dv_dialInnerPadding1,0));
        setDialInnerPadding2(a.getDimension(R.styleable.DialView_dv_dialInnerPadding2,0));
        setDialItemIntervalDegrees(a.getInteger(R.styleable.DialView_dv_dialItemIntervalDegrees,2));
        setDialIntervalItemCount(a.getInteger(R.styleable.DialView_dv_dialIntervalItemCount,7));
        setDialItemCount(a.getInteger(R.styleable.DialView_dv_dialItemCount,5));
        a.recycle();
    }

    public void setDialIntervalItemCount(int itemCount) {
        this.intervalItemCount=itemCount;
        invalidate();
    }

    private void setDialItemCount(int count) {
        this.itemCount=count;
        invalidate();
    }

    public void setDialItemIntervalDegrees(int degrees) {
        this.itemIntervalDegrees=degrees;
        invalidate();
    }

    public void setDialInnerPadding1(float padding) {
        this.dialInnerPadding1=padding;
        invalidate();
    }

    public void setDialInnerPadding2(float padding) {
        this.dialInnerPadding2=padding;
        invalidate();
    }

    public void setDialPadding(float padding) {
        this.dialPadding=padding;
        invalidate();
    }

    public void setStrokeWidth(float width) {
        this.paint.setStrokeWidth(width);
        invalidate();
    }

    public void setPaintColor(int color) {
        this.paint.setColor(color);
        invalidate();
    }

    public void setDialProgressDrawable(Drawable drawable) {
        this.progressDrawable=drawable;
        invalidate();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        paint.setStyle(Paint.Style.STROKE);
        int width = getWidth();
        int height = getHeight();
        float radius = Math.max(width, height)/2-dialPadding;
        path.addCircle(width/2,height-textBottomPadding,radius, Path.Direction.CW);
        pathMeasure=new PathMeasure(path, false);

        textPath.addCircle(width/2,height-textBottomPadding,radius-dialPadding-dialInnerPadding1-dialInnerPadding2,Path.Direction.CW);
        textPathMeasure=new PathMeasure(textPath,false);
        float startY=height-radius+dialInnerPadding1;
        float padding = dialPadding + dialInnerPadding1;
        arcRect=new RectF(padding,startY-textBottomPadding,width-padding,startY+(radius-dialInnerPadding1)*2-textBottomPadding);

    }

    public void setProgress(float fraction){
        this.fraction=fraction;
        invalidate();
    }

    public void setRotateDegrees(int degrees){
        this.rotateDegrees=degrees;
        invalidate();
    }
    private int rotateDegrees;
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        long st = System.currentTimeMillis();
        int width = getWidth();
        int height = getHeight();
        paint.setColor(Color.WHITE);
        canvas.drawPath(path,paint);
        float radius = Math.max(width, height)/2-dialPadding;
        float halfLength = pathMeasure.getLength()/2;
        pathMeasure.getPosTan(halfLength+fraction*halfLength,pos,tan);
        float degrees =(float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
        if(null!=progressDrawable){
            canvas.save();
            canvas.rotate(degrees,pos[0],pos[1]);
            int halfDrawableWidth = progressDrawable.getIntrinsicWidth()/2;
            int halfDrawableHeight = progressDrawable.getIntrinsicHeight()/2;
            progressDrawable.setBounds((int)pos[0]-halfDrawableWidth,(int)pos[1]-halfDrawableHeight,(int)pos[0]+halfDrawableWidth,(int)pos[1]+halfDrawableHeight);
            progressDrawable.draw(canvas);
            canvas.restore();
        }
        int startDegrees=180+itemIntervalDegrees/2;
        int itemDegrees=(180-itemIntervalDegrees*(itemCount-1))/(itemCount-1);
        for(int i=0;i<itemCount-1;i++){
            canvas.drawArc(arcRect,startDegrees,itemDegrees,false,paint);
            startDegrees+=itemDegrees+itemIntervalDegrees;
        }

        int totalItemCount=intervalItemCount*(itemCount-1);
        float itemFraction=1f/totalItemCount;
        float strokeWidth = paint.getStrokeWidth();
        for(int i=0;i<=totalItemCount;i++){
            pathMeasure.getPosTan(halfLength+i*itemFraction*halfLength,pos,tan);
            degrees =(float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
            canvas.save();
            canvas.rotate(degrees,pos[0],pos[1]);
            canvas.translate(0 ,dialInnerPadding1+dialInnerPadding2+strokeWidth/2);
            canvas.drawLine(pos[0],pos[1]-(0==i%intervalItemCount?10:0),pos[0],pos[1]+10,paint);
            canvas.restore();
        }

        //draw level;
        halfLength=textPathMeasure.getLength()/2;
        textPaint.setTextSize(20);
        for(int i=0;i<itemCount;i++){
            String text = String.valueOf((i+1)*100);
            float textWidth = textPaint.measureText(text, 0, text.length());
            textPathMeasure.getPosTan(textWidth/2,pos,tan);
            degrees =(float) (Math.atan2(tan[1], tan[0]) * 180.0 / Math.PI);
            textPaint.getTextBounds(text, 0, text.length(),textRect);
            canvas.save();
            canvas.rotate(270-degrees,width/2,height-textBottomPadding);
            canvas.drawTextOnPath(text, textPath,i*(halfLength/(itemCount-1)),textRect.height(), textPaint);
            canvas.restore();
        }

        //draw note
//        (height-(textPaint.descent() + textPaint.ascent()))/2
        String value="信用中等";
        textPaint.setTextSize(40);
        float textWidth = textPaint.measureText(value, 0, value.length());
        canvas.drawText(value,(width-textWidth)/2,-(textPaint.descent()+ textPaint.ascent())+(height-radius)+radius/2,textPaint);
        //draw value
        value=String.valueOf(500);
        textPaint.setTextSize(50);
        textWidth = textPaint.measureText(value, 0, value.length());
        textPaint.getTextBounds(value, 0, value.length(),textRect);
        canvas.drawText(value,(width-textWidth)/2,(height-textPaint.descent()- textPaint.ascent())-textRect.height()-textBottomPadding,textPaint);

        if(DEBUG){
            radius = Math.max(width, height)/2;
            canvas.drawLine(0,height-radius,width,height-radius,paint);
            canvas.drawLine(width/2,0,width/2,height,paint);
        }

        Log.e(TAG,"time:"+(System.currentTimeMillis()-st));
    }

}
