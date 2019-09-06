package com.kulya.clock;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class NotificationClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent stop=new Intent(context,WindowService.class);
        context.stopService(stop);
        Toast.makeText(context,"时钟服务已关闭",Toast.LENGTH_SHORT).show();
}
}
