package com.odiousrainbow.leftovers.Helpers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;

public class AlarmNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String title = intent.getStringExtra("notiTitle");
        String message = intent.getStringExtra("notiMessage");
        NotificationHelper notificationHelper = new NotificationHelper(context);
        NotificationCompat.Builder builder = notificationHelper.getChannel1Notification(title,message);
        notificationHelper.getManager().notify(1,builder.build());
    }

}
