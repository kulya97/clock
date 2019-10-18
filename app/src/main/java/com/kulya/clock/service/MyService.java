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
import android.os.IBinder;

import com.kulya.clock.R;
import com.kulya.clock.until.NotificationClickReceiver;
import com.kulya.clock.view.clock;

import java.time.Clock;

import androidx.core.app.NotificationCompat;

public class MyService extends Service {
    public static Context context;
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        clock.init();
        startService();
        context=getBaseContext();
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
