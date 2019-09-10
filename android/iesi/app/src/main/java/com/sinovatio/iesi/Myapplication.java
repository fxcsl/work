package com.sinovatio.iesi;

import android.app.Application;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import org.litepal.LitePal;

public class Myapplication extends Application {

    public static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        LitePal.initialize(this);
        SQLiteDatabase db = LitePal.getDatabase();

    }

    public Context getApplicationContext(){
        return context;
    }


}
