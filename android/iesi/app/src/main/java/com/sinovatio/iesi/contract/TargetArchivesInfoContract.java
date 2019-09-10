package com.sinovatio.iesi.contract;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.BasePresenter;
import com.sinovatio.iesi.BaseView;
import com.sinovatio.iesi.model.entity.TargetArchivesInfo_entity;

import java.util.List;

import io.reactivex.Flowable;

/**
 * 目标详情mvp契约类
 */
public interface TargetArchivesInfoContract {

    public interface Model{
        Flowable<BaseArrayBean<Object>> getCaseInfo(String account,String caseId);
    }

    public interface Presenter extends BasePresenter{
        //获取目标档案信息
       void getCaseInfo(String account,String caseId);
    }

    public interface View extends BaseView<Presenter>{
       void getCaseInfoSuccess(TargetArchivesInfo_entity entity);
       void getError(String info);
    }
}
