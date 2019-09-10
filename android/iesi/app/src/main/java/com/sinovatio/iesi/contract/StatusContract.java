package com.sinovatio.iesi.contract;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.BaseBean;
import com.sinovatio.iesi.BasePresenter;
import com.sinovatio.iesi.BaseView;
import com.sinovatio.iesi.model.entity.StatusBean;

import java.util.List;

import io.reactivex.Flowable;

public interface StatusContract {

    interface Model {
        Flowable<BaseArrayBean<Object>> changeGroupWorkingState(String account,
                                                                String missionId,
                                                                String dicValue);
        Flowable<BaseArrayBean<Object>> getWorkingStateDic();

    }

    interface View extends BaseView<Presenter> {
        void updateStateSuccess(String message);
        void getStateDicSuccess(List<StatusBean> list);
        void httpError(int code,String message);
    }

    interface Presenter extends BasePresenter {
        void changeGroupWorkingState(String account,
                                     String missionId,
                                     String dicValue);

        void getWorkingStateDic();
    }
}
