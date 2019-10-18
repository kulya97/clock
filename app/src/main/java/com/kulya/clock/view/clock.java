package com.kulya.clock.view;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.kulya.clock.activity.MainActivity;
import com.kulya.clock.service.MyService;
import com.kulya.clock.until.Myapplication;
import com.kulya.clock.R;
import com.kulya.clock.until.settingInfo;

import java.util.Calendar;
import java.util.Timer;

/*
项目名称： clock
创建人：黄大神
类描述：
创建时间：2019/9/10 18:58
*/
public class clock {
    private static clock mClock;
    private timeHandler timeHandler;
    private Button imageButton1;
    private LinearLayout toucherLayout;
    private WindowManager.LayoutParams params;
    private WindowManager windowManager;


    private int Countdown;
    private int minute;
    private int second;
    private int hour;
    private int minute0;
    private int second0;
    private int hour0;

    private boolean isCountdownTask = false;
    private boolean isMove;
    private boolean stopMove = settingInfo.getBooleanInfo(settingInfo.Stop);

    private int statusBarHeight = 0;
    private int x;
    private int y;
    private int mTouchStartX = 0;
    private int mTouchStartY = 0;
    private int mStartX = 0;
    private int mStartY = 0;


    class TimeThread extends Thread {
        @Override
        public void run() {
            while (true) {

                Message msg = new Message();
                if (!isCountdownTask) msg.what = 1;
                else msg.what = 2;
                timeHandler.sendMessage(msg);
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    class timeHandler extends Handler {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Calendar calendar = Calendar.getInstance();
            switch (msg.what) {
                case 1:
                    minute = calendar.get(Calendar.MINUTE);
                    second = calendar.get(Calendar.SECOND);
                    hour = calendar.get(Calendar.HOUR_OF_DAY);
                    try {
                        imageButton1.setText(String.format("%02d", hour) + ":"
                                + String.format("%02d", minute) + ":" + String.format("%02d", second)); //更新时
                    } catch (Exception e) {
                    }
                    break;
                case 2:
                    Calendar calendar2 = Calendar.getInstance();
                    hour = calendar2.get(Calendar.HOUR_OF_DAY);
                    minute = calendar2.get(Calendar.MINUTE);
                    second = calendar2.get(Calendar.SECOND);
                    int time = hour * 3600 + minute * 60 + second;
                    Countdown = hour0 * 3600 + minute0 * 60 + second0;
                    if (time > Countdown)
                        Countdown += 60 * 60 * 24;
                    hour = (Countdown - time) / 3600;
                    minute = (Countdown - time) / 60 % 60;
                    second = (Countdown - time) % 60;
                    try {
                        imageButton1.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second)); //更新时
                    } catch (Exception e) {
                    }

                default:
                    break;
            }
        }
    }

    private static clock getInstance() {
        if (mClock == null) {
            synchronized (clock.class) {
                if (mClock == null) {
                    mClock = new clock();
                }
            }
        }
        return mClock;
    }

    private clock() {
        timeHandler = new timeHandler();
        new TimeThread().start();
        createView();
        initInfo();
    }

