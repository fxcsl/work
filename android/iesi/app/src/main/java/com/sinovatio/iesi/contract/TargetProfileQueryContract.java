package com.sinovatio.iesi.contract;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.BasePresenter;
import com.sinovatio.iesi.BaseView;
import com.sinovatio.iesi.model.entity.ProfileBean;

import java.util.List;

import io.reactivex.Flowable;

public interface TargetProfileQueryContract {
    interface Model {
        //获取目标信息数据
        Flowable<BaseArrayBean<Object>> getTargetList(String account, String missionId);
    }
    interface View extends BaseView<Presenter> {
        void onSuccess(List<ProfileBean> bean);
        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter {
        void getTargetList(String account, String missionId);
    }
}
