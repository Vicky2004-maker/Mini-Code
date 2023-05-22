package com.example.minicode;

import android.app.Application;

import com.google.android.material.color.DynamicColors;

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        DynamicColors.applyToActivitiesIfAvailable(MainApplication.this);
        super.onCreate();
    }
}
