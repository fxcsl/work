package com.sinovatio.iesi.presenter;

import android.support.annotation.NonNull;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.contract.LoginContract;
import com.sinovatio.iesi.http.RxScheduler;
import com.sinovatio.iesi.model.LoginModel;

import io.reactivex.functions.Consumer;

public class LoginPresenter implements LoginContract.Presenter {

    @NonNull
    private LoginContract.View view;
    private LoginContract.Model model;

    public LoginPresenter(@NonNull LoginContract.View view){
        this.view = view;
        this.model = new LoginModel();
    }

    @Override
    public void login(String username, String password) {
        view.showLoading();
        model.login(username, password)
        .compose(RxScheduler.<BaseArrayBean<Object>>Flo_io_main())
        .as(view.<BaseArrayBean<Object>>bindAutoDispose())
        .subscribe(new Consumer<BaseArrayBean<Object>>() {
            @Override
            public void accept(BaseArrayBean<Object> bean) throws Exception {
                view.onSuccess(bean);
                view.hideLoading();
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(Throwable throwable) throws Exception {
                view.onError(throwable);
                view.hideLoading();
            }
        });
    }

    @Override
    public boolean remPass(String account,String password,String name,String department){
        return model.saveUser(account,password,name,department);
    }

}
