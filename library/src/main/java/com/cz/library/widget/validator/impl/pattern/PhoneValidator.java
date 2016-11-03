package com.cz.library.widget.validator.impl.pattern;

import android.util.Patterns;

import com.cz.library.widget.validator.Validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by czz on 2016/9/23.
 */
public class PhoneValidator extends PatternValidator {
    private final Pattern pattern;
    public PhoneValidator() {
        pattern=Pattern.compile("[1][3,4,5,7,8][0-9]{9}");
    }
    @Override
    public Pattern getPattern() {
        return pattern;
    }
}
