package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;

import com.cz.library.R;


/**
 * TextView封装类,用于CompoundDrawable与文字同时居中,以及针对CompoundDrawable的尺寸限定
 * 实现思路,架空textView的text以及CompoundDrawable,自己绘制方位,控制大小
 * <p/>
 * Created by cz on 2014/6/3
 * <p/>
 * update 2015/11/10;重写思路, 架空textView的text以及CompoundDrawable, 自己绘制方位, 控制大小
 */
public class CenterTextView extends DivideTextView {
    //sizeMode Drawable尺寸大小控制
    private static final int NONE = 0x00;
    private static final int LEFT = 0x01;
    private static final int TOP = 0x02;
    private static final int RIGHT = 0x04;
    private static final int BOTTOM = 0x08;
    private static final int ALL = 0xf;

    //drawableMode Drawable的 设定drawable展示位置模式
    public static final int DRAWABLE_LEFT = 0x00;
    public static final int DRAWABLE_TOP = 0x01;
    public static final int DRAWABLE_RIGHT = 0x02;
    public static final int DRAWABLE_BOTTOM = 0x03;
    public static final int DRAWABLE_ALL = 0x04;
    private int sizeMode;
    private int drawableMode;
    private int drawableWidth;
    private int drawableHeight;
    private Drawable[] compoundDrawables;
    private CharSequence text;
    private TextPaint textPaint;
    private boolean isChecked;
    private boolean debug;
    private Paint paint;

    public CenterTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CenterTextView(Context context) {
        this(context, null, 0);
    }

