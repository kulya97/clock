package com.kulya.clock.until;

import android.content.SharedPreferences;

import javax.xml.parsers.FactoryConfigurationError;

import androidx.preference.PreferenceManager;

/*
项目名称： clock
创建人：黄大神
类描述：
创建时间：2019/9/10 18:07
*/
public class settingInfo {
    private static settingInfo mSettingInfo;
    private SharedPreferences.Editor editor;
    private SharedPreferences pref;
    public static final String HideSetPage = "hideSetPage";
    public static final String Translate = "translate";
    public static final String Countdown = "countdown";
    public static final String Black = "black";
    public static final String StatusBar = "statusBar";
    public static final String Stop = "stop";
    public static final String BackgroundWidth = "backgroundWidth";
    public static final String BackgroundHeight = "backgroundHeight";
    public static final String TextSize = "textSize";

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


    private boolean getBoolean(String key) {
        switch (key) {
            case HideSetPage:
                return pref.getBoolean(HideSetPage, false);
            case Translate:
                return pref.getBoolean(Translate, false);
            case Countdown:
                return pref.getBoolean(Countdown, false);
            case Black:
                return pref.getBoolean(Black, false);
            case StatusBar:
                return pref.getBoolean(StatusBar, false);
            case Stop:
                return pref.getBoolean(Stop, false);
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
            default:
                return pref.getInt(key, 40);
        }
    }


    /***********************************************************
     *
     */

    public static int getIntInfo(String key) {
        return getInstance().getInt(key);
    }

    public static boolean getBooleanInfo(String key) {
        return getInstance().getBoolean(key);
    }

}
