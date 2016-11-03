package com.cz.library.widget.editlayout;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by cz on 11/3/16.
 */

public class MyEditText extends EditText implements IEditText {

    public MyEditText(Context context) {
        super(context);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


}
