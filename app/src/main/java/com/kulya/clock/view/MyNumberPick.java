package com.kulya.clock.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.NumberPicker;

import com.kulya.clock.R;

/*
项目名称： clock
创建人：黄大神
类描述：
创建时间：2019/9/28 10:12
*/
public class MyNumberPick extends NumberPicker {
    public MyNumberPick(Context context) {
        super(context);
    }

    public MyNumberPick(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyNumberPick(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    private void updateView(View view) {
        if (view instanceof EditText) {
            ((EditText) view).setTextColor(getResources().getColor(R.color.black));
            ((EditText) view).setTextSize(16);
        }
    }
}

