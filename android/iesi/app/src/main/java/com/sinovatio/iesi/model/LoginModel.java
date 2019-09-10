package com.sinovatio.iesi.model;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.contract.LoginContract;
import com.sinovatio.iesi.http.RetrofitClient;
import com.sinovatio.iesi.model.adb.UserInfo;

import org.litepal.LitePal;

import io.reactivex.Flowable;

public class LoginModel implements LoginContract.Model {

    @Override
    public Flowable<BaseArrayBean<Object>> login(String account, String password) {
        return RetrofitClient.getInstance().getInterface().login(account,password);
    }

    /**
     * 先清空表，再添加记录
     * @return
     */
    @Override
    public boolean saveUser(String account ,String password,String name,String department) {
        LitePal.deleteAll(UserInfo.class);
        UserInfo user = new UserInfo();
        user.setAccount(account);
        user.setPassword(password);
        user.setName(name);
        user.setDepartment(department);
        return user.save();

    }

}
