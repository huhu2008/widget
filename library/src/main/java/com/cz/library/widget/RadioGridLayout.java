package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;

import com.cz.library.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 一个自定义多选组,继承自CenterGridLayout故条目是居中的
 * <p/>
 * Created by cz on 2015/2/12
 */
public class RadioGridLayout extends CenterGridLayout {
    // 三种选择状态
    public static final int SINGLE_CHOOSE = 0;//单选
    public static final int MORE_CHOOSE = 1;//多选
    public static final int RECTANGLE_CHOOSE = 2;//块选择
    private static final String TAG = "RadioGridLayout";

    @IntDef(value = {SINGLE_CHOOSE, MORE_CHOOSE, RECTANGLE_CHOOSE})
    public @interface ChoiceMode {
    }

    public static final int LEFT = Gravity.LEFT;
    public static final int TOP = Gravity.TOP;
    public static final int RIGHT = Gravity.RIGHT;
    public static final int BOTTOM = Gravity.BOTTOM;

    private Drawable buttonDrawable;
    private Drawable itemSelectorDrawable;
    private int imageWidth;//drawable宽
    private int imageHeight;//drawable高
    private int horizontalPadding;//横向边距
    private int verticalPadding;//纵向边距
    private int imageGravity;//image展示方向
    private int choiceMode;// 选择状态
    private OnCheckListener choiceListener;

    private int singleChoiceIndex;// 选中位置
    private ArrayList<Integer> multChoiceItems;//选中集
    private int start, end;


    public RadioGridLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RadioGridLayout(Context context) {
        this(context, null, 0);
    }

