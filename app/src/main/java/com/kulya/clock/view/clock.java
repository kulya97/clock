package com.kulya.clock.view;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.service.quicksettings.Tile;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.kulya.clock.service.setservice;
import com.kulya.clock.service.timeService;
import com.kulya.clock.until.Myapplication;
import com.kulya.clock.R;
import com.kulya.clock.until.settingInfo;
import com.kulya.clock.until.ScreenTools;

import java.net.URL;
import java.net.URLConnection;


/*
项目名称： clock
创建人：黄大神
类描述：
创建时间：2019/9/10 18:58
*/
public class clock {
    private static clock mClock;
    private ItemOnClickInterface itemOnClickInterface;

    private floatButton imageButton;
    private LinearLayout toucherLayout;
    private WindowManager.LayoutParams params;
    private WindowManager windowManager;

    private boolean stopMove = settingInfo.getBooleanInfo(settingInfo.Fixed);

    private boolean isMove;

    private int statusBarHeight = 0;
    private int x;
    private int y;
    private int mTouchStartX = 0;
    private int mTouchStartY = 0;
    private int mStartX = 0;
    private int mStartY = 0;

    public interface ItemOnClickInterface {
        void onItemClick(View view);
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
        imageButton = toucherLayout.findViewById(R.id.imageButton);

        imageButton.setOnClickListener(new itemOnclick());
        imageButton.setOnTouchListener(new OnTouchClick());
        imageButton.setOnLongClickListener(new itemLongOnclick());
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
                monDismiss();
            }
            return false;
        }
    }

    class itemOnclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {

            switch (v.getId()) {
                case R.id.imageButton:
                    itemOnClickInterface.onItemClick(imageButton);
                    break;
                default:
                    break;
            }

        }
    }

    /**
     * 内部方法
     */

    class mTime {
        public int hour;
        public int minute;
        public int second;
        public int millisecond;
    }

    private mTime getNetTime(String url2) {
        String webUrl = "cn.pool.ntp.org";//中国科学院国家授时中心
        mTime time = new mTime();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        URL url = new URL(webUrl);
                        URLConnection uc = url.openConnection();
                        uc.connect();
                        long correctTime = uc.getDate();
                        int h = (int) (correctTime / 1000 / 3600);
                        int m = (int) (correctTime / 1000 / 60 % 60);
                        int s = (int) (correctTime / 1000 % 60);
                        int ms = (int) (correctTime % 1000);
                        Log.d("1223", h + ":" + m + ":" + s + ":" + ms);
//                        Calendar calendar = Calendar.getInstance();
//                        calendar.setTimeInMillis(correctTime);
//                        time.minute = calendar.get(Calendar.MINUTE);
//                        time.second = calendar.get(Calendar.SECOND);
//                        time.hour = calendar.get(Calendar.HOUR_OF_DAY);
//                        time.millisecond = calendar.get(Calendar.MILLISECOND);
                    } catch (Exception e) {
                        Log.d("1223", "err");
                    }
                }
            }
        }).start();

        return time;
    }

    /*刷新窗口*/
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

    /*初始化窗口数据*/
    private void initInfo() {
        params.x = settingInfo.getIntInfo(settingInfo.endX);
        params.y = settingInfo.getIntInfo(settingInfo.endY);
        updateWindows(settingInfo.getBooleanInfo(settingInfo.StatusBar));
        setTextColor(settingInfo.getIntInfo(settingInfo.textColor));
        setBgColor(settingInfo.getIntInfo(settingInfo.bgColor));
        setTextSize(settingInfo.getIntInfo(settingInfo.TextSize));
        setBackgroundHeight(settingInfo.getIntInfo(settingInfo.BackgroundHeight));
        setBackgroundWidth(settingInfo.getIntInfo(settingInfo.BackgroundWidth));
    }

    /*设置字体大小*/
    private void setTextSize(int textSize) {
        imageButton.setTextSize(textSize);
    }

    /*设置字体颜色，暂未使用*/
    private void setTextColor(int color) {
        imageButton.setTextColor(color);

    }

    /*设置背景颜色，暂未使用*/
    private void setBgColor(int color) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            imageButton.setBackgroundColor(color);
        }
    }

    /*禁止移动*/
    private void stop(boolean info) {
        if (info) {
            stopMove = true;
            Log.d("2222", "stop: ");
        } else {
            stopMove = false;
            Log.d("2222", "stop:2 ");
        }
    }

    /*设置悬浮窗宽度*/
    private void setBackgroundWidth(int width) {
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) imageButton.getLayoutParams();
        linearParams.width = ScreenTools.dip2px(width);
        imageButton.setLayoutParams(linearParams);
        windowManager.updateViewLayout(toucherLayout, params);
    }

    /*设置悬浮窗高度*/
    private void setBackgroundHeight(int height) {
        LinearLayout.LayoutParams linearParams = (LinearLayout.LayoutParams) imageButton.getLayoutParams();
        linearParams.height = ScreenTools.dip2px(height);
        imageButton.setLayoutParams(linearParams);
        windowManager.updateViewLayout(toucherLayout, params);
    }

    /*关闭悬浮窗*/
    private void monDismiss() {
        settingInfo.setPositionInfo(params.x, params.y);
        Intent stop = new Intent(Myapplication.getContext(), timeService.class);
        Myapplication.getContext().stopService(stop);
        windowManager.removeView(toucherLayout);
        System.exit(0);

    }

    /**
     * 外部方法
     */
    /*关闭悬浮窗*/
    public static void onDismiss() {
        getInstance().monDismiss();
    }

    /*初始化*/
    public static void init() {
        getInstance();
    }

    /*设置点击事件*/
    public static void setItemOnClick(ItemOnClickInterface itemOnClickInterface) {
        getInstance().itemOnClickInterface = itemOnClickInterface;
    }

    /*设置文字*/
    public static void setText(String str) {
        getInstance().imageButton.setText(str);
    }

    /*设置窗口数据*/
    public static void setWindowInfo(String key, Object info) {
        switch (key) {
            case settingInfo.HideSetPage:
                break;
            case settingInfo.Countdown:
                break;
            case settingInfo.StatusBar:
                getInstance().updateWindows((Boolean) info);
                break;
            case settingInfo.Fixed:
                getInstance().stop((Boolean) info);
                break;
            case settingInfo.textColor:
                getInstance().setTextColor((Integer) info);
                break;
            case settingInfo.bgColor:
                getInstance().setBgColor((Integer) info);
                break;
            case settingInfo.TextSize:
                getInstance().setTextSize((Integer) info);
                break;
            case settingInfo.BackgroundWidth:
                getInstance().setBackgroundWidth((Integer) info);
                break;
            case settingInfo.BackgroundHeight:
                getInstance().setBackgroundHeight((Integer) info);
                break;
            default:
                break;
        }
    }

}
