package com.cz.library.util;

import android.app.Application;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.support.annotation.ArrayRes;
import android.support.annotation.AttrRes;
import android.support.annotation.ColorRes;
import android.support.annotation.StringRes;
import android.util.TypedValue;

import java.lang.reflect.Method;


/**
 * Utils
 */
public class Utils {

    private static Context appContext;

    static {
        appContext = getContext();
    }

    public static Context getContext() {
        if (appContext == null) {
            try {
                final Class<?> activityThreadClass = Utils.class
                        .getClassLoader().loadClass(
                                "android.app.ActivityThread");
                final Method currentActivityThread = activityThreadClass
                        .getDeclaredMethod("currentActivityThread");
                final Object activityThread = currentActivityThread
                        .invoke(null);
                final Method getApplication = activityThreadClass
                        .getDeclaredMethod("getApplication");
                final Application application = (Application) getApplication
                        .invoke(activityThread);
                appContext = application.getApplicationContext();
            } catch (final Exception e) {
                e.printStackTrace();
            }
        }
        return appContext;
    }

    public static Resources getResources(){
        return getContext().getResources();
    }

    public static int getColor(@ColorRes int res) {
        Context context = getContext();
        return context.getResources().getColor(res);
    }

    public static String getString(@StringRes int res, Object value) {
        Context context = getContext();
        return context.getString(res, value);
    }


    public static int dip2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, appContext.getResources().getDisplayMetrics());
    }


    public static int px2dip(float value) {
        final float scale = appContext.getResources().getDisplayMetrics().density;
        return (int) (value / scale + 0.5f);
    }

    public static int sp2px(float value) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, value, appContext.getResources().getDisplayMetrics());
    }

    public static float px2sp(float px) {
        float scaledDensity = appContext.getResources().getDisplayMetrics().scaledDensity;
        return px / scaledDensity;
    }

    public static float getDimensionPixelSize(@AttrRes int attr) {
        int[] attribute = new int[]{attr};
        TypedArray array = appContext.obtainStyledAttributes(null, attribute);
        int value = array.getDimensionPixelSize(0, 0);
        array.recycle();
        return value;
    }

    public static int getResourceId(@AttrRes int attr) {
        int[] attribute = new int[]{attr};
        TypedArray array = appContext.obtainStyledAttributes(null, attribute);
        int res = array.getResourceId(0, 0);
        array.recycle();
        return res;
    }


    public static String[] getStringArray(@ArrayRes int id) {
        return appContext.getResources().getStringArray(id);
    }

    public static int[] getIntArray(@ArrayRes int id) {
        return appContext.getResources().getIntArray(id);
    }
}
