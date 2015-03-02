package com.taskerlite.main;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.taskerlite.R;
import com.taskerlite.other.Screen;
import com.taskerlite.main.Types.*;

import java.util.HashMap;

public class Icons {

    private static Icons instance = null;

    public static int previewSize, builderSize, deleteSize;
    private Context context;

    private HashMap<TYPES, Drawable> iconsViewList    = new HashMap<TYPES, Drawable>();
    private HashMap<TYPES, Bitmap>   iconsBuilderList = new HashMap<TYPES, Bitmap>();
    private Bitmap tmpBigIcon, previewIcon, builderIcon;
    private Bitmap deleteIcon, pimpaIcon;
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
        this.previewSize = context.getResources().getInteger(R.integer.preview_icon_size);
        this.builderSize = Screen.getWidth(context)/context.getResources().getInteger(R.integer.icon_divider);
        this.deleteSize  = builderSize/3;

        generateIconsDrawable();
    }

    private void generateIconsDrawable(){

        generateSwipeMenu();

        generateDeleteIcon();
        generatePimpa();

        generate(TYPES.A_TIME, R.drawable.a_timer);
        generate(TYPES.A_BOOT_COMPLETE, R.drawable.a_boot_complete);
        generate(TYPES.T_APP, R.drawable.t_app);
        generate(TYPES.T_THREE_G, R.drawable.t_three_g);
        generate(TYPES.T_WIFI, R.drawable.t_wifi);
        generate(TYPES.T_ACCESS_POINT, R.drawable.t_access_point);
        generate(TYPES.T_UNLOCK_SCREEN, R.drawable.t_unlock_screen);
    }

    private void generatePimpa(){

        tmpBigIcon  = BitmapFactory.decodeResource(context.getResources(), R.drawable.pimpa);
        pimpaIcon   = Bitmap.createScaledBitmap(tmpBigIcon, builderSize, builderSize, true);
    }

    private void generateDeleteIcon(){

        tmpBigIcon  = BitmapFactory.decodeResource(context.getResources(), R.drawable.delete_element);
        deleteIcon = Bitmap.createScaledBitmap(tmpBigIcon, deleteSize, deleteSize, true);
    }

    public Bitmap getDeleteIcon() {
        return deleteIcon;
    }

    private void generateSwipeMenu(){

        int iconSize = context.getResources().getInteger(R.integer.swipe_menu_icon_size);

        tmpBigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.swipe_start);
        tmpBigIcon = Bitmap.createScaledBitmap(tmpBigIcon, Screen.dp2px(context, iconSize), Screen.dp2px(context, iconSize), true);
        swMenuStart = new BitmapDrawable(context.getResources(), tmpBigIcon);

        tmpBigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.swipe_pause);
        tmpBigIcon = Bitmap.createScaledBitmap(tmpBigIcon, Screen.dp2px(context, iconSize), Screen.dp2px(context, iconSize), true);
        swMenuStop = new BitmapDrawable(context.getResources(), tmpBigIcon);
    }

    public BitmapDrawable getSwMenuStart() {
        return swMenuStart;
    }

    public BitmapDrawable getSwMenuStop() {
        return swMenuStop;
    }

    public Bitmap getPimpaIcon() {
        return pimpaIcon;
    }

    private void generate(TYPES typeIcon, int res){

        tmpBigIcon = BitmapFactory.decodeResource(context.getResources(), res);
        previewIcon = Bitmap.createScaledBitmap(tmpBigIcon, Screen.dp2px(context, previewSize), Screen.dp2px(context, previewSize), true);
        builderIcon = Bitmap.createScaledBitmap(tmpBigIcon, builderSize, builderSize, true);
        iconsViewList.put(typeIcon, new BitmapDrawable(context.getResources(), previewIcon));
        iconsBuilderList.put(typeIcon, builderIcon);
    }

    public Drawable getPreviewIcon(TYPES icon){

        return iconsViewList.get(icon);
    }

    public Bitmap getBuilderIcon(TYPES icon){

        return iconsBuilderList.get(icon);
    }

}
