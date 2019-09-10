package com.sinovatio.mapp.model;


import com.sinovatio.mapp.bean.BaseObjectBean;
import com.sinovatio.mapp.bean.LoginBean;
import com.sinovatio.mapp.contract.LoginContract;
import com.sinovatio.mapp.net.RetrofitClient;

import io.reactivex.Flowable;

public class LoginModel implements LoginContract.Model {
    @Override
    public Flowable<BaseObjectBean<LoginBean>> login(String username, String password) {
        return RetrofitClient.getInstance().getApi().login(username, password);
    }
}
