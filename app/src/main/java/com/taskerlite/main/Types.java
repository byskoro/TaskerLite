package com.taskerlite.main;

public class Types {

    public static enum TYPES {A_TIME, T_APP, T_THREE_G, T_WIFI, T_ACCESS_POINT, T_UNLOCK_SCREEN}

    private static int[] mColors = new int[] {
            0xFFFFFFCC,
            0xFF6666FF,
            0xFFFF9900,
            0xFF66CCCC,
            0xFFFF0000,
            0xFFFF6699,
            0xFF0000CC,
            0xFFFFFF66,
            0xFFCC66CC,
            0xFF33CC99,
            0xFFFF6600,
            0xFFFFFF66,
            0xFF3399FF,
            0xFFCCCC33
    };

    public static int getColor(int index){

        return mColors[index];
    }
}
