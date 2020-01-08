package com.kulya.clock.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;

import com.kulya.clock.R;
import com.kulya.clock.until.Myapplication;
import com.kulya.clock.until.NotificationClickReceiver;
import com.kulya.clock.until.settingInfo;
import com.kulya.clock.view.clock;
import com.kulya.clock.view.timePick;

import java.util.Calendar;

import androidx.core.app.NotificationCompat;

public class timeService extends Service {
    public static Context context;

    public static boolean showMS = settingInfo.getBooleanInfo(settingInfo.showMS);
    private boolean isCountdownTask = false;
    private timeHandler timeHandler;
    private int Countdown;
    private int hour;
    private int minute;
    private int second;
    private int millisecond;
    private int hour0;
    private int minute0;
    private int second0;
    private int millisecond0;

    class itemOnclick implements clock.ItemOnClickInterface {

        @Override
        public void onItemClick(View view) {
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
        }
    }

    public timeService() {
    }

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
            hour = calendar.get(Calendar.HOUR_OF_DAY);
            minute = calendar.get(Calendar.MINUTE);
            second = calendar.get(Calendar.SECOND);
            millisecond = calendar.get(Calendar.MILLISECOND)/100;
            String timeString;
            switch (msg.what) {
                case 1:
                    try {
                        timeString = String.format("%02d", hour) + ":"
                                + String.format("%02d", minute) + ":" + String.format("%02d", second);
                        if (showMS)
                            timeString = timeString + ":" + String.format("%01d", millisecond);
                        clock.setText(timeString); //更新时
                    } catch (Exception e) {
                    }
                    break;
                case 2:
                    int time = hour * 3600 + minute * 60 + second;
                    Countdown = hour0 * 3600 + minute0 * 60 + second0;
                    if (time > Countdown)
                        Countdown += 60 * 60 * 24;
                    hour = (Countdown - time) / 3600;
                    minute = (Countdown - time) / 60 % 60;
                    second = (Countdown - time) % 60;
                    try {
                        clock.setText(String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":" + String.format("%02d", second)); //更新时
                    } catch (Exception e) {
                    }

                default:
                    break;
            }
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        timeHandler = new timeHandler();
        new TimeThread().start();
        clock.init();
        clock.setItemOnClick(new itemOnclick());
        startService();
        context = getBaseContext();
    }

    private void startService() {
        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel("default", "name", NotificationManager.IMPORTANCE_HIGH);
            manager.createNotificationChannel(notificationChannel);
        }

        Intent intent = new Intent(this, NotificationClickReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, 0);
        Notification notification = new NotificationCompat.Builder(this, "default")
                .setContentTitle("悬浮时钟正在运行")
                .setContentText("点击跳转设置界面")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        clock.onDismiss();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
