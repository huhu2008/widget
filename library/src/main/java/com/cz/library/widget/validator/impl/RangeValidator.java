package com.cz.library.widget.validator.impl;

import android.text.Editable;
import android.text.TextUtils;

import com.cz.library.widget.validator.Validator;

/**
 * Created by czz on 2016/9/23.
 */
public class RangeValidator implements Validator{
    private final int min;
    private final int max;

    public RangeValidator(int min, int max) {
        this.min = min;
        this.max = max;
    }

    @Override
    public boolean validator(Editable value) {
        int length= TextUtils.isEmpty(value)?0:value.length();
        return min<=length&&length<max;
    }
}
