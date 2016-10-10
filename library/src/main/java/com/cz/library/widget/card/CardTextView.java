package com.cz.library.widget.card;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.shapes.Shape;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.cz.library.R;
import com.cz.library.widget.DivideTextView;

/**
 * Created by Administrator on 2016/9/27.
 */
public class CardTextView extends DivideTextView {
    private static final int MAX_SHADOW_SIZE=20;
    public static final int RECT=0x00;
    public static final int OVAL=0x01;
    private float contentLeftPadding;
    private float contentTopPadding;
    private float contentRightPadding;
    private float contentBottomPadding;
    private RectShadowDrawable shadowDrawable;

    public CardTextView(Context context) {
        this(context,null);
    }

    public CardTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.shadowDrawable=new RectShadowDrawable();
        setLayerType(View.LAYER_TYPE_SOFTWARE, this.shadowDrawable.getPaint());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CardTextView);
        setContentLeftPadding(a.getDimension(R.styleable.CardTextView_cv_contentLeftPadding,0));
        setContentTopPadding(a.getDimension(R.styleable.CardTextView_cv_contentTopPadding,0));
        setContentRightPadding(a.getDimension(R.styleable.CardTextView_cv_contentRightPadding,0));
        setContentBottomPadding(a.getDimension(R.styleable.CardTextView_cv_contentBottomPadding,0));
//        setCornerRadius(a.getDimension(R.styleable.CardTextView_cv_cardCornerRadius,0));
//        setCardBackgroundColor(a.getColor(R.styleable.CardTextView_cv_cardBackgroundColor, Color.WHITE));
//        setCardElevation(a.getDimension(R.styleable.CardTextView_cv_cardElevation,0));
//        setCardType(a.getInt(R.styleable.CardTextView_cv_cardType,RECT));
        a.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.shadowDrawable.setBounds(10,10,w-10,h-10);
    }

    public void setContentLeftPadding(float padding) {
        this.contentLeftPadding=padding;
        invalidate();
    }

    public void setContentTopPadding(float padding) {
        this.contentTopPadding=padding;
        invalidate();
    }

    public void setContentRightPadding(float padding) {
        this.contentRightPadding=padding;
        invalidate();
    }

    public void setContentBottomPadding(float padding) {
        this.contentBottomPadding=padding;
        invalidate();
    }

    public void setCornerRadius(float radius) {
        this.shadowDrawable.setShadowRadius((int) radius);
    }

    public void setCardBackgroundColor(int color) {
        this.shadowDrawable.setBackgroundColor(color);
    }

    public void setCardElevation(float elevation) {
        this.shadowDrawable.setElevation(elevation);
    }



    @Override
    protected void onDraw(Canvas canvas) {
        shadowDrawable.draw(canvas);
        super.onDraw(canvas);
    }
}