    public RadioGridLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setWillNotDraw(false);
        singleChoiceIndex=-1;
        multChoiceItems = new ArrayList<>();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.RadioGridLayout);
        setButtonImage(a.getDrawable(R.styleable.RadioGridLayout_rl_buttonImage));
        setButtonItemSelector(a.getDrawable(R.styleable.RadioGridLayout_rl_buttonItemSelector));
        setButtonWidth((int) a.getDimension(R.styleable.RadioGridLayout_rl_imageWidth, 0));
        setButtonHeight((int) a.getDimension(R.styleable.RadioGridLayout_rl_imageHeight, 0));
        setButtonHorizontalPadding((int) a.getDimension(R.styleable.RadioGridLayout_rl_buttonHorizontalPadding, 0));
        setButtonVerticalPadding((int) a.getDimension(R.styleable.RadioGridLayout_rl_buttonVerticalPadding, 0));
        setImageGravity(a.getInt(R.styleable.RadioGridLayout_rl_imageGravity, Gravity.RIGHT | Gravity.BOTTOM));
        setChoiceModeInner(a.getInt(R.styleable.RadioGridLayout_rl_choiceMode, SINGLE_CHOOSE));//设置选择模式
        a.recycle();
    }

    public void setButtonItemSelector(Drawable itemSelector) {
        this.itemSelectorDrawable=itemSelector;
        refreshItemSelector();
    }

    /**
     * 设置drawable对象
     *
     * @param res
     */
    public void setButtonImage(@DrawableRes int res) {
        buttonDrawable = getResources().getDrawable(res);
        invalidate();
    }

    /**
     * 设置drawable对象
     *
     * @param drawable
     */
    public void setButtonImage(Drawable drawable) {
        buttonDrawable = drawable;
        invalidate();
    }

    /**
     * 设置图片宽
     *
     * @param width
     */
    public void setButtonWidth(int width) {
        this.imageWidth = width;
        invalidate();
    }

    /**
     * 设置图片高
     *
     * @param height
     */
    public void setButtonHeight(int height) {
        this.imageHeight = height;
        invalidate();
    }

    /**
     * 设置图像横向边距
     *
     * @param padding
     */
    public void setButtonHorizontalPadding(int padding) {
        this.horizontalPadding = padding;
        invalidate();
    }

    /**
     * 设置选中图象纵向边距
     *
     * @param padding
     */
    public void setButtonVerticalPadding(int padding) {
        this.verticalPadding = padding;
        invalidate();
    }

    /**
     * 设置图片方向
     *
     * @param gravity
     */
    public void setImageGravity(int gravity) {
        this.imageGravity = gravity;
        invalidate();
    }

    /**
     * 设置选择模式
     *
     * @param mode
     */
    public void setChoiceMode(@ChoiceMode int mode) {
        this.choiceMode = mode;
        switch (choiceMode) {
            case MORE_CHOOSE:
                singleChoiceIndex = start = end = -1;
                break;
            case RECTANGLE_CHOOSE:
                singleChoiceIndex=-1;
                multChoiceItems.clear();
                break;
            case SINGLE_CHOOSE:
            default:
                start = end = -1;
                multChoiceItems.clear();
                break;
        }
        setItemSelect(false);
        invalidate();
    }

    /**
     * 设置选择模式
     *
     * @param mode
     */
    private void setChoiceModeInner(int mode) {
        setChoiceMode(mode);
    }

    @Override
    public void addView(View child, int index) {
        super.addView(child, index);
        setViewListener(child);
        setBackgroundDrawableCompat(child);
    }

    private void refreshItemSelector(){
        int childCount = getChildCount();
        for(int i=0;i<childCount;i++){
            setBackgroundDrawableCompat(getChildAt(i));
        }
    }

    private void setBackgroundDrawableCompat(View childView) {
        if(null!=itemSelectorDrawable) {
            Drawable newDrawable = itemSelectorDrawable.getConstantState().newDrawable();
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN) {
                childView.setBackgroundDrawable(newDrawable);
            } else {
                childView.setBackground(newDrawable);
            }
        }
    }

    /**
     * 设置view选中事件
     *
     * @param view
     */
    private void setViewListener(final View view) {
        view.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = indexOfChild(v);
                switch (choiceMode) {
                    case MORE_CHOOSE:
                        if (multChoiceItems.contains(index)) {
                            v.setSelected(false);
                            multChoiceItems.remove(Integer.valueOf(index));
                        } else {
                            v.setSelected(true);
                            multChoiceItems.add(Integer.valueOf(index));
                        }
                        if (null != choiceListener) {
                            choiceListener.onMultiChoice(v, multChoiceItems);
                        }
                        break;
                    case RECTANGLE_CHOOSE:
                        if (-1 != start && -1 != end) {
                            setItemSelect(false);
                            start = end = -1;//重置
                        } else if (-1 == start) {
                            start = index;
                            v.setSelected(true);
                        } else if (-1 == end) {
                            end = index;
                            for(int i=Math.min(start,end);i<=Math.max(start,end);i++){
                                getChildAt(i).setSelected(true);
                            }
                            if (null != choiceListener) {
                                choiceListener.onRectangleChoice(start, end);
                            }
                        }
                        break;
                    case SINGLE_CHOOSE:
                    default:
                        v.setSelected(true);
                        if(-1!=singleChoiceIndex) getChildAt(singleChoiceIndex).setSelected(false);
                        if (null != choiceListener) {
                            choiceListener.onSingleChoice(v, index, singleChoiceIndex);
                        }
                        singleChoiceIndex = index;
                        break;
                }
                invalidate();
            }
        });
    }

    private void setItemSelect(boolean select) {
        int childCount = getChildCount();
        for(int i=0;i<childCount;i++){
            getChildAt(i).setSelected(select);
        }
    }

    /**
     * 获得单选位置
     *
     * @return
     */
    public int getSingleChoiceIndex() {
        return singleChoiceIndex;
    }

    /**
     * 获得多选位置
     *
     * @return
     */
    public List<Integer> getMultiChoiceIndexs() {
        return this.multChoiceItems;
    }

    /**
     * 获得截取开始位置
     *
     * @return
     */
    public int getRactangleStartIndex() {
        return start;
    }

    /**
     * 获得截取结束位置
     *
     * @return
     */
    public int getRactangleEndIndex() {
        return end;
    }

    /**
     * 设置选择监听
     *
     * @param listener
     */
    public void setOnCheckedListener(OnCheckListener listener) {
        this.choiceListener = listener;
    }


    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);
        if (0 >= getChildCount() || null == buttonDrawable) return;
        switch (choiceMode) {
            case MORE_CHOOSE:
                for (Integer index : multChoiceItems) {
                    drawChoiceView(canvas, getChildAt(index));
                }
                break;
            case RECTANGLE_CHOOSE:
                if (-1 != start && -1 != end) {
                    for (int i = Math.min(start, end); i <= Math.max(start, end); i++) {
                        drawChoiceView(canvas, getChildAt(i));
                    }
                } else if (-1 != start) {
                    drawChoiceView(canvas, getChildAt(start));
                }
                break;
            case SINGLE_CHOOSE:
            default:
                if (0 <= singleChoiceIndex) {
                    drawChoiceView(canvas, getChildAt(singleChoiceIndex));
                }
                break;
        }
    }

    /**
     * 绘制选中drawable
     *
     * @param childView
     */
    private void drawChoiceView(Canvas canvas, View childView) {
        Rect outRect = new Rect();
        childView.getHitRect(outRect);
        imageWidth = 0 == imageWidth ? buttonDrawable.getIntrinsicWidth() : imageWidth;
        imageHeight = 0 == imageHeight ? buttonDrawable.getIntrinsicHeight() : imageHeight;
        switch (imageGravity) {
            case LEFT:
            case TOP:
            case LEFT | TOP:
                //左上
                drawDrawable(canvas, outRect.left + horizontalPadding, outRect.top + verticalPadding, outRect.left + imageWidth + horizontalPadding, outRect.top + imageHeight + verticalPadding);
                break;
            case RIGHT:
            case RIGHT | TOP:
                //右上
                drawDrawable(canvas, outRect.right - horizontalPadding - imageWidth, outRect.top + verticalPadding, outRect.right - horizontalPadding, outRect.top + imageHeight + verticalPadding);
                break;
            case LEFT | RIGHT:
            case LEFT | RIGHT | TOP:
                //左右居中
                drawDrawable(canvas, outRect.centerX() - imageWidth / 2, outRect.top + verticalPadding, outRect.centerX() + imageWidth / 2, outRect.top + imageHeight + verticalPadding);
                break;
            case LEFT | RIGHT | BOTTOM:
                //左右下居中
                drawDrawable(canvas, outRect.centerX() - imageWidth / 2, outRect.bottom - verticalPadding - imageHeight, outRect.centerX() + imageWidth / 2, outRect.bottom - verticalPadding);
                break;
            case TOP | BOTTOM:
            case TOP | BOTTOM | LEFT:
                //上下左居中
                drawDrawable(canvas, outRect.left + horizontalPadding, outRect.centerY() - imageHeight / 2, outRect.left + horizontalPadding + imageWidth, outRect.centerY() + imageHeight / 2);
                break;
            case TOP | BOTTOM | RIGHT:
                //上下右居中
                drawDrawable(canvas, outRect.right - horizontalPadding - imageWidth, outRect.centerY() - imageHeight / 2, outRect.right - horizontalPadding, outRect.centerY() + imageHeight / 2);
                break;
            case BOTTOM:
            case LEFT | BOTTOM:
                //左下
                drawDrawable(canvas, outRect.left + horizontalPadding, outRect.bottom - verticalPadding - imageHeight, outRect.left + horizontalPadding + imageWidth, outRect.bottom - verticalPadding);
                break;
            case RIGHT | BOTTOM:
                //右下
                drawDrawable(canvas, outRect.right - horizontalPadding - imageWidth, outRect.bottom - verticalPadding - imageHeight, outRect.right - horizontalPadding, outRect.bottom - verticalPadding);
                break;
            case LEFT | TOP | RIGHT | BOTTOM:
                //居中
                drawDrawable(canvas, outRect.centerX() - imageWidth / 2, outRect.centerY() - imageHeight / 2, outRect.centerX() + imageWidth / 2, outRect.centerY() + imageHeight / 2);
                break;
        }
    }

    /**
     * 绘制选中标记
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    private void drawDrawable(Canvas canvas, int left, int top, int right, int bottom) {
        buttonDrawable.setBounds(left, top, right, bottom);
        buttonDrawable.draw(canvas);
    }


    /**
     * 选择监听器
     */
    public interface OnCheckListener {
        void onSingleChoice(View v, int newPosition, int oldPosition);

        void onMultiChoice(View v, ArrayList<Integer> mChoicePositions);

        void onRectangleChoice(int startPosition, int endPosition);
    }

}