    public CenterTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.CenterTextView);
        setDrawableWidth((int) a.getDimension(R.styleable.CenterTextView_cv_drawableWidth, -1));
        setDrawableHeight((int) a.getDimension(R.styleable.CenterTextView_cv_drawableHeight, -1));
        setDrawableMode(a.getInt(R.styleable.CenterTextView_cv_drawableMode, DRAWABLE_LEFT));
        setSizeMode(a.getInt(R.styleable.CenterTextView_cv_sizeMode, NONE));
        a.recycle();
    }

    /**
     * 展示debug绘制信息
     *
     * @param debug
     */
    public void setDebugDraw(boolean debug) {
        this.debug = debug;
        invalidate();
    }


    public void setSizeMode(int mode) {
        this.sizeMode = mode;
        invalidate();
    }

    public void setDrawableMode(int mode) {
        this.drawableMode = mode;
        invalidate();
    }


    public void setDrawableWidth(int width) {
        this.drawableWidth = width;
        invalidate();
    }

    public void setDrawableHeight(int height) {
        this.drawableHeight = height;
        invalidate();
    }

    @Override
    public void setText(CharSequence text, BufferType type) {
        super.setText(null, type);
        if (!TextUtils.isEmpty(text)) {
            this.text = text;
        }
    }

    public void setTextEmpty() {
        this.text = null;
        invalidate();
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        //动态设定选中状态
        Drawable[] compoundDrawables = this.compoundDrawables;
        for (int i = 0; i < compoundDrawables.length; i++) {
            if (null != compoundDrawables[i]) {
                compoundDrawables[i].setState(enabled ? ENABLED_STATE_SET : EMPTY_STATE_SET);
            }
        }
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        //动态设定选中状态
        Drawable[] compoundDrawables = this.compoundDrawables;
        for (int i = 0; i < compoundDrawables.length; i++) {
            if (null != compoundDrawables[i]) {
                compoundDrawables[i].setState(selected ? SELECTED_STATE_SET : EMPTY_STATE_SET);
            }
        }
    }

    @Override
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
        if (null == compoundDrawables) {
            compoundDrawables = new Drawable[4];
        }
        compoundDrawables[0] = left;
        compoundDrawables[1] = top;
        compoundDrawables[2] = right;
        compoundDrawables[3] = bottom;
        invalidate();
    }

    @NonNull
    @Override
    public Drawable[] getCompoundDrawables() {
        return compoundDrawables;
    }

    @Override
    public CharSequence getText() {
        return this.text;
    }

    /**
     * 根据模式获得drawable对象的高度
     *
     * @param drawable
     * @param isMode
     * @return
     */
    private int getDrawableHeight(Drawable drawable, boolean isMode) {
        if (null == drawable) return -1;
        int intrinsicHeight = drawable.getIntrinsicHeight();
        return isMode ? drawableHeight : intrinsicHeight;
    }

    /**
     * 根据模式获得drawable对象的宽度
     *
     * @param drawable
     * @param isMode
     * @return
     */
    private int getDrawableWidth(Drawable drawable, boolean isMode) {
        if (null == drawable) return -1;
        int intrinsicWidth = drawable.getIntrinsicWidth();
        return isMode ? drawableWidth : intrinsicWidth;
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //动态计算宽高
        Paint paint = getPaint();
        CharSequence text = this.text;
        int textWidth = 0, textHeight = 0;
        if (!TextUtils.isEmpty(text)) {
            textWidth = (int) paint.measureText(text.toString());//文字宽
            Rect textRect = new Rect();
            paint.getTextBounds(text.toString(), 0, text.length(), textRect);
            textHeight = textRect.height();
        }
        //初始化宽长度
        int width = getPaddingLeft() + getPaddingRight() + textWidth;
        Drawable[] compoundDrawables = this.compoundDrawables;
        if (null != compoundDrawables[0] || null != compoundDrawables[2]) {
            width += getCompoundDrawablePadding();
            if (null != compoundDrawables[0]) {
                width += compoundDrawables[0].getIntrinsicWidth();
            }
            if (null != compoundDrawables[2]) {
                width += compoundDrawables[2].getIntrinsicWidth();
            }
        }
        //初始化高宽度
        int height = getPaddingTop() + getPaddingBottom() + textHeight;
        if (null != compoundDrawables[1] || null != compoundDrawables[3]) {
            height += getCompoundDrawablePadding();
            if (null != compoundDrawables[1]) {
                height += compoundDrawables[1].getIntrinsicHeight();
            }
            if (null != compoundDrawables[3]) {
                height += compoundDrawables[3].getIntrinsicHeight();
            }
        }
        setMeasuredDimension(resolveSize(width, widthMeasureSpec), resolveSize(height, heightMeasureSpec));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Drawable[] drawables = compoundDrawables;
        int drawablePadding = getCompoundDrawablePadding();
        if (DRAWABLE_ALL != drawableMode) {
            draw(0, drawables, drawableMode == DRAWABLE_LEFT || drawableMode == DRAWABLE_ALL, canvas, drawablePadding,
                    getDrawableWidth(drawables[0], sizeMode == (sizeMode | LEFT)),
                    getDrawableHeight(drawables[0], sizeMode == (sizeMode | LEFT)));
            draw(1, drawables, drawableMode == DRAWABLE_TOP || drawableMode == DRAWABLE_ALL, canvas, drawablePadding,
                    getDrawableWidth(drawables[1], sizeMode == (sizeMode | TOP)),
                    getDrawableHeight(drawables[1], sizeMode == (sizeMode | TOP)));
            draw(2, drawables, drawableMode == DRAWABLE_RIGHT || drawableMode == DRAWABLE_ALL, canvas, drawablePadding,
                    getDrawableWidth(drawables[2], sizeMode == (sizeMode | RIGHT)),
                    getDrawableHeight(drawables[2], sizeMode == (sizeMode | RIGHT)));
            draw(3, drawables, drawableMode == DRAWABLE_BOTTOM || drawableMode == DRAWABLE_ALL, canvas, drawablePadding,
                    getDrawableWidth(drawables[3], sizeMode == (sizeMode | BOTTOM)),
                    getDrawableHeight(drawables[3], sizeMode == (sizeMode | BOTTOM)));
        } else {
            //所有图片整体居中
            drawCenterDrawables(canvas);
        }

        //绘制文字
        drawText(drawableMode, canvas, drawablePadding);
        if (debug) {
            int width = getWidth();
            int height = getHeight();
            //横线
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(4);
            canvas.drawLine(0, height / 2, width, height / 2, paint);
            //竖线
            canvas.drawLine(width / 2, 0, width / 2, height, paint);
        }
    }

    /**
     * 所有图片以中心位置居中
     */
    private void drawCenterDrawables(Canvas canvas) {
        TextPaint paint = getPaint();
        int width = getWidth();
        int height = getHeight();
        int textWidth = 0, textHeight = 0;
        int centerX = width / 2, centerY = 0;
        int drawableWidth, drawableHeight;
        int drawablePadding = getCompoundDrawablePadding();
        //计算出字体宽高,如果文字无,那么设置图片居中
        if (!TextUtils.isEmpty(text)) {
            String text = this.text.toString();
            textWidth = (int) paint.measureText(text);//文字宽
            Rect textRect = new Rect();
            paint.getTextBounds(text, 0, text.length(), textRect);
            textHeight = textRect.height();
            centerX = (width - textWidth) / 2;
            centerY = (height - textHeight) / 2;
        }
        Drawable[] drawables = compoundDrawables;
        if (null != drawables[0]) {
            //左边图片
            drawableWidth = getDrawableWidth(drawables[0], sizeMode == (sizeMode | LEFT));
            drawableHeight = getDrawableHeight(drawables[0], sizeMode == (sizeMode | LEFT));
            int leftCenterY = (height - drawableHeight) / 2;
            drawables[0].setBounds(centerX - drawablePadding - drawableWidth, leftCenterY, centerX - drawablePadding, leftCenterY + drawableHeight);
            drawables[0].draw(canvas);
        }
        if (null != drawables[1]) {
            //上边图片
            drawableWidth = getDrawableWidth(drawables[1], sizeMode == (sizeMode | TOP));
            drawableHeight = getDrawableHeight(drawables[1], sizeMode == (sizeMode | TOP));
            int topCenterX = (width - drawableWidth) / 2;
            drawables[1].setBounds(topCenterX, centerY - drawablePadding - drawableHeight, topCenterX + drawableWidth, centerY - drawablePadding);
            drawables[1].draw(canvas);
        }
        if (null != drawables[2]) {
            //右边图片
            drawableWidth = getDrawableWidth(drawables[2], sizeMode == (sizeMode | RIGHT));
            drawableHeight = getDrawableHeight(drawables[2], sizeMode == (sizeMode | RIGHT));
            int rightCenterY = (height - drawableHeight) / 2;
            drawables[2].setBounds(centerX + drawablePadding + textWidth, rightCenterY, centerX + textWidth + drawablePadding + drawableWidth, rightCenterY + drawableHeight);
            drawables[2].draw(canvas);
        }
        if (null != drawables[3]) {
            //底部图片
            drawableWidth = getDrawableWidth(drawables[3], sizeMode == (sizeMode | BOTTOM));
            drawableHeight = getDrawableHeight(drawables[3], sizeMode == (sizeMode | BOTTOM));
            int bottomCenterX = (width - drawableWidth) / 2;
            drawables[3].setBounds(bottomCenterX, centerY + drawablePadding + textHeight, bottomCenterX + drawableWidth, centerY + drawablePadding + drawableHeight + textHeight);
            drawables[3].draw(canvas);
        }
    }

    /**
     * 绘制文字
     *
     * @param mode
     * @param canvas
     * @param drawablePadding
     */
    private void drawText(int mode, Canvas canvas, int drawablePadding) {
        TextPaint paint = getPaint();
        int width = getWidth();
        int height = getHeight();
        //计算出字体宽高,如果文字无,那么设置图片居中
        if (!TextUtils.isEmpty(text)) {
            String text = this.text.toString();
            Drawable[] drawables = compoundDrawables;
            float textWidth = paint.measureText(text);//文字宽
            Rect textRect = new Rect();
            paint.getTextBounds(text, 0, text.length(), textRect);
            float centerX, centerY = (height - (paint.descent() + paint.ascent())) / 2;
            int drawableWidth, drawableHeight;
            //绘制文字,两个图片以上,让文字完全居中,一个时,文字和图片居中
            switch (mode) {
                case DRAWABLE_ALL:
                    canvas.drawText(text, (width - textWidth) / 2, centerY, paint);//完全居中
                    break;
                case DRAWABLE_LEFT:
                    drawableWidth = getDrawableWidth(drawables[0], sizeMode == (sizeMode | LEFT));
                    drawablePadding = (0 == drawableWidth) ? 0 : drawablePadding;
                    centerX = (width - drawableWidth - drawablePadding - textWidth) / 2;
                    canvas.drawText(text, centerX + drawableWidth + drawablePadding, centerY, paint);
                    break;
                case DRAWABLE_TOP:
                    drawableHeight = getDrawableHeight(drawables[1], sizeMode == (sizeMode | TOP));
                    drawablePadding = (0 == drawableHeight) ? 0 : drawablePadding;
                    centerX = (width - textWidth) / 2;
                    centerY -= (drawableHeight + drawablePadding) / 2;
                    canvas.drawText(text, centerX, centerY + drawableHeight + drawablePadding, paint);
                    break;
                case DRAWABLE_RIGHT:
                    drawableWidth = getDrawableWidth(drawables[2], sizeMode == (sizeMode | RIGHT));
                    drawablePadding = (0 == drawableWidth) ? 0 : drawablePadding;
                    centerX = (width - drawableWidth - drawablePadding - textWidth) / 2;
                    canvas.drawText(text, centerX, centerY, paint);
                    break;
                case DRAWABLE_BOTTOM:
                    drawableHeight = getDrawableHeight(drawables[3], sizeMode == (sizeMode | BOTTOM));
                    drawablePadding = (0 == drawableHeight) ? 0 : drawablePadding;
                    centerX = (width - textWidth) / 2;
                    centerY -= (drawableHeight + drawablePadding) / 2;
                    canvas.drawText(text, centerX, centerY, paint);
                    break;
            }
        }
    }


    /**
     * 绘制指定方向drawable对象
     *
     * @param index
     * @param drawables
     * @param mode
     * @param drawablePadding
     */
    private void draw(int index, Drawable[] drawables, boolean mode, Canvas canvas, int drawablePadding, int drawableWidth, int drawableHeight) {
        if (null == drawables[index]) return;
        int width = getWidth();
        int height = getHeight();
        float textWidth = 0, textHeight = 0;
        //计算出字体宽高,如果文字无,那么设置图片居中
        if (!TextUtils.isEmpty(text)) {
            String text = this.text.toString();
            TextPaint paint = getPaint();
            textWidth = paint.measureText(text);//文字宽
            Rect textRect = new Rect();
            paint.getTextBounds(text, 0, text.length(), textRect);
            textHeight = textRect.height();//文字高
        }
        Drawable drawable = drawables[index];
        //绘制drawable
        drawCompoundDrawable(index, mode, canvas, drawablePadding, drawableWidth, drawableHeight, drawable, width, height, textWidth, textHeight);

    }

    /**
     * 绘制drawable对象
     *
     * @param index
     * @param mode
     * @param canvas
     * @param drawablePadding
     * @param drawableWidth
     * @param drawableHeight
     * @param drawable
     * @param width
     * @param height
     * @param textWidth
     * @param textHeight
     */
    private void drawCompoundDrawable(int index, boolean mode, Canvas canvas, int drawablePadding, int drawableWidth, int drawableHeight, Drawable drawable, int width, int height, float textWidth, float textHeight) {
        //绘drawable对象
        int centerX = (int) ((width - drawableWidth - drawablePadding - textWidth) / 2);
        int centerY = (int) ((height - drawableHeight - drawablePadding - textHeight) / 2);
        //如果应用居中模式,则设置居中
        if (mode) {
            switch (index) {
                case 0:
                    centerY = (height - drawableHeight) / 2;
                    drawable.setBounds(centerX, centerY, centerX + drawableWidth, centerY + drawableHeight);//左边
                    break;
                case 1:
                    centerX = (width - drawableWidth) / 2;
                    drawable.setBounds(centerX, centerY, centerX + drawableWidth, centerY + drawableHeight);//上边
                    break;
                case 2:
                    centerY = (height - drawableHeight) / 2;
                    drawable.setBounds(centerX + drawablePadding + (int) textWidth, centerY, centerX + drawablePadding + (int) textWidth + drawableWidth, centerY + drawableHeight);//右边
                    break;
                case 3:
                    centerX = (width - drawableWidth) / 2;
                    drawable.setBounds(centerX, centerY + drawablePadding + (int) textHeight, centerX + drawableWidth, centerY + drawablePadding + (int) textHeight + drawableHeight);//下边
                    break;
            }
            if (debug) {
                switch (index) {
                    case 0:
                    case 2:
                        centerY = (int) ((height - Math.max(drawableHeight, textHeight)) / 2);
                        canvas.drawRect(centerX, centerY, centerX + drawableWidth + textWidth + drawablePadding, centerY + Math.max(drawableHeight, textHeight), paint);
                        break;
                    case 1:
                    case 3:
                        centerX = (int) ((width - Math.max(drawableWidth, textWidth)) / 2);
                        canvas.drawRect(centerX, centerY, centerX + Math.max(drawableWidth, textWidth), centerY + drawableHeight + textHeight + drawablePadding, paint);
                        break;
                }
            }
        } else {
            //应用常规模式
            int paddingLeft = getPaddingLeft();
            int paddingTop = getPaddingTop();
            int paddingRight = getPaddingRight();
            int paddingBottom = getPaddingBottom();
            switch (index) {
                case 0:
                    centerY = (height - drawableHeight) / 2;
                    drawable.setBounds(paddingLeft, centerY, paddingLeft + drawableWidth, centerY + drawableHeight);//左边
                    break;
                case 1:
                    centerX = (width - drawableWidth) / 2;
                    drawable.setBounds(centerX, paddingTop, centerX + drawableWidth, paddingTop + drawableHeight);//上边
                    break;
                case 2:
                    centerY = (height - drawableHeight) / 2;
                    drawable.setBounds(width - paddingRight - drawableWidth, centerY, width - paddingRight, centerY + drawableHeight);//右边
                    break;
                case 3:
                    centerX = (width - drawableWidth) / 2;
                    drawable.setBounds(centerX, height - paddingBottom - drawableHeight, centerX + drawableWidth, height - paddingBottom);//底边
                    break;
            }
        }
        drawable.draw(canvas);
    }
}
