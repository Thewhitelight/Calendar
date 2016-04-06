package cn.libery.calendar;

import android.app.Application;

/**
 * Created by Libery on 2016/4/6.
 * Email:libery.szq@qq.com
 */
public class MyApp extends Application {

    private static MyApp myApp;

    @Override
    public void onCreate() {
        super.onCreate();
        myApp = this;
    }

    public static MyApp getInstance() {
        return myApp;
    }
}
