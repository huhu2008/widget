package com.cz.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.Toast;

import com.cz.app.R;
import com.cz.app.widget.RadioLayout;
import com.cz.app.widget.SeekLayout;
import com.cz.library.widget.card.CardLinearLayout;
import com.cz.library.widget.card.CardRelativeLayout;
import com.cz.library.widget.card.CardTextView;

/**
 * Created by Administrator on 2016/9/27.
 */
public class ShadowFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_shadow,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final CardTextView cardTextView= (CardTextView) view.findViewById(R.id.cv_text_view);
        final CardLinearLayout cardLinearView= (CardLinearLayout) view.findViewById(R.id.cv_linear_view);
        final CardRelativeLayout cardRelativeView= (CardRelativeLayout) view.findViewById(R.id.cv_relative_view);
        cardTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click!", Toast.LENGTH_SHORT).show();
            }
        });
        cardLinearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click!", Toast.LENGTH_SHORT).show();
            }
        });
        cardRelativeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Click!", Toast.LENGTH_SHORT).show();
            }
        });
        RadioLayout radioLayout= (RadioLayout) view.findViewById(R.id.shadow_layout);
        radioLayout.setOnCheckedListener(new RadioLayout.OnCheckedListener() {
            @Override
            public void onChecked(View v, int position, boolean isChecked) {
                cardTextView.setCardType(position);
                cardLinearView.setCardType(position);
                cardRelativeView.setCardType(position);
            }
        });

        SeekLayout radiusLayout= (SeekLayout) view.findViewById(R.id.sl_radius);
        radiusLayout.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                cardTextView.setCornerRadius(progress);
                cardLinearView.setCornerRadius(progress);
                cardRelativeView.setCornerRadius(progress);
            }
        });

        SeekLayout elevationLayout= (SeekLayout) view.findViewById(R.id.sl_elevation);
        elevationLayout.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                cardTextView.setCardElevation(progress);
                cardLinearView.setCardElevation(progress);
                cardRelativeView.setCardElevation(progress);
            }
        });
        SeekLayout paddingLayout= (SeekLayout) view.findViewById(R.id.sl_padding);
        paddingLayout.setOnSeekProgressChangeListener(new SeekLayout.OnSeekProgressChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress) {
                cardTextView.setContentPadding(progress);
                cardLinearView.setContentPadding(progress);
                cardRelativeView.setContentPadding(progress);
            }
        });
    }
}
