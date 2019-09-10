package com.sinovatio.iesi.model;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.contract.TargetProfileQueryContract;
import com.sinovatio.iesi.http.RetrofitClient;

import io.reactivex.Flowable;

public class TargetProfileQueryModel implements TargetProfileQueryContract.Model {
    @Override
    public Flowable<BaseArrayBean<Object>> getTargetList(String account, String missionId) {
        return RetrofitClient.getInstance().getInterface().getTargetList(account,missionId);
    }
}
