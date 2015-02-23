package com.taskerlite.main;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import com.taskerlite.R;
import com.taskerlite.other.Screen;
import com.taskerlite.main.TaskerTypes.*;

import java.util.HashMap;

public class TaskerIcons {

    private static TaskerIcons instance = null;

    public static int previewSize, builderSize, deleteSize;
    private Context context;

    private HashMap<TYPES, Drawable> iconsViewList    = new HashMap<TYPES, Drawable>();
    private HashMap<TYPES, Bitmap>   iconsBuilderList = new HashMap<TYPES, Bitmap>();
    private Bitmap tmpBigIcon, previewIcon, builderIcon;
    private Bitmap selectIcons, deleteIcon, pimpaIcon;
    private BitmapDrawable swMenuDelete, swMenuStart, swMenuStop;

    public static TaskerIcons getInstance(Context context){

        if(instance == null)
            instance = new TaskerIcons(context);

        return instance;
    }

    public static TaskerIcons getInstance(){

        return instance;
    }

    public TaskerIcons(Context context){

        this.context = context;
        this.previewSize = context.getResources().getInteger(R.integer.preview_icon_size);
        this.builderSize = Screen.getWidth(context)/context.getResources().getInteger(R.integer.icon_divider);
        this.deleteSize  = builderSize/3;

        generateIconsDrawable();
    }

    private void generateIconsDrawable(){

        tmpBigIcon  = BitmapFactory.decodeResource(context.getResources(), R.drawable.pimpa);
        pimpaIcon = Bitmap.createScaledBitmap(tmpBigIcon, builderSize, builderSize, true);

        generateSelectIcon();
        generateDeleteIcon();

        generateSwipeMenu();

        generate(TYPES.TIME, R.drawable.a_timer);
        generate(TYPES.APP, R.drawable.t_app);
    }

    private void generateSelectIcon(){

        tmpBigIcon  = BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_select);
        selectIcons = Bitmap.createScaledBitmap(tmpBigIcon, builderSize, builderSize, true);
    }

    public Bitmap getSelectIcons() {
        return selectIcons;
    }

    private void generateDeleteIcon(){

        tmpBigIcon  = BitmapFactory.decodeResource(context.getResources(), R.drawable.delete);
        deleteIcon = Bitmap.createScaledBitmap(tmpBigIcon, deleteSize, deleteSize, true);
    }

    public Bitmap getDeleteIcon() {
        return deleteIcon;
    }

    private void generateSwipeMenu(){

        int iconSize = context.getResources().getInteger(R.integer.swipe_menu_icon_size);

        tmpBigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.swipe_delete);
        tmpBigIcon = Bitmap.createScaledBitmap(tmpBigIcon, Screen.dp2px(context, iconSize), Screen.dp2px(context, iconSize), true);
        swMenuDelete = new BitmapDrawable(context.getResources(), tmpBigIcon);

        tmpBigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.swipe_start);
        tmpBigIcon = Bitmap.createScaledBitmap(tmpBigIcon, Screen.dp2px(context, iconSize), Screen.dp2px(context, iconSize), true);
        swMenuStart = new BitmapDrawable(context.getResources(), tmpBigIcon);

        tmpBigIcon = BitmapFactory.decodeResource(context.getResources(), R.drawable.swipe_pause);
        tmpBigIcon = Bitmap.createScaledBitmap(tmpBigIcon, Screen.dp2px(context, iconSize), Screen.dp2px(context, iconSize), true);
        swMenuStop = new BitmapDrawable(context.getResources(), tmpBigIcon);
    }

    public BitmapDrawable getSwMenuDelete() {
        return swMenuDelete;
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
