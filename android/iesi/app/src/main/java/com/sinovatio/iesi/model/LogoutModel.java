package com.sinovatio.iesi.model;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.contract.LogoutContract;
import com.sinovatio.iesi.http.RetrofitClient;

import io.reactivex.Flowable;

public class LogoutModel implements LogoutContract.Model {
    @Override
    public Flowable<BaseArrayBean<Object>> logout(String account) {
        return RetrofitClient.getInstance().getInterface().logout(account);
    }

    @Override
    public Flowable<BaseArrayBean<Object>> getTargetUser(String account, String missionId,String queryType) {
        return RetrofitClient.getInstance().getInterface().getTargetUser(account,missionId,queryType);
    }

    @Override
    public Flowable<BaseArrayBean<Object>> getWorkingState(String account, String missionId) {
        return RetrofitClient.getInstance().getInterface().getWorkingState(account,missionId);
    }
}
