package com.sinovatio.iesi.contract;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.BaseBean;
import com.sinovatio.iesi.BasePresenter;
import com.sinovatio.iesi.BaseView;
import com.sinovatio.iesi.model.adb.UserInfo;
import com.sinovatio.iesi.model.entity.VersionBean;

import io.reactivex.Flowable;

public interface MainContract {

    interface Model {
        Flowable<BaseBean<VersionBean>> checkAPKVersion();
    }

    interface View extends BaseView<Presenter>{

        void success(UserInfo u);
        void HttpSuccess(BaseBean<VersionBean> m);
        void fail(String s);
    }

    interface Presenter extends BasePresenter{
        void insert_select_adb(String s);
    }
}
