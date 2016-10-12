package com.cz.app.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.View;

import com.cz.app.R;
import com.cz.app.widget.ColorLayout;
import com.cz.library.widget.RadioGridLayout;

/**
 * Created by cz on 16/3/8.
 */
public class ColorPicketDialog extends DialogFragment {
    private OnColorSelectListener listener;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final ColorLayout layout = new ColorLayout(getActivity());
        layout.setOnCheckedListener(new RadioGridLayout.OnSingleCheckListener() {
            @Override
            public void onChecked(View v, int newPosition, int oldPosition) {
                int color = layout.getColor(newPosition);
                if (null != listener) {
                    listener.onSelected(v, color);
                }
                dismiss();
            }
        });
        return new AlertDialog.Builder(getActivity()).setTitle(R.string.choice_color).setView(layout).create();
    }

    public void setOnColorSelectListener(OnColorSelectListener listener) {
        this.listener = listener;
    }

    public interface OnColorSelectListener {
        void onSelected(View v, int color);
    }
}
