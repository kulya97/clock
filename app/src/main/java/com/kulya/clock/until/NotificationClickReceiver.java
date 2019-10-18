package com.kulya.clock.until;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.kulya.clock.activity.setActivity;

public class NotificationClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent jump = new Intent(context, setActivity.class);
        jump.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(jump);
    }
}
