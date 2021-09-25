package com.r3z4.sciencetesting;

import android.app.Application;
import android.os.StrictMode;

public class MyApp extends Application {
    static {
        if (BuildConfig.DEBUG) StrictMode.enableDefaults();
    }
}
