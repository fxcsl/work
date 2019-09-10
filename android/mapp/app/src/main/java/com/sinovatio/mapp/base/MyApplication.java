package com.sinovatio.mapp.base;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.sinovatio.mapp.greendao.DaoMaster;
import com.sinovatio.mapp.greendao.DaoSession;
import com.sinovatio.mapp.utils.WriterDBUtils;

import org.xutils.x;

public class MyApplication extends Application {
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        x.Ext.init(this);
        x.Ext.setDebug(true);

        context = getApplicationContext();

        WriterDBUtils.copyDBFromRaw(context);

        initGreenDao();

    }

    //获取应用上下文环境
    public static Context getContext() {
        return context;
    }

    /**
     * 初始化GreenDao,直接在Application中进行初始化操作
     */
    private void initGreenDao() {

//        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "mapp.db");
//        Database database = helper.getEncryptedWritableDb("sinovatio");
//        DaoMaster daoMaster = new DaoMaster(database);
//        daoSession=daoMaster.newSession();

        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "mapp.db");
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    private DaoSession daoSession;
    public DaoSession getDaoSession() {
        return daoSession;
    }
}
