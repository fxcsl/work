package com.sinovatio.iesi.model;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.contract.TargetArchivesInfoContract;
import com.sinovatio.iesi.http.RetrofitClient;

import io.reactivex.Flowable;

public class TargetArchivesInfoModel implements TargetArchivesInfoContract.Model {
    @Override
    public Flowable<BaseArrayBean<Object>> getCaseInfo(String account, String caseId) {
        return RetrofitClient.getInstance().getInterface().getCaseInfo(account,caseId);
    }

}
