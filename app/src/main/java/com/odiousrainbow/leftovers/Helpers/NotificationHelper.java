package com.odiousrainbow.leftovers.Helpers;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.odiousrainbow.leftovers.Activities.AddStuffDetailsActivity;
import com.odiousrainbow.leftovers.Activities.MainActivity;
import com.odiousrainbow.leftovers.R;

public class NotificationHelper extends ContextWrapper {

    public static final String channel1ID = "TULACHANNEL1ID";
    public static final String channel1Name = "Channel 1";
    public static final String channel2ID = "TULACHANNEL2ID";
    public static final String channel2Name = "Channel 2";

    private NotificationManager notificationManager;

    public NotificationHelper(Context base) {
        super(base);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            createChanels();
        }

    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChanels(){
        NotificationChannel channel1 = new NotificationChannel(channel1ID,channel1Name,NotificationManager.IMPORTANCE_DEFAULT);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel1);

        NotificationChannel channel2 = new NotificationChannel(channel2ID,channel2Name,NotificationManager.IMPORTANCE_DEFAULT);
        channel2.enableLights(true);
        channel2.enableVibration(true);
        channel2.setLightColor(R.color.colorPrimary);
        channel2.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        getManager().createNotificationChannel(channel2);
    }

    public void createNotificationApiBelow26(){

    }


    public NotificationManager getManager(){
        if(notificationManager == null){
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        }
        return notificationManager;
    }


    public NotificationCompat.Builder getChannel1Notification(String title, String message){
        Intent backToTulaIntent = new Intent(getApplicationContext(),MainActivity.class);
        backToTulaIntent.putExtra("navigateToTula","true");
        backToTulaIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this,1,backToTulaIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        return new NotificationCompat.Builder(getApplicationContext(),channel1ID)
                .setVibrate(new long[] {100, 350, 200, 350})
                .setLights(Color.RED,1000,1000)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_food_notification_white_24dp);
    }

    public NotificationCompat.Builder getChannelsNotification(){
        return new NotificationCompat.Builder(getApplicationContext(),channel1ID)
                .setContentTitle("Tula title")
                .setContentText("something")
                .setSmallIcon(R.drawable.ic_food_24dp);
    }

    public NotificationCompat.Builder getChannel2Notification(String title, String message){
        return new NotificationCompat.Builder(getApplicationContext(),channel2ID)
                .setContentTitle(title)
                .setContentText(message)
                .setSmallIcon(R.drawable.ic_food_24dp);
    }
}
