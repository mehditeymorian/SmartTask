package ir.timurid.smarttask.utils;

import android.content.res.ColorStateList;
import android.graphics.Color;

public class ColorUtils {

    public static ColorStateList getColorStateList(String color) {
        if(color == null) return null;
        ColorStateList colorStateList = null;
        try {
            colorStateList = ColorStateList.valueOf(Color.parseColor(color));
        } catch (IllegalArgumentException e) {
            // TODO: 8/21/2020 log this motherfucker
        }

        return colorStateList;
    }

    public static ColorStateList getColorStateList(int color) {
        return ColorStateList.valueOf(color);
    }

    public static int parseColor(String color) {
        int result = Color.GRAY;
        if(color == null) return result;

        try {
            result = Color.parseColor(color);
        } catch (IllegalArgumentException e) {
            // TODO: 8/21/2020 log this motherfucker
        }

        return result;
    }



}
