package com.kulya.clock.until;

import android.content.SharedPreferences;
import android.graphics.Color;

import androidx.preference.PreferenceManager;

/*
项目名称： clock
创建人：黄大神
类描述：读取sharepreferences中的数据
创建时间：2019/9/10 18:07
*/
public class settingInfo {
    //前面为在本类的标识，后面为在sharepreference中的标识
    public static final String HideSetPage = "HideSetPage";
    public static final String Countdown = "Countdown";
    public static final String StatusBar = "StatusBar";
    public static final String Fixed = "Fixed";//固定位置
    public static final String textColor = "textColor";
    public static final String bgColor = "bgColor";
    public static final String BackgroundWidth = "BackgroundWidth";
    public static final String BackgroundHeight = "BackgroundHeight";
    public static final String TextSize = "textSize";
    public static final String showMS = "showMS";
    public static final String endX = "endX";
    public static final String endY = "endY";

    private static settingInfo mSettingInfo;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;

    private settingInfo() {
        pref = PreferenceManager.getDefaultSharedPreferences(Myapplication.getContext());
        editor = pref.edit();
    }

    private static settingInfo getInstance() {
        if (mSettingInfo == null) {
            synchronized (settingInfo.class) {
                if (mSettingInfo == null) {
                    mSettingInfo = new settingInfo();
                }
            }
        }
        return mSettingInfo;
    }

    private void setPosition(int x, int y) {
        editor.putInt(endX, x);
        editor.putInt(endY, y);
        editor.commit();
    }

    private boolean getBoolean(String key) {
        switch (key) {
            case HideSetPage:
                return pref.getBoolean(HideSetPage, false);
            case Countdown:
                return pref.getBoolean(Countdown, false);
            case StatusBar:
                return pref.getBoolean(StatusBar, false);
            case Fixed:
                return pref.getBoolean(Fixed, false);
            case showMS:
                return pref.getBoolean(showMS, false);
            default:
                return pref.getBoolean(key, false);
        }
    }

    private int getInt(String key) {
        switch (key) {
            case TextSize:
                return pref.getInt(TextSize, 14);
            case BackgroundWidth:
                return pref.getInt(BackgroundWidth, 90);
            case BackgroundHeight:
                return pref.getInt(BackgroundHeight, 30);
            case textColor:
                return pref.getInt(textColor, Color.BLACK);
            case bgColor:
                return pref.getInt(bgColor, Color.WHITE);
            case endX:
                return pref.getInt(endX, 0);
            case endY:
                return pref.getInt(endY, 0);
            default:
                return pref.getInt(key, 40);
        }
    }

    /************************外部方法************************************/
    public static void setPositionInfo(int x, int y) {
        getInstance().setPosition(x, y);
    }

    public static int getIntInfo(String key) {
        return getInstance().getInt(key);
    }

    public static boolean getBooleanInfo(String key) {
        return getInstance().getBoolean(key);
    }

}
