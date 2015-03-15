package com.taskerlite.source;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.taskerlite.R;
import com.taskerlite.source.Types.*;

import java.util.HashMap;

public class Icons {

    private static Icons instance = null;

    public static int builderSize, deleteSize;
    private Context context;

    private HashMap<TYPES, Bitmap>   iconsBuilderList = new HashMap<TYPES, Bitmap>();
    private Bitmap tmpBigIcon, builderIcon;
    private Bitmap deleteIcon;
    private BitmapDrawable swMenuStart, swMenuStop;

    public static Icons prepareResource(Context context){

        if(instance == null)
            instance = new Icons(context);

        return instance;
    }

    public static Icons getInstance(){

        return instance;
    }

    public Icons(Context context){

        this.context = context;
        this.builderSize = Screen.getWidth(context)/context.getResources().getInteger(R.integer.icon_divider);
        this.deleteSize  = builderSize/3;

        generateIconsDrawable();
    }

    private void generateIconsDrawable(){

        generateSwipeMenu();

        generateDeleteIcon();

        generate(TYPES.A_TIME, R.drawable.a_timer);
        generate(TYPES.A_BOOT_COMPLETE, R.drawable.a_boot_complete);
        generate(TYPES.A_SCREEN_ON, R.drawable.a_screen_on);
        generate(TYPES.A_SCREEN_OFF, R.drawable.a_screen_off);
        generate(TYPES.T_APP, R.drawable.t_app);
        generate(TYPES.T_THREE_G, R.drawable.t_three_g);
        generate(TYPES.T_WIFI, R.drawable.t_wifi);
        generate(TYPES.T_ACCESS_POINT, R.drawable.t_access_point);
        generate(TYPES.T_SCREEN, R.drawable.t_screen);
        generate(TYPES.T_MOBILE_LIGHT, R.drawable.t_light);
        generate(TYPES.T_GPS, R.drawable.t_gps);
    }

    private void generateDeleteIcon(){

        tmpBigIcon  = BitmapFactory.decodeResource(context.getResources(), R.drawable.delete_element);
        deleteIcon = Bitmap.createScaledBitmap(tmpBigIcon, deleteSize, deleteSize, true);
    }

    public Bitmap getDeleteIcon() {
        return deleteIcon;
    }

    private void generateSwipeMenu(){

        int iconSize = (int)context.getResources().getDimension(R.dimen.swipe_menu_icon_size);

        tmpBigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.swipe_start);
        tmpBigIcon = Bitmap.createScaledBitmap(tmpBigIcon, iconSize, iconSize, true);
        swMenuStart = new BitmapDrawable(context.getResources(), tmpBigIcon);

        tmpBigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.swipe_pause);
        tmpBigIcon = Bitmap.createScaledBitmap(tmpBigIcon, iconSize, iconSize, true);
        swMenuStop = new BitmapDrawable(context.getResources(), tmpBigIcon);
    }

    public BitmapDrawable getSwMenuStart() {
        return swMenuStart;
    }

    public BitmapDrawable getSwMenuStop() {
        return swMenuStop;
    }

    private void generate(TYPES typeIcon, int res){

        tmpBigIcon = BitmapFactory.decodeResource(context.getResources(), res);
        builderIcon = Bitmap.createScaledBitmap(tmpBigIcon, builderSize, builderSize, true);
        iconsBuilderList.put(typeIcon, builderIcon);
    }

    public Bitmap getBuilderIcon(TYPES icon){

        return iconsBuilderList.get(icon);
    }

}
