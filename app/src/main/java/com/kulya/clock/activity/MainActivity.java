package com.kulya.clock.activity;


import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import com.kulya.clock.service.timeService;
import com.kulya.clock.until.settingInfo;
import com.kulya.clock.view.clock;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applicationPermission();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void applicationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (Settings.canDrawOverlays(MainActivity.this)) {
                clock.init();
                Intent startService = new Intent(this, timeService.class);
                startService(startService);
                if (!settingInfo.getBooleanInfo(settingInfo.HideSetPage)) {
                    Intent intent = new Intent(this, setActivity.class);
                    startActivity(intent);
                }
                Toast.makeText(MainActivity.this, "已开启Toucher", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION);
                //intent.setData(Uri.parse("package:"+getPackageName()));
                Toast.makeText(this, "需要取得权限以使用悬浮窗", Toast.LENGTH_SHORT).show();
                startActivity(intent);
            }

        } else {
            Intent startService = new Intent(this, timeService.class);
            startService(startService);
            if (!settingInfo.getBooleanInfo(settingInfo.HideSetPage)) {
                Intent intent = new Intent(this, setActivity.class);
                startActivity(intent);
            }
            finish();
        }
    }


}
