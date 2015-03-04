package com.taskerlite.source;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.widget.RemoteViews;

import com.taskerlite.R;

import java.util.ArrayList;

public class mNotification {

    private static mNotification instance;

    private static Context context;
    private NotificationManager manager;
    private static int index = 0;

    public static mNotification getInstance(Context context) {

        if (instance == null)
            instance = new mNotification(context);
        else
            instance.context = context;

        return instance;
    }

    private mNotification(Context context) {

        this.context = context;
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void createInfoNotification(String header, String message) {

        Notification.Builder nb = new Notification.Builder(context);
        nb.setSmallIcon(R.drawable.ic_launcher);
        nb.setAutoCancel(true);
        nb.setContentText(message);
        nb.setWhen(System.currentTimeMillis());
        nb.setContentTitle(header);

        Notification notification = nb.getNotification();
        notification.flags    |= Notification.FLAG_AUTO_CANCEL;
        notification.defaults |= Notification.DEFAULT_LIGHTS;
        notification.defaults |= Notification.DEFAULT_VIBRATE;
        notification.defaults |= Notification.DEFAULT_SOUND;

        manager.notify(index, notification);
        index++;
    }
}