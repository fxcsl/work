package com.sinovatio.iesi.model;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.contract.StatusContract;
import com.sinovatio.iesi.http.RetrofitClient;

import io.reactivex.Flowable;

public class StatusModel implements StatusContract.Model {
    @Override
    public Flowable<BaseArrayBean<Object>> changeGroupWorkingState(String account, String missionId, String dicValue) {
        return RetrofitClient.getInstance().getInterface().changeGroupWorkingState(account,missionId,dicValue);
    }

    @Override
    public Flowable<BaseArrayBean<Object>> getWorkingStateDic() {
        return RetrofitClient.getInstance().getInterface().getWorkingStateDic();
    }
}
