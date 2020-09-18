package ir.timurid.smarttask.db;

import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatDelegate;


public class Preferences {
    private static final String PREFERENCES = "PREFERENCES";
    public static final String LANGUAGE = "LANGUAGE";
    public static final String THEME = "THEME";
    public static final String WATERFALL_TODO_ADD = "WATERFALL_TODO_ADD";

    public static final String FA = "fa";
    public static final String EN = "en-us";

    private static SharedPreferences getSharedPreferences(Context context) {
        return context.getSharedPreferences(PREFERENCES, Context.MODE_PRIVATE);
    }

    private static SharedPreferences.Editor getEditor(Context context) {
        return getSharedPreferences(context).edit();
    }

    public static String getLanguage(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getString(LANGUAGE, EN);
    }

    public static void setLanguage(Context context, String language) {
        getEditor(context).putString(LANGUAGE, language).apply();
    }

    public static int getThemeMode(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getInt(THEME, AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
    }

    public static void setThemeMode(Context context, int theme) {
        getEditor(context).putInt(THEME, theme).apply();
    }

    public static boolean getWaterfallTodoAdd(Context context) {
        SharedPreferences preferences = getSharedPreferences(context);
        return preferences.getBoolean(WATERFALL_TODO_ADD, false);
    }

    public static void setWaterfallTodoAdd(Context context, boolean enable) {
        getEditor(context).putBoolean(WATERFALL_TODO_ADD, enable).apply();
    }

}
