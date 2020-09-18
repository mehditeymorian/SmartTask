package ir.timurid.smarttask;

import android.content.Context;

import dagger.hilt.android.HiltAndroidApp;
import ir.timurid.smarttask.utils.LocaleManager;

@HiltAndroidApp
public class Application extends android.app.Application {


    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(LocaleManager.wrapContext(base));
    }

}
