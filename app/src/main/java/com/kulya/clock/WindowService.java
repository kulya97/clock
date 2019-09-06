package com.kulya.clock;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.NumberPicker;

import java.util.Calendar;

import androidx.core.app.NotificationCompat;

public class WindowService extends Service {
    private Thread timeThread;
    private Button imageButton1;
    private NumberPicker picker;
    private NumberPicker picker2;
    private Button countdown;
    private Button stopcountdown;
    LinearLayout toucherLayout;
    LinearLayout CountdownLayout;
    WindowManager.LayoutParams params;
    WindowManager windowManager;
    private Boolean isShowing = true;
    private boolean isTaskA = false;
    private boolean isFirst = true;
    private int Countdown;
    int statusBarHeight = 0;
    int minute;
    int second;
    int hour;
    final int point[] = new int[2];

    class TimeThread extends Thread {
        @Override
        public void run() {
            while (true) {
                try {
                    Message msg = new Message();
                    if (!isTaskA) msg.what = 1;
                    else msg.what = 2;
                    mHandler.sendMessage(msg);
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Calendar calendar = Calendar.getInstance();
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
                    minute = calendar2.get(Calendar.MINUTE);
                    second = calendar2.get(Calendar.SECOND);
                    int time = minute * 60 + second;
                    if (isFirst) {
                        Countdown = picker.getValue() * 60 + picker2.getValue();
                        if (Countdown >= time)
                            Countdown = Countdown - time;
                        else
                            Countdown = Countdown - time + 3600;
                        isFirst = false;
                    }
                    if (Countdown == 0) {
                        isFirst = true;
                        isTaskA = false;
                        break;
                    }
                    minute = Countdown / 60;
                    second = Countdown % 60;
                    Countdown--;
                    try {
                        imageButton1.setText(String.format("%02d", minute) + ":" + String.format("%02d", second)); //更新时
                    } catch (Exception e) {
                    }

                default:
                    break;
            }
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        startService();
        createToucher();
        timeThread = new TimeThread();
        timeThread.start();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void createToucher() {
        params = new WindowManager.LayoutParams();
        windowManager = (WindowManager) getApplication().getSystemService(Context.WINDOW_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
            params.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        else params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        DisplayMetrics metric = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metric);
        final int width = metric.widthPixels;     // 屏幕宽度（像素）
        final int height = metric.heightPixels;   // 屏幕高度（像素）
        params.gravity = Gravity.LEFT | Gravity.TOP;

        final int window_x = WindowManager.LayoutParams.WRAP_CONTENT;
        final int window_y = WindowManager.LayoutParams.WRAP_CONTENT;
        params.width = window_x;
        params.height = window_y;

        toucherLayout = (LinearLayout) LayoutInflater.from(getApplication()).inflate(R.layout.floatwindow, null);
        windowManager.addView(toucherLayout, params);
        //主动计算出当前View的宽高信息.
        toucherLayout.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        //用于检测状态栏高度.
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0)
            statusBarHeight = getResources().getDimensionPixelSize(resourceId);
        else statusBarHeight = 0;
        picker = toucherLayout.findViewById(R.id.numberpicker);
        picker2 = toucherLayout.findViewById(R.id.numberpicker2);
        countdown = toucherLayout.findViewById(R.id.countdown);
        stopcountdown = toucherLayout.findViewById(R.id.stopcountdown);
        CountdownLayout = toucherLayout.findViewById(R.id.hy);
        picker.setMaxValue(59);
        picker.setMinValue(0);
        picker.setValue(0);
        picker.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);//不可手动修改
        picker2.setMaxValue(59);
        picker2.setMinValue(0);
        picker2.setValue(0);
        picker2.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        imageButton1 = toucherLayout.findViewById(R.id.imageButton1);
        imageButton1.setOnClickListener(new itemOnclick());
        countdown.setOnClickListener(new itemOnclick());
        stopcountdown.setOnClickListener(new itemOnclick());
        imageButton1.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (params.x < 0) params.x = 0;
                else if (params.x > width - window_x) params.x = width - window_x;
                if (params.y < 0) params.y = 0;
                else if (params.y > height - window_y - statusBarHeight)
                    params.y = height - window_y - statusBarHeight;
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        point[0] = (int) event.getRawX() - params.x;
                        point[1] = (int) event.getRawY() - params.y - statusBarHeight;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        params.x = (int) event.getRawX() - point[0];
                        params.y = (int) event.getRawY() - point[1] - statusBarHeight;
                        windowManager.updateViewLayout(toucherLayout, params);
                        break;
                    default:
                        break;
                }
                return false;
            }
        });

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
                .setContentText("点击可关闭")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                .setAutoCancel(true)//通知自动取消
                .setContentIntent(pendingIntent)
                .build();
        startForeground(1, notification);
    }

    class itemOnclick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.imageButton1:
                    if (!isShowing) {
                        CountdownLayout.setVisibility(View.VISIBLE);
                    } else {
                        CountdownLayout.setVisibility(View.GONE);
                    }
                    isShowing = !isShowing;
                    break;
                case R.id.stopcountdown:
                    CountdownLayout.setVisibility(View.GONE);
                    isShowing = false;
                    isTaskA = false;
                    isFirst = true;
                    break;
                case R.id.countdown:
                    CountdownLayout.setVisibility(View.GONE);
                    isShowing = false;
                    isTaskA = true;
                    isFirst = true;
                    break;
                default:
                    break;
            }
        }

    }

    @Override
    public void onDestroy() {
        //用imageButton检查悬浮窗还在不在，这里可以不要。优化悬浮窗时要用到。
        if (imageButton1 != null) {
            windowManager.removeView(toucherLayout);
        }
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

}
