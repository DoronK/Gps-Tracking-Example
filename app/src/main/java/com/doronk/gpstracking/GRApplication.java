package com.doronk.gpstracking;

import android.app.Application;
import android.content.Context;


public class GRApplication extends Application {

    public static final String TAG = GRApplication.class.getSimpleName();
    private static Context context;

    /**
     * *******************************************************************************
     * GETTERS SETTERS
     * ********************************************************************************
     */
    public static Context getAppContext() {
        return GRApplication.context;
    }


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }
}
