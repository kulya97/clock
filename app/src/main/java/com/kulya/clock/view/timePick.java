package com.kulya.clock.view;


import android.content.Context;
import android.icu.util.Calendar;
import android.os.Build;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupWindow;

import com.kulya.clock.R;

/*
项目名称： clock
创建人：黄大神
类描述：倒计时时间选择窗口，有问题不能正常显示ui，已经找到问题所在，有空再改
创建时间：2019/10/17 15:59
*/
public class timePick extends PopupWindow {
    private Context mContext;
    private View contentView;
    private onClick listener;
    private MyNumberPick picker1;
    private MyNumberPick picker2;
    private MyNumberPick picker3;
    private Button countdown;
    private Button stopcountdown;

    public interface onClick {
        void onClick(int hour, int minute, int second);

        void onCancel();
    }

    public timePick(Context mContext, onClick listener) {
        this.mContext = mContext;
        this.listener=listener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.setWindowLayoutType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
        }
        contentView = LayoutInflater.from(mContext).inflate(R.layout.timepick, null);
        initPop();
        initView();
    }

    public void initView() {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        int second = calendar.get(Calendar.SECOND);
        picker1 = contentView.findViewById(R.id.numberpicker1);
        picker2 = contentView.findViewById(R.id.numberpicker2);
        picker3 = contentView.findViewById(R.id.numberpicker3);
        countdown = contentView.findViewById(R.id.countdown);
        stopcountdown = contentView.findViewById(R.id.stopcountdown);
        picker1.setMaxValue(23);
        picker1.setMinValue(0);
        picker1.setValue(hour);
        picker2.setMaxValue(59);
        picker2.setMinValue(0);
        picker2.setValue(minute);
        picker3.setMaxValue(59);
        picker3.setMinValue(0);
        picker3.setValue(second);
        countdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onClick(picker1.getValue(), picker2.getValue(), picker3.getValue());
                dismiss();
            }
        });
        stopcountdown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCancel();
                dismiss();
            }
        });
    }

    private void initPop() {
        this.setContentView(contentView);
        this.setWidth(WindowManager.LayoutParams.WRAP_CONTENT);
        this.setHeight(WindowManager.LayoutParams.WRAP_CONTENT);

        this.setFocusable(true);
        this.setOutsideTouchable(true);

        View rootview = LayoutInflater.from(mContext).inflate(R.layout.floatwindow, null);
        this.showAtLocation(rootview, Gravity.BOTTOM, 0, 800);



    }


}