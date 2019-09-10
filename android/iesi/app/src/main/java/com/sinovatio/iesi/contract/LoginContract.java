package com.sinovatio.iesi.contract;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.BasePresenter;
import com.sinovatio.iesi.BaseView;

import io.reactivex.Flowable;

public interface LoginContract {

    interface Model {
        public Flowable<BaseArrayBean<Object>> login(String username,String password);

        public boolean saveUser(String account,String password,String name,String department);
    }

    interface View extends BaseView<Presenter>{
        public void onSuccess(BaseArrayBean<Object> bean);
        public void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter{
        public void login(String username, String password);
        public boolean remPass(String account,String password,String name,String department);
    }
}
