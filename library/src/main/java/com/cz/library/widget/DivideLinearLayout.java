package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

import com.cz.library.R;


/**
 * 分隔线布局
 * <p>
 * Created by cz on 2014/8/8
 */
public class DivideLinearLayout extends LinearLayout {
    public static final int NONE = 0x01;
    public static final int LEFT = 0x02;
    public static final int TOP = 0x04;
    public static final int RIGHT = 0x08;
    public static final int BOTTOM = 0x10;
    public static final int ITEM_DIVIDE = 0x20;

    @IntDef({NONE, LEFT, TOP, RIGHT, BOTTOM, ITEM_DIVIDE})
    public @interface DivideGravity {
    }

    private int strokeSize;
    private int itemStrokeSize;
    private int dividePadding;
    private int itemDividePadding;
    private int leftPadding;//左下单独边距,项目内这块需要比较多大
    private Drawable divideDrawable;
    private Drawable itemDivideDrawable;
    private int gravity;

    public DivideLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);
        initAttribute(context, attrs);
    }

    public DivideLinearLayout(Context context) {
        this(context, null);
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     */
    private void initAttribute(Context context, AttributeSet attrs) {
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.DivideLinearLayout);
        setDivideGravityInner(a.getInt(R.styleable.DivideLinearLayout_dl_divideGravity, NONE));
        setDivideSize(a.getDimension(R.styleable.DivideLinearLayout_dl_divideSize, getResources().getDimension(R.dimen.divideSize)));
        setItemDivideSize(a.getDimension(R.styleable.DivideLinearLayout_dl_itemDivideSize, getResources().getDimension(R.dimen.divideSize)));
        setDivideDrawable(a.getDrawable(R.styleable.DivideLinearLayout_dl_divideDrawable));
        setItemDivideDrawable(a.getDrawable(R.styleable.DivideLinearLayout_dl_itemDivideDrawable));
        setDividePadding(a.getDimension(R.styleable.DivideLinearLayout_dl_dividePadding, 0f));
        setLeftPadding(a.getDimension(R.styleable.DivideLinearLayout_dl_leftPadding, 0f));
        setItemDividePadding(a.getDimension(R.styleable.DivideLinearLayout_dl_itemDividePadding, 0f));
        a.recycle();
    }

    /**
     * 设置左边内边距
     *
     * @param padding
     */
    private void setLeftPadding(float padding) {
        this.leftPadding = Math.round(padding);
        invalidate();
    }


    public void setDivideGravity(@DivideGravity int gravity) {
        setDivideGravityInner(gravity);
    }

    public void setDivideGravityInner(int gravity) {
        this.gravity = gravity;
        invalidate();
    }

    /**
     * 设置分隔线宽
     *
     * @param size
     */
    public void setDivideSize(float size) {
        this.strokeSize = Math.round(size);
        invalidate();
    }

    /**
     * 设置条目分隔线宽
     *
     * @param size
     */
    public void setItemDivideSize(float size) {
        this.itemStrokeSize = Math.round(size);
        invalidate();
    }

    public void setDivideDrawable(Drawable drawable) {
        this.divideDrawable = drawable;
        invalidate();
    }

    public void setItemDivideDrawable(Drawable drawable) {
        this.itemDivideDrawable = drawable;
        invalidate();
    }

    /**
     * 设置绘制线边距
     *
     * @param padding
     */
    public void setDividePadding(float padding) {
        this.dividePadding = Math.round(padding);
        invalidate();
    }

    /**
     * 设置条目分隔线边距
     *
     * @param padding
     */
    public void setItemDividePadding(float padding) {
        this.itemDividePadding = Math.round(padding);
        invalidate();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        // 绘制周边分隔线
        drawDivide(canvas, 0 != (gravity & LEFT),
                0 != (gravity & TOP),
                0 != (gravity & RIGHT),
                0 != (gravity & BOTTOM));
        // 画divider
        drawItemDivide(canvas);
    }


    private void drawDivide(Canvas canvas, boolean drawLeft, boolean drawTop, boolean drawRight, boolean drawBottom) {
        if(null==divideDrawable)return;
        int width = getWidth();
        int height = getHeight();
        if (drawLeft) {
            divideDrawable.setBounds(0, dividePadding, strokeSize, height - dividePadding);
            divideDrawable.draw(canvas);
        }
        if (drawTop) {
            divideDrawable.setBounds(dividePadding, 0, width - dividePadding, strokeSize);
            divideDrawable.draw(canvas);
        }
        if (drawRight) {
            divideDrawable.setBounds(width - strokeSize, dividePadding, width, height - dividePadding);
            divideDrawable.draw(canvas);
        }
        if (drawBottom) {
            divideDrawable.setBounds(dividePadding + leftPadding, height - strokeSize, width - dividePadding, height);
            divideDrawable.draw(canvas);
        }
    }

    /**
     * 绘制条目分隔线
     *
     * @param canvas
     */
    private void drawItemDivide(Canvas canvas) {
        if(null==itemDivideDrawable) return;
        int width = getWidth();
        int height = getHeight();
        int orientation = getOrientation();
        int childCount = getChildCount();
        for (int i = 0; i < childCount - 1; i++) {
            View childView = getChildAt(i);
            if (View.VISIBLE == childView.getVisibility()) {
                int paddingLeft = getPaddingLeft();
                int paddingTop = getPaddingTop();
                int paddingRight = getPaddingRight();
                int paddingBottom = getPaddingBottom();
                if(0 != (gravity & ITEM_DIVIDE)){
                    if (HORIZONTAL == orientation) {
                        int right = childView.getRight();
                        itemDivideDrawable.setBounds(right-itemStrokeSize, itemDividePadding + paddingTop, right, height - itemDividePadding - paddingBottom);
                        itemDivideDrawable.draw(canvas);
                    } else if (VERTICAL == orientation) {
                        int bottom = childView.getBottom();
                        itemDivideDrawable.setBounds(itemDividePadding + paddingLeft, bottom-itemStrokeSize, width - itemDividePadding - paddingRight, bottom);
                        itemDivideDrawable.draw(canvas);
                    }
                }
            }
        }
    }


}
