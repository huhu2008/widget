package com.cz.library.widget.editlayout;

import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.TransformationMethod;
import android.view.View;

/**
 * Created by cz on 11/3/16.
 */

public interface IEditText {

    void addTextChangedListener(TextWatcher watcher);

    void removeTextChangedListener(TextWatcher watcher);

    void setOnFocusChangeListener(View.OnFocusChangeListener listener);

    void setTextColor(int color);

    Editable getText();

    void setHint(CharSequence hint);

    void setText(CharSequence text);

    void setSelection(int selection);

    void setHintTextColor(int color);

    void setTextSize(int type,float textSize);

    void setInputType(int inputType);

    void setTransformationMethod(TransformationMethod method);

    void setFilters(InputFilter[] filters);

    void setMaxLines(int maxLines);

    void setPadding(int left,int top,int right,int bottom);
}
