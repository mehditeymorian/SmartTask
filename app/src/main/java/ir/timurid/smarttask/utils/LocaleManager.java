package ir.timurid.smarttask.utils;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;

import java.util.Locale;

import ir.timurid.smarttask.db.Preferences;

public class LocaleManager {


    public static Context wrapContext(Context context) {
        return wrapContext(context, Preferences.getLanguage(context));
    }



    public static Context wrapContext(Context context, String language) {
        Locale locale = new Locale(language);


        Locale.setDefault(locale);

        Resources res = context.getResources();
        Configuration config = new Configuration(res.getConfiguration());
        config.setLocale(locale);

        return context.createConfigurationContext(config);
    }
}
