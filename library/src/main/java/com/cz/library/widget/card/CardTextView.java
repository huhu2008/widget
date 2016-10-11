package com.cz.library.widget.card;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;

import com.cz.library.R;
import com.cz.library.util.Utils;
import com.cz.library.widget.DivideTextView;

/**
 * Created by czz on 2016/9/27.
 */
public class CardTextView extends DivideTextView {
    private final ShadowDrawable shadowDrawable;
    private int backgroundPressColor;
    private int contentPadding;

    public CardTextView(Context context) {
        this(context, null);
    }

    public CardTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.shadowDrawable=new ShadowDrawable();
        setLayerType(View.LAYER_TYPE_SOFTWARE, this.shadowDrawable.getPaint());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CardTextView);
        setContentPadding((int) a.getDimension(R.styleable.CardTextView_cv_contentPadding, Utils.dip2px(2)));
        setCornerRadius(a.getDimension(R.styleable.CardTextView_cv_cardCornerRadius, Utils.dip2px(2)));
        setCardRectRadius(a.getDimension(R.styleable.CardTextView_cv_cardRectRadius,0));
        setCardBackgroundColor(a.getColor(R.styleable.CardTextView_cv_cardBackgroundColor, Color.WHITE));
        setCardBackgroundPressColor(a.getColor(R.styleable.CardTextView_cv_cardBackgroundPressColor, Color.TRANSPARENT));
        setCardElevation(a.getDimension(R.styleable.CardTextView_cv_cardElevation, Utils.dip2px(2)));
        setCardType(a.getInt(R.styleable.CardTextView_cv_cardType, ShadowDrawable.RECT));
        a.recycle();
    }

    /**
     * 5.0以下动态背景
     * @param shadowDrawable
     */
    private void setBackgroundDrawableCompat(ShadowDrawable shadowDrawable) {
        int width = getWidth();
        int height = getHeight();
        StateListDrawable stateListDrawable=new StateListDrawable();
        ShadowDrawable pressDrawable = shadowDrawable.clone();
        pressDrawable.setBackgroundColor(backgroundPressColor);
        stateListDrawable.addState(PRESSED_ENABLED_STATE_SET, pressDrawable);
        stateListDrawable.addState(EMPTY_STATE_SET, shadowDrawable);
        stateListDrawable.setBounds(contentPadding, contentPadding, width - contentPadding, height - contentPadding);
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.JELLY_BEAN){
            setBackgroundDrawable(stateListDrawable);
        } else {
            setBackground(stateListDrawable);
        }
    }

    /**
     * 5.0及以上动态背景
     * @param shadowDrawable
     */
    private void setBackgroundDrawableL(ShadowDrawable shadowDrawable) {
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.LOLLIPOP){
            setBackground(new RippleDrawable(ColorStateList.valueOf(backgroundPressColor),shadowDrawable,null));
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resetShadowDrawable();
    }


    private void resetShadowDrawable() {
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
            setBackgroundDrawableCompat(shadowDrawable);
        } else {
            setBackgroundDrawableL(shadowDrawable);
        }
    }

    public void setCardType(int cardType){
        this.shadowDrawable.setCardType(cardType);
        if(isShown()){
            resetShadowDrawable();
        }
    }

    public void setContentPadding(int padding) {
        this.contentPadding=padding;
        if(Build.VERSION.SDK_INT<Build.VERSION_CODES.LOLLIPOP){
            Drawable drawable = getBackground();
            if(null!=drawable){
                drawable.setBounds(padding, padding, getWidth() - padding, getHeight() - padding);
            }
        } else {
            shadowDrawable.setBounds(padding, padding, getWidth() - padding, getHeight() - padding);
        }
    }

    public void setCornerRadius(float radius) {
        this.shadowDrawable.setShadowRadius(radius);
        invalidate();
    }

    public void setCardRectRadius(float radius) {
        this.shadowDrawable.setRectRadius(radius);
        invalidate();
    }

    public void setCardBackgroundColor(int color) {
        this.shadowDrawable.setBackgroundColor(color);
        invalidate();
    }

    private void setCardBackgroundPressColor(int color) {
        this.backgroundPressColor=color;
    }

    public void setCardElevation(float elevation) {
        this.shadowDrawable.setElevation(elevation);
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //这里之所以重新设置一次content padding是因为在控件未绘制之前设置不生效.
        setContentPadding(contentPadding);
        super.dispatchDraw(canvas);
    }
}
