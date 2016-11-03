package com.cz.app.widget;

import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.util.AttributeSet;
import android.widget.EditText;

import com.cz.app.R;
import com.cz.library.widget.editlayout.IEditText;

/**
 * Created by czz on 2016/11/3.
 */

public class MyTextInputLayout extends TextInputLayout implements IEditText {
    private EditText editor;
    public MyTextInputLayout(Context context) {
        this(context,null,0);
    }

    public MyTextInputLayout(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyTextInputLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflate(context, com.cz.library.R.layout.edit_view,this);
        editor= (EditText) findViewById(R.id.et_editor);
    }

    @Override
    public void addTextChangedListener(TextWatcher watcher) {
        editor.addTextChangedListener(watcher);
    }

    @Override
    public void removeTextChangedListener(TextWatcher watcher) {
        editor.removeTextChangedListener(watcher);
    }

    @Override
    public void setTextColor(int color) {
        editor.setTextColor(color);
    }

    @Override
    public Editable getText() {
        return editor.getText();
    }

    @Override
    public void setText(CharSequence text) {
        editor.setText(text);
    }

    @Override
    public void setSelection(int selection) {
        editor.setSelection(selection);
    }

    @Override
    public void setHintTextColor(int color) {
        editor.setHintTextColor(color);
    }

    @Override
    public void setTextSize(int type, float textSize) {
        editor.setTextSize(type,textSize);
    }

    @Override
    public void setInputType(int inputType) {
        editor.setInputType(inputType);
    }

    @Override
    public void setTransformationMethod(TransformationMethod method) {
        editor.setTransformationMethod(method);
    }

    @Override
    public void setFilters(InputFilter[] filters) {
        editor.setFilters(filters);
    }

    @Override
    public void setMaxLines(int maxLines) {
        editor.setMaxLines(maxLines);
    }
}
