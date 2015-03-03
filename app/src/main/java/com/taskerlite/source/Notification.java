package com.taskerlite.source;

import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import com.taskerlite.R;

import java.util.ArrayList;

public class Notification {

    private static Notification instance;

    private static Context context;
    private NotificationManager manager;
    private ArrayList<String> notificationMessages;
    private static int index = 0;

    public static Notification getInstance(Context context) {
        if (instance == null)
            instance = new Notification(context);
        else
            instance.context = context;

        return instance;
    }

    private Notification(Context context) {
        this.context = context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        notificationMessages = new ArrayList<String>();
    }

    public void createInfoNotification(String header, String message) {

        NotificationCompat.Builder nb = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_launcher)
                .setAutoCancel(true) //уведомление закроется по клику на него
                .setContentTitle(header) //заголовок уведомления
                .setTicker(message) //текст, который отобразится вверху статус-бара при создании уведомления
                .setContentText(message) // Основной текст уведомления
                .setWhen(System.currentTimeMillis())
                .setDefaults(android.app.Notification.DEFAULT_VIBRATE);

        android.app.Notification notification = nb.getNotification(); //генерируем уведомление
        notification.flags |= notification.FLAG_AUTO_CANCEL; // FLAG_ONLY_ALERT_ONCE
        manager.notify(index, notification); // отображаем его пользователю.
        index++;
    }
}