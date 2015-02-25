package com.taskerlite.other;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.v4.app.NotificationCompat;

import com.taskerlite.R;

import java.util.HashMap;

public class NotificationUtils {

    private static NotificationUtils instance;

    private static Context context;
    private NotificationManager manager;
    private int lastId = 0;
    private HashMap<Integer, Notification> notifications;

    public static NotificationUtils getInstance(Context context) {
        if (instance == null) {
            instance = new NotificationUtils(context);
        } else {
            instance.context = context;
        }
        return instance;
    }

    private NotificationUtils(Context context) {
        this.context = context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifications = new HashMap<Integer, Notification>();
    }

    public int createInfoNotification(String message) {
        //Intent notificationIntent = new Intent(context, HomeActivity.class); // по клику на уведомлении откроется HomeActivity
        NotificationCompat.Builder nb = new NotificationCompat.Builder(context)
        //NotificationCompat.Builder nb = new NotificationBuilder(context) //для версии Android > 3.0
                .setSmallIcon(R.drawable.ic_launcher) //иконка уведомления
                .setAutoCancel(true) //уведомление закроется по клику на него
                .setTicker(message) //текст, который отобразится вверху статус-бара при создании уведомления
                .setContentText(message) // Основной текст уведомления
                //.setContentIntent(PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT))
                .setWhen(System.currentTimeMillis()) //отображаемое время уведомления
                .setContentTitle(context.getResources().getString(R.string.app_name)) //заголовок уведомления
                .setDefaults(Notification.DEFAULT_VIBRATE);

        Notification notification = nb.getNotification(); //генерируем уведомление
        manager.notify(lastId, notification); // отображаем его пользователю.
        notifications.put(lastId, notification); //теперь мы можем обращаться к нему по id
        return lastId++;
    }
}