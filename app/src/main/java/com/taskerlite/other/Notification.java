package com.taskerlite.other;


import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;

import com.taskerlite.R;

import java.util.HashMap;

public class Notification {

    private static Notification instance;

    private static Context context;
    private NotificationManager manager;
    private int lastId = 0;
    private HashMap<Integer, android.app.Notification> notifications;

    public static Notification getInstance(Context context) {
        if (instance == null) {
            instance = new Notification(context);
        } else {
            instance.context = context;
        }
        return instance;
    }

    private Notification(Context context) {
        this.context = context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notifications = new HashMap<Integer, android.app.Notification>();
    }

    public int createInfoNotification(String header, String message) {
        NotificationCompat.Builder nb = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher) //иконка уведомления
                .setAutoCancel(true) //уведомление закроется по клику на него
                .setTicker(message) //текст, который отобразится вверху статус-бара при создании уведомления
                .setContentText(message) // Основной текст уведомления
                //.setContentIntent(PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT))
                .setWhen(System.currentTimeMillis()) //отображаемое время уведомления
                .setContentTitle(header) //заголовок уведомления
                .setDefaults(android.app.Notification.DEFAULT_VIBRATE);

        android.app.Notification notification = nb.getNotification(); //генерируем уведомление
        manager.notify(lastId, notification); // отображаем его пользователю.
        notifications.put(lastId, notification); //теперь мы можем обращаться к нему по id
        return lastId++;
    }
}