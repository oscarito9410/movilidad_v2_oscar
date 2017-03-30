package com.gruposalinas.elektra.movilidadgs.utils;

import android.app.Application;
import android.content.Context;

/**
 * Created by oemy9 on 13/03/2017.
 */

public class ApplicationBase extends Application {
    private static Context context;
    public void onCreate() {
        super.onCreate();
        ApplicationBase.context = getApplicationContext();
    }
    public static Context getAppContext() {
        return ApplicationBase.context;
    }
}
