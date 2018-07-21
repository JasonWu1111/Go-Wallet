package com.rightteam.accountbook;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import com.facebook.stetho.Stetho;
import com.rightteam.accountbook.greendao.DaoMaster;
import com.rightteam.accountbook.greendao.DaoSession;


/**
 * Created by JasonWu on 2017/12/28
 */

public class MyApplication extends Application {
    private static DaoSession sDaoSession;

    @SuppressLint("StaticFieldLeak")
    private static Context sContext;

    @Override
    public void onCreate() {

        super.onCreate();
        sContext = getApplicationContext();
        Stetho.initialize(
                Stetho.newInitializerBuilder(this)
                        .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                        .enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this))
                        .build());
        initDatabase();
//        RetrofitService.init();
    }

    public static Context getContext(){
        return sContext;
    }

    private void initDatabase(){
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(getApplicationContext(), "ab-db", null);
        DaoMaster daoMaster = new DaoMaster(devOpenHelper.getWritableDatabase());
        sDaoSession = daoMaster.newSession();
    }

    public static DaoSession getsDaoSession(){
        return sDaoSession;
    }
}
