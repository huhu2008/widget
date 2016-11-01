package com.cz.app;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;

import com.cz.app.adapter.SimpleAdapter;
import com.ldzs.recyclerlibrary.PullToRefreshRecyclerView;
import com.ldzs.recyclerlibrary.anim.SlideInLeftAnimator;
import com.ldzs.recyclerlibrary.callback.OnItemClickListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private PullToRefreshRecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = (PullToRefreshRecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(new SlideInLeftAnimator());
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        final ArrayList<Item> items = new ArrayList<>();
        items.add(new Item("BadgerTextViewFragment", "com.cz.app.ui.BadgerTextViewFragment", ""));
        items.add(new Item("BorderLabelTextView", "com.cz.app.ui.BorderLabelTextViewFragment", ""));
        items.add(new Item("CenterTextView", "com.cz.app.ui.CenterTextViewFragment", ""));
        items.add(new Item("DivideLinearLayout", "com.cz.app.ui.DivideLinedarLayoutFragment", ""));
        items.add(new Item("FrameView", "com.cz.app.ui.FrameViewFragment", ""));
        items.add(new Item("ListFragment", "com.cz.app.ui.ListFragment", ""));
        items.add(new Item("IndicateView", "com.cz.app.ui.IndicateViewFragment", ""));
        items.add(new Item("ListIndicator", "com.cz.app.ui.ListIndicateFragment", ""));
        items.add(new Item("CenterGridLayout", "com.cz.app.ui.CenterGridLayoutFragment", ""));
        items.add(new Item("RadioGridLayout", "com.cz.app.ui.RadioGridLayoutFragment", ""));
        items.add(new Item("ShapeDrawable", "com.cz.app.ui.ShapeDrawableFragment", ""));
        items.add(new Item("OptionGroup", "com.cz.app.ui.OptionGroupFragment", ""));
        items.add(new Item("TabLinearLayoutFragment", "com.cz.app.ui.TabLinearLayoutFragment", ""));
        items.add(new Item("WaveImageView", "com.cz.app.ui.WaveImageViewFragment", ""));
        items.add(new Item("TimeDownView", "com.cz.app.ui.TimeViewFragment", ""));
        items.add(new Item("BothTextView", "com.cz.app.ui.BothTextViewFragment", ""));
        items.add(new Item("EditLayout", "com.cz.app.ui.EditLayoutFragment", ""));
        items.add(new Item("DialView", "com.cz.app.ui.DialViewFragment", ""));
        items.add(new Item("ShadowView", "com.cz.app.ui.ShadowFragment", ""));
        items.add(new Item("LoadView", "com.cz.app.ui.LoadFragment", ""));
        recyclerView.setAdapter(new SimpleAdapter(this, items));
        recyclerView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position) {
                Item item = items.get(position);
                try {
                    Class<? extends Fragment> clazz = (Class<? extends Fragment>) Class.forName(item.className);
                    Bundle args = new Bundle();
                    args.putString("title", item.name);
                    ShellActivity.toActivity(MainActivity.this, clazz, args);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

    }
}
