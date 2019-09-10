package com.sinovatio.iesi.model;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.BaseBean;
import com.sinovatio.iesi.contract.MainContract;
import com.sinovatio.iesi.http.RetrofitClient;
import com.sinovatio.iesi.model.entity.VersionBean;

import io.reactivex.Flowable;

public class MainModel  implements MainContract.Model {

    @Override
    public Flowable<BaseBean<VersionBean>> checkAPKVersion() {
        return RetrofitClient.getInstance().getInterface().checkAPKVersion();
    }
}
