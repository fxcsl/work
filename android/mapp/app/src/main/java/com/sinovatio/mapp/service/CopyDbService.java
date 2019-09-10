package com.sinovatio.mapp.service;

import android.app.IntentService;
import android.content.Intent;

public class CopyDbService extends IntentService {
    public static final String TAG=IntentService.class.getSimpleName();
    public CopyDbService() {
        super(TAG);
    }
    /**
     *  异步执行，不在主线程执行，执行完后自动停止Service。
     * @param intent
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        //WriterDBUtils.copyDBFromRaw(MyApplication.getContext());
    }
}
