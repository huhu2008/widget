package com.cz.library.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.DrawableRes;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

import com.cz.library.R;
import com.cz.library.widget.validator.Validator;
import com.cz.library.widget.validator.ValidatorObserver;
import com.cz.library.widget.validator.impl.LengthValidator;
import com.cz.library.widget.validator.impl.NotEmptyValidator;
import com.cz.library.widget.validator.impl.RangeValidator;
import com.cz.library.widget.validator.impl.pattern.CharacterValidator;
import com.cz.library.widget.validator.impl.pattern.EmailValidator;
import com.cz.library.widget.validator.impl.pattern.IdValidator;
import com.cz.library.widget.validator.impl.pattern.NumberValidator;
import com.cz.library.widget.validator.impl.pattern.PatternValidator;
import com.cz.library.widget.validator.impl.pattern.PatternValueValidator;
import com.cz.library.widget.validator.impl.pattern.PhoneValidator;
import com.cz.library.widget.validator.impl.pattern.WebUrlValidator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created by cz on 24/9/16.
 * 此类为简化表单输入,主要功能分为几块:
 * 1:支持右侧自定义控件扩展(添加文字,验证码等)
 * 2:支持自定义条件匹配(长度,长度区间,以及条件扩展)
 *   2.1:sv_validatorValue:用作条件信息输入,比如length 则输入length:4 range则为:rage:4-10
 * 3:支持正则匹配(sv_patternValidator,支持预设部分正则(手机号,邮箱...),以及直接输入正则字符串方式生成.属性为:enum|string
 *
 * ValidatorObserver为多个EditLayout的操作类.负责动态监听信息是否匹配,以及主动匹配功能.并能获取未匹配时的预设异常信息.完全所有表单的信息检测逻辑
 * @see ValidatorObserver
 *
 * 示例见:module app-> EditLayoutFragment
 */
public class EditLayout extends DivideLinearLayout {
    private static final String TAG = "EditLayout";

    public static final int VALIDATOR_NOT_EMPTY=0x01<<0;
    public static final int VALIDATOR_LENGTH=0x01<<1;
    public static final int VALIDATOR_RANGE=0x01<<2;
    public static final int PATTERN_CHARACTER=0x01<<3;
    public static final int PATTERN_EMAIL=0x01<<4;
    public static final int PATTERN_NUMBER=0x01<<5;
    public static final int PATTERN_PHONE=0x01<<6;
    public static final int PATTERN_WEB_URL=0x01<<7;
    public static final int PATTERN_ID=0x01<<8;

    public static final int INPUT_NUMBER=0x01;
    public static final int INPUT_TEXT=0x02;
    public static final int INPUT_NUMBER_PASSWORD=0x03;
    public static final int INPUT_TEXT_PASSWORD=0x04;

    static final int STYLE_NUM_ENTRIES = 6;
    private static final HashMap<String,Integer> validatorFlags;

    static {
        validatorFlags=new HashMap<>();
        validatorFlags.put("not_empty",VALIDATOR_NOT_EMPTY);
        validatorFlags.put("length",VALIDATOR_LENGTH);
        validatorFlags.put("range",VALIDATOR_RANGE);
    }

    private final List<Validator> validatorItems;
    private final SparseArray<String> validatorItemValues;
    private OnFocusChangeListener onFocusChangeListener;
    private ImageView hintView;
    private EditText editor;
    private ImageView deleteView;
    private int errorTextColor;
    private OnTextChangeListener listener;
    private String editError;
    private String emptyError;
    private String errorInfo;
    private int textColor;

    public EditLayout(Context context) {
        this(context,null);
    }

    public EditLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        inflate(context, R.layout.edit_layout, this);
        setGravity(Gravity.CENTER_VERTICAL);

        this.validatorItems=new ArrayList<>();
        this.validatorItemValues=new SparseArray<>();
        hintView= (ImageView) findViewById(R.id.iv_hint_icon);
        editor= (EditText) findViewById(R.id.et_editor);
        deleteView= (ImageView) findViewById(R.id.iv_delete_icon);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.EditLayout);
        setHintDrawable(a.getDrawable(R.styleable.EditLayout_sv_hintDrawable));
        setEditHint(a.getString(R.styleable.EditLayout_sv_editHint));
        setEditText(a.getString(R.styleable.EditLayout_sv_editText));
        setEditHintTextColor(a.getColor(R.styleable.EditLayout_sv_editHintTextColor, Color.DKGRAY));
        setEditTextColor(a.getColor(R.styleable.EditLayout_sv_editTextColor, Color.GRAY));
        setEditTextSize(a.getDimensionPixelSize(R.styleable.EditLayout_sv_editTextSize,0));
        setSearchDeleteDrawable(a.getDrawable(R.styleable.EditLayout_sv_editDeleteDrawable));
        setEditPadding((int) a.getDimension(R.styleable.EditLayout_sv_editPadding,0));
        setEditErrorTextColor(a.getColor(R.styleable.EditLayout_sv_editErrorTextColor,NO_ID));
        setEditError(a.getString(R.styleable.EditLayout_sv_editError));
        setEditEmptyError(a.getString(R.styleable.EditLayout_sv_editEmptyError));
        setEditValidatorValue(a.getString(R.styleable.EditLayout_sv_validatorValue));
        setEditValidator(a.getInt(R.styleable.EditLayout_sv_validator,VALIDATOR_NOT_EMPTY));
        setPatternValidator(a.getString(R.styleable.EditLayout_sv_patternValidator));
        setEditInputType(a.getInt(R.styleable.EditLayout_sv_editInputType, INPUT_TEXT));
        setEditMaxLength(a.getInteger(R.styleable.EditLayout_sv_editMaxLength, Short.MAX_VALUE));
        setEditMaxLine(a.getInteger(R.styleable.EditLayout_sv_editMaxLine,1));
        a.recycle();
    }


    private void setPatternValidator(String patternValue) {
        if(!TextUtils.isEmpty(patternValue)){
            String[] patternArray = patternValue.split("\\s+");
            if(null!=patternArray){
                for(int i=0;i<patternArray.length;i++){
                    this.validatorItems.add(new PatternValueValidator(patternArray[i]));
                }
            }
        }
    }

    private int getAttrType(TypedArray typedArray,int index){
        int attrType=TypedValue.TYPE_NULL;
        Class<TypedArray> clazz = TypedArray.class;
        try {
            Field field = clazz.getDeclaredField("mData");
            field.setAccessible(true);
            Object obj = field.get(typedArray);
            if(null!=obj){
                int[] data= (int[]) obj;
                index *= STYLE_NUM_ENTRIES;
                final int type = data[index];
                if (type >= TypedValue.TYPE_FIRST_INT && type <= TypedValue.TYPE_LAST_INT) {
                    attrType=TypedValue.TYPE_INT_HEX;
                } else if (type == TypedValue.TYPE_STRING) {
                    attrType=TypedValue.TYPE_STRING;
                }
            }
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return attrType;
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        deleteView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editor.getText())) {
                    editor.setText(null);
                    v.setVisibility(View.GONE);
                }
            }
        });
        editor.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                hintView.setSelected(b);
                if(null!=onFocusChangeListener){
                    onFocusChangeListener.onFocusChange(view,b);
                }
            }
        });
        editor.addTextChangedListener(new TextWatcher() {
            private CharSequence lastItem = null;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //标记异常颜色
                if(NO_ID!=errorTextColor){
                    editor.setTextColor(isValid()?textColor:errorTextColor);
                }
                deleteView.setVisibility(TextUtils.isEmpty(editor.getText()) ? View.GONE : View.VISIBLE);
                if (null != listener) {
                    listener.onTextChanged(s, lastItem, count);
                }
                lastItem = s;
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    public void setOnFocusChangeListener(OnFocusChangeListener listener){
        this.onFocusChangeListener=listener;
    }

    public void setHintDrawableResource(@DrawableRes int res) {
        setHintDrawable(getResources().getDrawable(res));
    }

    public void setHintDrawable(Drawable drawable) {
        if(null!=drawable){
            hintView.setVisibility(View.VISIBLE);
            hintView.setImageDrawable(drawable);
        } else {
            hintView.setVisibility(View.GONE);
        }
    }

    public void setEditHint(String text) {
        editor.setHint(text);
    }

    public void setEditText(String text) {
        editor.setText(text);
        editor.setSelection(TextUtils.isEmpty(text)?0:text.length());
    }

    public void setEditHintTextColor(int color) {
        editor.setHintTextColor(color);
    }

    public void setEditTextColor(int color) {
        this.textColor=color;
        editor.setTextColor(color);
    }

    public void setEditErrorTextColor(int color) {
        this.errorTextColor=color;
    }

    public void setEditTextSize(int textSize){
        editor.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
    }

    public void setEditInputType(int inputType){
        switch (inputType){
            case INPUT_NUMBER:
            case INPUT_NUMBER_PASSWORD:
                editor.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_VARIATION_NORMAL);
                break;
            case INPUT_TEXT:
            case INPUT_TEXT_PASSWORD:
                default:
                editor.setInputType(InputType.TYPE_CLASS_TEXT);
                break;
        }
        setEditPasswordTransformation(INPUT_NUMBER == inputType || INPUT_TEXT == inputType);
    }

    public void setEditPasswordTransformation(boolean show){
        if (show) {
            editor.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            editor.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    public void setEditPadding(int padding) {
        editor.setPadding(padding, 0, padding, 0);
    }

    public void setEditError(String text) {
        Log.e(TAG,"text:"+text);
        this.editError=text;
    }

    public void setEditEmptyError(String emptyError) {
        this.emptyError=emptyError;
    }

    public void setEditMaxLength(int maxLength) {
        editor.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLength)});
    }

    public void setEditMaxLine(int maxLine) {
        editor.setMaxLines(maxLine);
    }


    private void setEditValidator(int flag) {
        validatorItems.clear();
        if(flag==(VALIDATOR_NOT_EMPTY|flag)){
            validatorItems.add(new NotEmptyValidator());
        }
        if(flag==(VALIDATOR_LENGTH|flag)){
            String propertyValue = validatorItemValues.get(VALIDATOR_LENGTH);
            if(!TextUtils.isEmpty(propertyValue)){
                validatorItems.add(new LengthValidator(Integer.valueOf(propertyValue)));
            }
        }
        if(flag==(VALIDATOR_RANGE|flag)){
            String propertyValue = validatorItemValues.get(VALIDATOR_RANGE);
            if(!TextUtils.isEmpty(propertyValue)){
                Pattern pattern = Pattern.compile("(\\d+)-(\\d+)");
                Matcher matcher = pattern.matcher(propertyValue);
                if(matcher.find()){
                    String start = matcher.group(1);
                    String end = matcher.group(2);
                    validatorItems.add(new RangeValidator(Integer.valueOf(start),Integer.valueOf(end)));
                }
            }
        }
        if(flag==(PATTERN_EMAIL|flag)){
            validatorItems.add(new EmailValidator());
        }
        if(flag==(PATTERN_NUMBER|flag)){
            validatorItems.add(new NumberValidator());
        }
        if(flag==(PATTERN_PHONE|flag)){
            validatorItems.add(new PhoneValidator());
        }
        if(flag==(PATTERN_WEB_URL|flag)){
            validatorItems.add(new WebUrlValidator());
        }
        if(flag==(PATTERN_CHARACTER|flag)){
            validatorItems.add(new CharacterValidator());
        }
        if(flag==(PATTERN_ID|flag)){
            validatorItems.add(new IdValidator());
        }
    }

    private void setEditValidatorValue(String value) {
        if(!TextUtils.isEmpty(value)){
            Pattern pattern = Pattern.compile("(\\w+):([\\w-]+)\\s*");
            Matcher matcher = pattern.matcher(value);
            while(matcher.find()){
                String property=matcher.group(1);
                String propertyValue = matcher.group(2);
                if(validatorFlags.containsKey(property)){
                    Integer flag = validatorFlags.get(property);
                    if(!TextUtils.isEmpty(propertyValue)){
                        validatorItemValues.put(flag,propertyValue);
                    }
                }
            }
        }

    }

    public String getEditError(){
        return errorInfo;
    }

    public boolean isValid(){
        Editable text = getText();
        boolean result=true;
        boolean patternResult=false;
        boolean patternMatches=false;
        for(int i=0;i<validatorItems.size();i++){
            Validator validator = validatorItems.get(i);
            if(!patternMatches&&validator instanceof PatternValidator){
                //正则匹配对象,任一正则匹配成功返回
                patternResult=true;
                if(patternMatches^=validator.validator(text)){
                    continue;
                }
            } else if(!(result&=validator.validator(text))){
                //条件匹配对象
                break;
            }
        }
        //非空判断
        if(!result){
            if(TextUtils.isEmpty(text)&&!TextUtils.isEmpty(emptyError)){
                errorInfo=emptyError;
            } else {
                errorInfo=editError;
            }
        } else if(!patternMatches){
            errorInfo=editError;
        }
        //返回条件匹配与正则匹配结果
        return result&(patternMatches|!patternResult);
    }

    public EditText getEditor(){
        return editor;
    }

    public Editable getText(){
        return editor.getText();
    }

    public void setSearchDeleteDrawable(Drawable drawable) {
        if(null!=drawable){
            deleteView.setImageDrawable(drawable);
        } else {
            deleteView.setVisibility(View.GONE);
        }
    }

    public void setOnTextChangeListener(OnTextChangeListener listener){
        this.listener=listener;
    }

    public interface OnTextChangeListener{
        void onTextChanged(CharSequence newItem, CharSequence oldItem,int count);
    }


}
