package com.silverforge.elasticsearchrawclient;

import android.app.Application;
import android.content.Context;

public class ElasticClientApp extends Application {
    private static Context context;

    public void onCreate(){
        super.onCreate();
        ElasticClientApp.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return ElasticClientApp.context;
    }
}
