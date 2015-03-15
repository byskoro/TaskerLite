package com.taskerlite.source;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Fonts {

    private AssetManager mngr;

    public Fonts(Context context) {
        mngr = context.getAssets();
    }
    private enum AssetTypefaces {
        JuraMedium
    }

    private Typeface getTypeface(AssetTypefaces font) {

        Typeface tf = Typeface.DEFAULT;

        switch (font) {
            case JuraMedium:
                tf = Typeface.createFromAsset(mngr,"fonts/JuraMedium.ttf");
                break;
        }

        return tf;
    }
    public void setupLayoutTypefaces(View v) {
        try {
            if (v instanceof ViewGroup) {

                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    setupLayoutTypefaces(child);
                }

            } else if (v instanceof TextView) {

                if (v.getTag().toString().equals(AssetTypefaces.JuraMedium.toString()))
                    ((TextView)v).setTypeface(getTypeface(AssetTypefaces.JuraMedium));
            }
        } catch (Exception e) { }
    }
}
