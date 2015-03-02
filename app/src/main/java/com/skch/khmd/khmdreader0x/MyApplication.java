package com.skch.khmd.khmdreader0x; 
 
 
import android.app.Application;
import android.content.Context;
 
public class MyApplication extends Application {
 
 
    private static MyApplication sInstance;
 

 
 
    public static MyApplication getInstance() { 
        return sInstance;
    } 
 
 
    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    } 
 }
