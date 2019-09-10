package com.sinovatio.iesi.model;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.contract.TargetInfoQueryContract;
import com.sinovatio.iesi.http.RetrofitClient;

import io.reactivex.Flowable;

public class TargetInfoQueryModel implements TargetInfoQueryContract.Model {
    @Override
    public Flowable<BaseArrayBean<Object>> getTargetUser(String account, String missionId,String queryType) {
        return RetrofitClient.getInstance().getInterface().getTargetUser(account,missionId,queryType);
    }

    @Override
    public Flowable<BaseArrayBean<Object>> refTargetUser(String account, String missionId, String targetId, String name, String identityCode, String bdType) {
        return RetrofitClient.getInstance().getInterface().refTargetUser(account,missionId,targetId,name,identityCode,bdType);
    }
}
