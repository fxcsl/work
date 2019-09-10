package com.sinovatio.iesi.contract;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.BasePresenter;
import com.sinovatio.iesi.BaseView;
import com.sinovatio.iesi.model.entity.TargetBean;

import java.util.List;

import io.reactivex.Flowable;

public interface TargetInfoQueryContract {
    interface Model {
        //获取目标信息数据
        Flowable<BaseArrayBean<Object>> getTargetUser(String account, String missionId, String queryType);

        Flowable<BaseArrayBean<Object>> refTargetUser(String account, String missionId, String targetId, String name, String identityCode, String bdType);
    }

    interface View extends BaseView<TargetInfoQueryContract.Presenter> {
        void onSuccess(List<TargetBean> bean);
        void onbdTypeSuccess(BaseArrayBean<Object> bean);
        void onError(Throwable throwable);
    }

    interface Presenter extends BasePresenter {
        void getTargetUser(String account, String missionId,String queryType);
        void refTargetUser(String account, String missionId, String targetId, String name, String identityCode, String bdType);
    }
}
