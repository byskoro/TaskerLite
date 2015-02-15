package com.taskerlite.other;

import android.content.Context;
import android.os.Vibrator;

public class Vibro {

    public static void playShort(Context context) {

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(300);
    }

    public static void playLong(Context context) {

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        v.vibrate(1000);
    }

    public static void playMovement(Context context) {

        Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        long[] pattern = {0, 100, 100, 100, 100};
        v.vibrate(pattern, -1);
    }
}
