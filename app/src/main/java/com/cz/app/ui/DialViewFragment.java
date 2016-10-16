package com.cz.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.cz.app.R;
import com.cz.app.widget.GradientRelativeLayout;
import com.cz.library.widget.DialView;

import java.util.Random;

/**
 * Created by czz on 2016/9/26.
 */
public class DialViewFragment extends Fragment {
    public static final String TAG = "DialViewFragment";
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_dial_view,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        final DialView dialView= (DialView) view.findViewById(R.id.dv_view);
        final TextView textView= (TextView) view.findViewById(R.id.tv_text);
        final GradientRelativeLayout container= (GradientRelativeLayout) view.findViewById(R.id.container);
        container.setAnimColors(new int[]{0xFFFF4B00,0xFFFF7D00},new int[]{0xFFFFA700,0xFFFFC400},new int[]{0xFF74C31C,0xFF9ADC11},new int[]{0xFF5298FF,0xFF7D96FE},new int[]{0xFF5298FF,0xFF7D96FE});
        dialView.setOnLevelValueChangeListener(new DialView.OnLevelValueChangeListener() {
            @Override
            public void onLevelValueChanged(int startValue, int currentValue, int endValue, int level, float fraction) {
                Log.e(TAG,"level:"+level+" fraction:"+fraction);
                //500 0 0.0->1.0f
                container.setAnimFraction(level,fraction);
            }
        });
        view.findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Random random=new Random();
                int minLevelValue = dialView.getMinLevelValue();
                int maxLevelValue = dialView.getMaxLevelValue();
                int currentLevelValue = dialView.getCurrentLevelValue();
                int value = minLevelValue + random.nextInt(maxLevelValue - minLevelValue);
                textView.append("start:"+currentLevelValue+" to:"+value+"\n");
                dialView.setLevelValueTo(value);
            }
        });
    }
}
