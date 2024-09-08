package com.lgh.tapclick.myclass;

import android.app.Application;
import android.content.Context;

import com.lgh.tapclick.myfunction.MyUtils;

public class MyApplication extends Application {
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        DataDaoHelper.init(base);
        MyUtils.init(base);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MyUncaughtExceptionHandler.getInstance(this).run();
    }
}
