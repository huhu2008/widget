package com.cz.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;

import com.cz.app.R;
import com.cz.app.widget.CheckLayout;
import com.cz.app.widget.RadioLayout;
import com.cz.app.widget.SeekLayout;
import com.cz.library.widget.RadioGridLayout;

import java.util.ArrayList;

/**
 * Created by cz on 16/3/7.
 */
public class RadioGridLayoutFragment extends Fragment {
    private static final String TAG = "RadioGridLayoutFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_radio_grid_layout, container, false);
    }

    @Override
    public void onViewCreated(final View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final RadioGridLayout layout = (RadioGridLayout) view.findViewById(R.id.cl_layout);
        RadioLayout modeLayout = (RadioLayout) view.findViewById(R.id.rl_choice_mode);
        SeekLayout widthSeek = (SeekLayout) view.findViewById(R.id.sl_width);
        SeekLayout heightSeek = (SeekLayout) view.findViewById(R.id.sl_height);
        SeekLayout horizontalPaddingSeek = (SeekLayout) view.findViewById(R.id.sl_horizontal_padding);
        SeekLayout verticalPaddingSeek = (SeekLayout) view.findViewById(R.id.sl_vertical_padding);
        CheckLayout gravityLayout = (CheckLayout) view.findViewById(R.id.cl_gravity);

        modeLayout.setOnCheckedListener(new RadioLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, int position, boolean isChecked) {
                layout.setCheckedMode(position);
            }
        });
        widthSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                layout.setButtonWidth(12 + progress);
            }
        });
        heightSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                layout.setButtonHeight(12 + progress);
            }
        });
        horizontalPaddingSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                layout.setButtonHorizontalPadding(progress);
            }
        });
        verticalPaddingSeek.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                layout.setButtonVerticalPadding(progress);
            }
        });
        final int[] gravities = {Gravity.LEFT, Gravity.TOP, Gravity.RIGHT, Gravity.BOTTOM};
        gravityLayout.setOnCheckedListener(new CheckLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, ArrayList<Integer> items, int position) {
                int gravity = 0;
                for (Integer index : items) {
                    gravity |= gravities[index];
                }
                layout.setImageGravity(gravity);
            }
        });
        String[] items={"A","B","C","D","E","F","G","H"};
        layout.addTextItems(items);
        view.findViewById(R.id.btn_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layout.addView(new View(getActivity()));
            }
        });
        view.findViewById(R.id.btn_remove).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int childCount = layout.getChildCount();
                if (0 < childCount) {
                    layout.removeViewAt(childCount - 1);
                }
            }
        });

    }
}