    private void createView() {
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) Myapplication.getContext().getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        else params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        params.format = PixelFormat.RGBA_8888;
        if (settingInfo.getBooleanInfo(settingInfo.StatusBar)) {
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        } else {
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }
        params.gravity = Gravity.LEFT | Gravity.TOP;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        toucherLayout = (LinearLayout) LayoutInflater.from(Myapplication.getContext()).inflate(R.layout.floatwindow, null);
        windowManager.addView(toucherLayout, params);
        initView();
    }

    private void initView() {
        imageButton1 = toucherLayout.findViewById(R.id.imageButton1);
        imageButton1.setOnClickListener(new itemOnclick());
        imageButton1.setOnTouchListener(new OnTouchClick());
        imageButton1.setOnLongClickListener(new itemLongOnclick());
    }

    class OnTouchClick implements View.OnTouchListener {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    mStartX = params.x;
                    mStartY = params.y;
                    mTouchStartX = (int) event.getX();
                    mTouchStartY = (int) event.getY();
                    x = (int) event.getRawX();
                    y = (int) event.getRawY();
                    statusBarHeight = y - mTouchStartY - mStartY;
                    isMove = false;
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (!stopMove) {
                        x = (int) event.getRawX();
                        y = (int) event.getRawY();
                        params.x = (int) (x - mTouchStartX);
                        params.y = (int) (y - mTouchStartY - statusBarHeight);
                        windowManager.updateViewLayout(toucherLayout, params);  //刷新显示
                        if (Math.abs(mStartX - params.x) < 2 && Math.abs(mStartY - params.y) < 2)
                            isMove = false;
                        else
                            isMove = true;
                    } else
                        isMove = false;
                    break;
                case MotionEvent.ACTION_UP:
                    isMove = false;
                    break;
                default:
                    break;
            }
            return false;
        }
    }

    class itemLongOnclick implements View.OnLongClickListener {

        @Override
        public boolean onLongClick(View v) {
            if (!isMove) {
                Log.d("ffd", "onLongClick: ");
                Intent stop = new Intent(Myapplication.getContext(), MyService.class);
                Myapplication.getContext().stopService(stop);
                System.exit(0);
            }
            return false;
        }
    }

    class itemOnclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.imageButton1:
                    if (!settingInfo.getBooleanInfo(settingInfo.Countdown))
                        new timePick(Myapplication.getContext(), new timePick.onClick() {
                            @Override
                            public void onClick(int hour, int minute, int second) {
                                isCountdownTask = true;
                                hour0 = hour;
                                minute0 = minute;
                                second0 = second;
                            }

                            @Override
                            public void onCancel() {
                                isCountdownTask = false;
                            }
                        });
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * 内部方法
     */
    private int dip2px(int dpValue) {
        final float scale = Myapplication.getContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    private void updateWindows(boolean isStatusBar) {
        if (isStatusBar) {
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                    | WindowManager.LayoutParams.FLAG_LAYOUT_INSET_DECOR
                    | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        } else {
            params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        }
        params.gravity = Gravity.LEFT | Gravity.TOP;
        windowManager.updateViewLayout(toucherLayout, params);

    }

    private void initInfo() {
        updateWindows(settingInfo.getBooleanInfo(settingInfo.StatusBar));
        setBackgroundTranslate(settingInfo.getBooleanInfo(settingInfo.Translate));
        setBlack(settingInfo.getBooleanInfo(settingInfo.Black));
        setTextSize(settingInfo.getIntInfo(settingInfo.TextSize));
        setBackgroundHeight(settingInfo.getIntInfo(settingInfo.BackgroundHeight));
        setBackgroundWidth(settingInfo.getIntInfo(settingInfo.BackgroundWidth));
    }

    private void setTextSize(int textSize) {
        imageButton1.setTextSize(textSize);
    }

    private void setBlack(Boolean isBlack) {
        if (isBlack) {
            imageButton1.setTextColor(0xffffffff);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imageButton1.setBackground(Myapplication.getContext().getResources().getDrawable(R.drawable.shap2));

            }
        } else {
            imageButton1.setTextColor(0xff000000);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imageButton1.setBackground(Myapplication.getContext().getResources().getDrawable(R.drawable.shap));
            }
        }
    }

    private void setBlack2(int color) {
        imageButton1.setTextColor(0xffffffff);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageButton1.setBackgroundColor(color);
        }
    }

    private void stop(boolean info) {
        if (info) {
            stopMove = true;
            Log.d("2222", "stop: ");
        } else {
            stopMove = false;
            Log.d("2222", "stop:2 ");
        }
    }

    private void setBackgroundTranslate(Boolean isBackground) {
        if (isBackground) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imageButton1.setBackground(Myapplication.getContext().getResources().getDrawable(R.drawable.touming));
            }

        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                imageButton1.setBackground(Myapplication.getContext().getResources().getDrawable(R.drawable.shap));
            }
        }

    }

    private void setBackgroundWidth(int width) {
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) imageButton1.getLayoutParams();
        linearParams.width = dip2px(width);
        imageButton1.setLayoutParams(linearParams);
        windowManager.updateViewLayout(toucherLayout, params);
    }

    private void setBackgroundHeight(int height) {
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) imageButton1.getLayoutParams();
        linearParams.height = dip2px(height);
        imageButton1.setLayoutParams(linearParams);
        windowManager.updateViewLayout(toucherLayout, params);
    }

    private void monDismiss() {
        if (imageButton1 != null) {
            mClock = null;
            windowManager.removeView(toucherLayout);
        }
    }

    /**
     * 外部方法
     */
    public static void onDismiss() {
        getInstance().monDismiss();
    }

    public static void init() {
        getInstance();
    }

    public static void setWindowInfo(String key, Object info) {
        switch (key) {
            case "hideSetPage":
                break;
            case "translate":
                getInstance().setBackgroundTranslate((Boolean) info);
                break;
            case "countdown":
                break;
            case "black":
                getInstance().setBlack((Boolean) info);
                break;
            case "statusBar":
                getInstance().updateWindows((Boolean) info);
                break;
            case "stop":
                getInstance().stop((Boolean) info);
                break;
            case "textSize":
                getInstance().setTextSize((Integer) info);
                break;
            case "backgroundWidth":
                getInstance().setBackgroundWidth((Integer) info);
                break;
            case "backgroundHeight":
                getInstance().setBackgroundHeight((Integer) info);
                break;
            default:

                break;
        }
    }

}
