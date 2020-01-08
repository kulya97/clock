package com.kulya.clock.activity;


import android.os.Bundle;

import com.jaredrummler.android.colorpicker.ColorPickerDialogListener;
import com.jaredrummler.android.colorpicker.ColorPreferenceCompat;
import com.kulya.clock.R;
import com.kulya.clock.service.timeService;
import com.kulya.clock.until.settingInfo;
import com.kulya.clock.view.clock;

import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.SeekBarPreference;

/*
项目名称： clock
创建人：黄大神
类描述：
创建时间：2019/10/16 17:21
*/
public class settingFragment extends PreferenceFragment {
    private androidx.preference.SwitchPreference isHide;
    private androidx.preference.SwitchPreference isCountdown;
    private androidx.preference.SwitchPreference statusBar;
    private androidx.preference.SwitchPreference Fixed;
    private androidx.preference.SwitchPreference showMS;
    private SeekBarPreference backgroundWidth;
    private SeekBarPreference backgroundHeight;
    private SeekBarPreference textSize;
    private ColorPreferenceCompat textColor;
    private ColorPreferenceCompat bgColor;



    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        setPreferencesFromResource(R.xml.perference, rootKey);
        initView();
    }

    private void initView() {
        isHide = (androidx.preference.SwitchPreference) findPreference(settingInfo.HideSetPage);
        isCountdown = (androidx.preference.SwitchPreference) findPreference(settingInfo.Countdown);
        statusBar = (androidx.preference.SwitchPreference) findPreference(settingInfo.StatusBar);
        Fixed = (androidx.preference.SwitchPreference) findPreference(settingInfo.Fixed);
        showMS = (androidx.preference.SwitchPreference) findPreference(settingInfo.showMS);
        textSize = (SeekBarPreference) findPreference(settingInfo.TextSize);
        backgroundWidth = (SeekBarPreference) findPreference(settingInfo.BackgroundWidth);
        backgroundHeight = (SeekBarPreference) findPreference(settingInfo.BackgroundHeight);
        textColor = (ColorPreferenceCompat) findPreference(settingInfo.textColor);
        bgColor=(ColorPreferenceCompat) findPreference(settingInfo.bgColor);

        isHide.setOnPreferenceChangeListener(new dataChangeListener());
        isCountdown.setOnPreferenceChangeListener(new dataChangeListener());
        statusBar.setOnPreferenceChangeListener(new dataChangeListener());
        Fixed.setOnPreferenceChangeListener(new dataChangeListener());
        showMS.setOnPreferenceChangeListener(new dataChangeListener());
        textSize.setOnPreferenceChangeListener(new dataChangeListener());
        backgroundWidth.setOnPreferenceChangeListener(new dataChangeListener());
        backgroundHeight.setOnPreferenceChangeListener(new dataChangeListener());
        textColor.setOnPreferenceChangeListener(new dataChangeListener());
        bgColor.setOnPreferenceChangeListener(new dataChangeListener());

    }



    class dataChangeListener implements Preference.OnPreferenceChangeListener {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            if (preference.getKey().equals(settingInfo.showMS))
                timeService.showMS = (boolean) newValue;
            else
                clock.setWindowInfo(preference.getKey(), newValue);

            return true;
        }
    }
}
