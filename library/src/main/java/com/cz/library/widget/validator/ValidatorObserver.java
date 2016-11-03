package com.cz.library.widget.validator;

import android.text.Editable;
import android.text.TextWatcher;

import com.cz.library.widget.editlayout.EditLayout;
import com.cz.library.widget.editlayout.IEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


/**
 * Created by czz on 2016/9/23.
 * 匹配信息检测观察者对象
 */
public class ValidatorObserver<V extends IEditText> {
    private final List<EditLayout> layouts;
    private final HashMap<EditLayout<V>,ValidatorTextWatcher> itemTextWatchers;
    private String errorMessage;
    private ValidatorAction validatorAction;

    private ValidatorObserver(EditLayout<V>[] layouts) {
        this.layouts = new ArrayList<>();
        this.itemTextWatchers=new HashMap<>();
        if(null!=layouts){
            this.layouts.addAll(Arrays.asList(layouts));
            for(int i=0;i<layouts.length;i++){
                EditLayout<V> layout = layouts[i];
                V editor = layout.getEditor();
                ValidatorTextWatcher textWatcher = new ValidatorTextWatcher(layout);
                itemTextWatchers.put(layout,textWatcher);
                editor.addTextChangedListener(textWatcher);
            }
        }
    }
    public static ValidatorObserver create(EditLayout... layouts){
        return new ValidatorObserver(layouts);
    }

    public ValidatorObserver subscribe(ValidatorAction action){
        this.validatorAction=action;
        return this;
    }

    public void addEditLayout(EditLayout<V> layout){
        if(null!=layout){
            V editor = layout.getEditor();
            ValidatorTextWatcher textWatcher = new ValidatorTextWatcher(layout);
            editor.addTextChangedListener(textWatcher);
            itemTextWatchers.put(layout, textWatcher);
            layouts.add(layout);
        }
    }

    public void removeEditLayout(EditLayout<V> layout){
        if(null!=layout){
            layouts.remove(layout);
            ValidatorTextWatcher textWatcher = itemTextWatchers.remove(layout);
            if(null!=textWatcher){
                V editor = layout.getEditor();
                editor.removeTextChangedListener(textWatcher);
            }
        }
    }

    /**
     * 判断所有布局条件是否匹配
     * @return
     */
    public boolean isValid(){
        boolean result=true;
        for(int i=0;i<layouts.size();i++){
            EditLayout layout = layouts.get(i);
            if(!(result&=layout.isValid())){
                errorMessage=layout.getEditError();
                break;
            }
        }
        return result;
    }

    /**
     * 获得检测失败异常信息
     * @return
     */
    public String getErrorMessage(){
        return errorMessage;
    }

    public interface ValidatorAction{
        void onChanged(EditLayout editText,boolean changed);
    }

    class ValidatorTextWatcher implements TextWatcher {
        private boolean isValid;
        private final EditLayout<V> layout;

        public ValidatorTextWatcher(EditLayout<V> layout) {
            this.layout = layout;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            if(isValid^layout.isValid()){
                isValid=!isValid;
                if(null!=validatorAction){
                    validatorAction.onChanged(layout,isValid);
                }
            }
        }

        @Override
        public void afterTextChanged(Editable editable) {
        }
    }
}
