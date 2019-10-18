package com.kulya.clock.activity;

import android.os.Bundle;
import android.widget.Button;

import com.kulya.clock.R;
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
    private androidx.preference.SwitchPreference isBackground;
    private androidx.preference.SwitchPreference isCountdown;
    private androidx.preference.SwitchPreference isBlack;
    private androidx.preference.SwitchPreference statusBar;
    private androidx.preference.SwitchPreference stop;
    private SeekBarPreference backgroundWidth;
    private SeekBarPreference backgroundHeight;
    private Preference textSize;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.perference);
        initView();
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
    }

    private void initView() {
        isHide = (androidx.preference.SwitchPreference) findPreference("hideSetPage");
        isBackground = (androidx.preference.SwitchPreference) findPreference("translate");
        isCountdown = (androidx.preference.SwitchPreference) findPreference("countdown");
        isBlack = (androidx.preference.SwitchPreference) findPreference("black");
        statusBar = (androidx.preference.SwitchPreference) findPreference("statusBar");
        stop = (androidx.preference.SwitchPreference) findPreference("stop");
        textSize = findPreference("textSize");
        backgroundWidth = (SeekBarPreference) findPreference("backgroundWidth");
        backgroundHeight = (SeekBarPreference) findPreference("backgroundHeight");

        isHide.setOnPreferenceChangeListener(new dataChangeListener());
        isBackground.setOnPreferenceChangeListener(new dataChangeListener());
        isCountdown.setOnPreferenceChangeListener(new dataChangeListener());
        isBlack.setOnPreferenceChangeListener(new dataChangeListener());
        statusBar.setOnPreferenceChangeListener(new dataChangeListener());
        stop.setOnPreferenceChangeListener(new dataChangeListener());
        textSize.setOnPreferenceChangeListener(new dataChangeListener());
        backgroundWidth.setOnPreferenceChangeListener(new dataChangeListener());
        backgroundHeight.setOnPreferenceChangeListener(new dataChangeListener());
    }

    class dataChangeListener implements Preference.OnPreferenceChangeListener {

        @Override
        public boolean onPreferenceChange(Preference preference, Object newValue) {
            clock.setWindowInfo(preference.getKey(), newValue);
            return true;
        }
    }
}
