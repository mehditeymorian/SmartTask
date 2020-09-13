package ir.timurid.smarttask.db;

import android.content.Context;
import android.content.SharedPreferences;


public class Preferences {
    private static final String NAME = "PREFERENCES";
    public static final String LANGUAGE = "LANGUAGE";

    public static final String FA = "fa";
    public static final String EN = "en-us";

    public static String getLanguage(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        return preferences.getString(LANGUAGE, EN);
    }

    public static void setLanguage(Context context, String language) {
        SharedPreferences preferences = context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
        preferences.edit().putString(LANGUAGE, language).apply();
    }


}
