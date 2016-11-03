package com.cz.app.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.cz.app.R;
import com.cz.library.widget.editlayout.EditLayout;
import com.cz.library.widget.validator.ValidatorObserver;

/**
 * Created by czz on 2016/9/21.
 */
public class EditLayoutFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_edit_layout,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditLayout layout1= (EditLayout) view.findViewById(R.id.sv_edit_view1);
        EditLayout layout2= (EditLayout) view.findViewById(R.id.sv_edit_view2);
        EditLayout layout3= (EditLayout) view.findViewById(R.id.sv_edit_view3);
        final TextView textView= (TextView) view.findViewById(R.id.tv_info);

        final ValidatorObserver subscribe = ValidatorObserver.create(layout1, layout2, layout3).subscribe(new ValidatorObserver.ValidatorAction() {
            @Override
            public void onChanged(EditText editText, boolean changed) {
                textView.append("ValidatorObserver:"+changed+"\n");
            }
        });

        view.findViewById(R.id.btn_clear).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                textView.setText(null);
            }
        });

        view.findViewById(R.id.btn_check).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!subscribe.isValid()){
                    textView.append(subscribe.getErrorMessage()+"\n");
                } else {
                    textView.append("Checked Success!\n");
                }
            }
        });




    }
}
