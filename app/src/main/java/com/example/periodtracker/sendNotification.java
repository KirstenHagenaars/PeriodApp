package com.example.periodtracker;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import static android.content.Context.NOTIFICATION_SERVICE;

public class sendNotification extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String NOTIFICATION_CHANNEL_ID  = "101";

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Intent repeating = new Intent(context, MainActivity.class);
        repeating.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100, repeating, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);


        if (intent.getAction().equals("DAILY_NOTIFICATION"))
        {
            notificationBuilder.setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.periodstart)
                    .setTicker("Tutorialspoint")
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle("Daily notification")
                    .setContentText("Don't forget your pill")
                    .setContentInfo("hellooo");
            notificationManager.notify(100,notificationBuilder.build());
        }
        else if (intent.getAction().equals("PERIOD_NOTIFICATION"))
        {
            notificationBuilder.setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.periodadvance)
                    .setTicker("Tutorialspoint")
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle("Period alarm")
                    .setContentText("Your period is predicted to arrive today")
                    .setContentInfo("hoo");
            notificationManager.notify(100,notificationBuilder.build());
        }
        else if (intent.getAction().equals("PERIODADVANCE_NOTIFICATION"))
        {
            notificationBuilder.setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.daily)
                    .setTicker("Tutorialspoint")
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle("Period alarm")
                    .setContentText("Your period is predicted to arrive in 3 days")
                    .setContentInfo("hoo");
            notificationManager.notify(100,notificationBuilder.build());
        }
        else if (intent.getAction().equals("FERTILE_NOTIFICATION"))
        {
            notificationBuilder.setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.drawable.fertility)
                    .setTicker("Tutorialspoint")
                    .setPriority(Notification.PRIORITY_MAX)
                    .setContentTitle("Fertility alarm")
                    .setContentText("Your fertile window is predicted to open now")
                    .setContentInfo("hoo");
            notificationManager.notify(100,notificationBuilder.build());
        }
    }

    private void send(String NOTIFICATION_CHANNEL_ID, String title, String content, Drawable icon, Context context)
    {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);

        Intent repeating = new Intent(context, MainActivity.class);
        repeating.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(context,100, repeating, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, "My Notifications", NotificationManager.IMPORTANCE_MAX);
            // Configure the notification channel.
            notificationChannel.setDescription("Sample Channel description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);


    }
}

