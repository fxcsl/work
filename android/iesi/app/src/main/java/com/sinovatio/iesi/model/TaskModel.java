package com.sinovatio.iesi.model;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.contract.TaskContract;
import com.sinovatio.iesi.http.RetrofitClient;

import io.reactivex.Flowable;

public class TaskModel implements TaskContract.Model {
    @Override
    public Flowable<BaseArrayBean<Object>> getTaskList(String account, Integer missionState) {
        return RetrofitClient.getInstance().getInterface().getTaskList(account,missionState);
    }
}
