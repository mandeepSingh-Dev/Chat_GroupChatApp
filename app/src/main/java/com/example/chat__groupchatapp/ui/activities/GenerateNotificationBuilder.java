package com.example.chat__groupchatapp.ui.activities;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.core.app.NotificationCompat;

public class GenerateNotificationBuilder {


    public  static int NOTIFY_ID = 0525; // start notification id
    protected static final String CHANNEL_ID = "call_kit_notification";

    public static  NotificationCompat.Builder generateNotificatiobnBuilder(Context context, String content) {
        Context appContext = context.getApplicationContext();
        PackageManager pm = appContext.getPackageManager();
        String title = pm.getApplicationLabel(appContext.getApplicationInfo()).toString();
        Intent i = appContext.getPackageManager().getLaunchIntentForPackage(appContext.getPackageName());
        PendingIntent pendingIntent = PendingIntent.getActivity(appContext, NOTIFY_ID, i, PendingIntent.FLAG_IMMUTABLE);

        return new NotificationCompat.Builder(appContext, CHANNEL_ID)
                .setSmallIcon(appContext.getApplicationInfo().icon)
                .setContentTitle(title)
                .setTicker(content)
                .setContentText(content)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(pendingIntent);
    }


    public static  NotificationCompat.Builder generateBaseFullIntentBuilder(Intent fullScreenIntent, Context context, String content) {

        Context appContext = context.getApplicationContext();
        PackageManager pm = appContext.getPackageManager();
        String title = pm.getApplicationLabel(appContext.getApplicationInfo()).toString();
        PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(appContext, NOTIFY_ID, fullScreenIntent, PendingIntent.FLAG_IMMUTABLE);
        return new NotificationCompat.Builder(appContext, CHANNEL_ID)
                .setSmallIcon(appContext.getApplicationInfo().icon)
                .setContentTitle(title)
                .setTicker(content)
                .setContentText(content)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setCategory(NotificationCompat.CATEGORY_CALL)
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .setContentIntent(fullScreenPendingIntent);

    }
}
