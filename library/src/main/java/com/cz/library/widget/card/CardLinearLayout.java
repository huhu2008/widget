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
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;

import com.cz.library.R;
import com.cz.library.util.Utils;

/**
 * Created by czz on 2016/9/27.
 */
public class CardLinearLayout extends LinearLayout{
    public static final int RIPPLE_SHAPE=0x00;
    public static final int RIPPLE_FULL=0x01;
    private final ShadowDrawable shadowDrawable;
    private int backgroundPressColor;
    private int horizontalPadding;
    private int verticalPadding;
    private Drawable foreground;
    private int rippleMode;

    public CardLinearLayout(Context context) {
        this(context, null);
    }

    public CardLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.shadowDrawable=new ShadowDrawable();
        setLayerType(View.LAYER_TYPE_SOFTWARE, this.shadowDrawable.getPaint());
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CardLinearLayout);
        setHorizontalPadding((int) a.getDimension(R.styleable.CardLinearLayout_cl_horizontalPadding, Utils.dip2px(1)));
        setVerticalPadding((int) a.getDimension(R.styleable.CardLinearLayout_cl_verticalPadding, Utils.dip2px(2)));
        setCornerRadius(a.getDimension(R.styleable.CardLinearLayout_cl_cardCornerRadius, Utils.dip2px(1)));
        setCardRectRadius(a.getDimension(R.styleable.CardLinearLayout_cl_cardRectRadius, 0));
        setCardBackgroundColor(a.getColor(R.styleable.CardLinearLayout_cl_cardBackgroundColor, Color.WHITE));
        setCardBackgroundPressColor(a.getColor(R.styleable.CardLinearLayout_cl_cardBackgroundPressColor, Color.TRANSPARENT));
        setCardElevation(a.getDimension(R.styleable.CardLinearLayout_cl_cardElevation, Utils.dip2px(1)));
        setCardType(a.getInt(R.styleable.CardLinearLayout_cl_cardType, ShadowDrawable.RECT));
        setCardRippleMode(a.getInt(R.styleable.CardLinearLayout_cl_cardRippleMode, RIPPLE_SHAPE));
        a.recycle();
    }

    public void setCardRippleMode(int mode) {
        rippleMode = mode;
        resetShadowDrawable();
    }

    public void setHorizontalPadding(int padding) {
        setContentPadding(padding, verticalPadding);
    }

    public void setVerticalPadding(int padding) {
        setContentPadding(horizontalPadding, padding);
    }

    /**
     * 5.0以下动态背景
     *
     * @param shadowDrawable
     */
    private void setBackgroundDrawableCompat(ShadowDrawable shadowDrawable) {
        int width = getWidth();
        int height = getHeight();
        StateListDrawable stateListDrawable = new StateListDrawable();
        ShadowDrawable pressDrawable = shadowDrawable.clone();
        pressDrawable.setBackgroundColor(backgroundPressColor);
        stateListDrawable.addState(PRESSED_ENABLED_STATE_SET, pressDrawable);
        stateListDrawable.addState(EMPTY_STATE_SET, shadowDrawable);
        stateListDrawable.setBounds(horizontalPadding, verticalPadding, width - horizontalPadding, height - verticalPadding);
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
            setBackgroundDrawable(stateListDrawable);
        } else {
            setBackground(stateListDrawable);
        }
    }

    /**
     * 5.0及以上动态背景
     *
     * @param shadowDrawable
     */
    private void setBackgroundDrawableL(ShadowDrawable shadowDrawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (null != foreground) {
                foreground.setCallback(null);
                unscheduleDrawable(foreground);
            }
            if (RIPPLE_FULL == rippleMode) {
                ShadowDrawable newDrawable = shadowDrawable.clone();
                newDrawable.setBounds(0, 0, getWidth(), getHeight());
                foreground = new RippleDrawable(ColorStateList.valueOf(backgroundPressColor), null, newDrawable);
            } else {
                foreground = new RippleDrawable(ColorStateList.valueOf(backgroundPressColor), null, shadowDrawable);
            }
            foreground.setCallback(this);
            if (foreground.isStateful()) {
                foreground.setState(getDrawableState());
            }
            setBackground(shadowDrawable);
            setContentPadding(horizontalPadding, verticalPadding);
        }
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        resetShadowDrawable();
    }


    private void resetShadowDrawable() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            setBackgroundDrawableCompat(shadowDrawable);
        } else {
            setBackgroundDrawableL(shadowDrawable);
        }

    }


    public void setCardType(int cardType) {
        this.shadowDrawable.setCardType(cardType);
        if (isShown()) {
            resetShadowDrawable();
        }
    }

    public void setContentPadding(int padding) {
        setContentPadding(padding, padding);
    }

    public void setContentPadding(int horizontalPadding, int verticalPadding) {
        this.horizontalPadding = horizontalPadding;
        this.verticalPadding = verticalPadding;
        if (null != getBackground()) {
            getBackground().setBounds(horizontalPadding, verticalPadding, getWidth() - horizontalPadding, getHeight() - verticalPadding);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (null != foreground) {
                if (RIPPLE_FULL == rippleMode) {
                    foreground.setBounds(0, 0, getWidth(), getHeight());
                } else {
                    foreground.setBounds(horizontalPadding, verticalPadding, getWidth() - horizontalPadding, getHeight() - verticalPadding);
                }
            }
        }
    }

    public void setCornerRadius(float radius) {
        this.shadowDrawable.setShadowRadius(radius);
        invalidate();
    }

    public void setCardBackgroundColor(int color) {
        this.shadowDrawable.setBackgroundColor(color);
        invalidate();
    }

    private void setCardBackgroundPressColor(int color) {
        this.backgroundPressColor = color;
    }

    public void setCardElevation(float elevation) {
        this.shadowDrawable.setElevation(elevation);
        invalidate();
    }

    public void setCardRectRadius(float radius) {
        this.shadowDrawable.setRectRadius(radius);
        invalidate();
    }

    @Override
    protected void drawableStateChanged() {
        super.drawableStateChanged();
        if (foreground != null && foreground.isStateful()) {
            foreground.setState(getDrawableState());
        }
    }

    @Override
    protected boolean verifyDrawable(Drawable who) {
        return super.verifyDrawable(who) || (who == foreground);
    }

    @Override
    public void jumpDrawablesToCurrentState() {
        super.jumpDrawablesToCurrentState();
        if (foreground != null) {
            foreground.jumpToCurrentState();
        }
    }


    @Override
    public void draw(Canvas canvas) {
        setContentPadding(horizontalPadding, verticalPadding);
        super.draw(canvas);
        if (null != foreground) {
            foreground.draw(canvas);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (e.getActionMasked() == MotionEvent.ACTION_DOWN) {
                if (null != foreground) {
                    foreground.setHotspot(e.getX(), e.getY());
                }
            }
        }
        return super.onTouchEvent(e);
    }
}