package com.vssve.valarms;

import android.app.Application;
import android.content.Intent;

public class BackgroundProcess extends Application {
    public static BackgroundProcess instance;
    @Override
    public void onCreate() {
        startService(new Intent(this,BackgroundService.class));
        instance = this;
        super.onCreate();
    }

}
