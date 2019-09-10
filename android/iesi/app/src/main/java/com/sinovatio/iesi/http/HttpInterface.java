package com.sinovatio.iesi.http;

import com.sinovatio.iesi.BaseArrayBean;
import com.sinovatio.iesi.BaseBean;
import com.sinovatio.iesi.model.entity.VersionBean;

import io.reactivex.Flowable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Rxjava2  Observable -> Flowable
 *           Observer  -> Subscriber
 */
public interface HttpInterface {

    @GET("app/checkAPKVersion")
    Flowable<BaseBean<VersionBean>> checkAPKVersion();

    @FormUrlEncoded
    @POST("app/login")
    Flowable<BaseArrayBean<Object>> login(@Field("account") String account,
                                            @Field("password") String password);

    @FormUrlEncoded
    @POST("app/logout")
    Flowable<BaseArrayBean<Object>> logout(@Field("account") String account);

    @FormUrlEncoded
    @POST("app/receiveGpsInfo")
    Flowable<BaseArrayBean<Object>> receiveGpsInfo(@Field("account") String account,
                                          @Field("longitude") String longitude,
                                          @Field("latitude") String latitude,
                                          @Field("missionId") String missionId);

    //设备类型查询
    @POST("app/equipmentEntityList")
    Flowable<BaseArrayBean<Object>> equipmentTypes();

    /**
     * 已分配设备
     * @param missionId 任务ID
     * @param account 人员ID
     * @return
     */
    @FormUrlEncoded
    @POST("app/accountEquipment")
    Flowable<BaseArrayBean<Object>> accountEquipment(@Field("missionId")String missionId,@Field("account") String account);

    /**
     * 删除已分配设备
     * @param lastEquipmentId 设备id
     * @param missionId 任务ID
     * @param account 人员ID
     * @return
     */
    @FormUrlEncoded
    @POST("app/deleteLastEquipment")
    Flowable<BaseArrayBean<Object>> deleteLastEquipment(@Field("lastEquipmentId")String lastEquipmentId,@Field("missionId")String missionId,@Field("account") String account);

    /**
     * 上传已分配设备
     * @param lastEquipmentId 设备id 多个逗号分隔
     * @param missionId 任务ID
     * @param account 人员ID
     * @return
     */
    @FormUrlEncoded
    @POST("app/updateLastEquipment")
    Flowable<BaseArrayBean<Object>> updateLastEquipment(@Field("lastEquipmentId")String lastEquipmentId,@Field("missionId")String missionId,@Field("account") String account);

    /**
     *
     * @param account
     * @param missionState
     * @return
     */
    @FormUrlEncoded
    @POST("app/selectMissionList")
    Flowable<BaseArrayBean<Object>> getTaskList(@Field("account") String account,
                                          @Field("missionState") Integer missionState);

    @FormUrlEncoded
    @POST("app/getTargetUser")
    Flowable<BaseArrayBean<Object>> getTargetUser(@Field("account") String account,
                                                @Field("missionId") String missionId,
                                                @Field("queryType") String queryType);

    @FormUrlEncoded
    @POST("app/refTargetUser")
    Flowable<BaseArrayBean<Object>> refTargetUser(@Field("account") String account,
                                                  @Field("missionId") String missionId,
                                                  @Field("targetId") String targetId,
                                                  @Field("name") String name,
                                                  @Field("identityCode") String identityCode,
                                                  @Field("bdType") String bdType);

    /**
     * 查找目标档案详情
     * @param account
     * @param caseId
     * @return
     */
    @FormUrlEncoded
    @POST("app/getCaseInfo")
    Flowable<BaseArrayBean<Object>> getCaseInfo(@Field("account") String account,
                                                @Field("caseId") String caseId);

    @FormUrlEncoded
    @POST("app/getTargetList")
    Flowable<BaseArrayBean<Object>> getTargetList(@Field("account") String account,
                                                  @Field("missionId") String missionId);

    /**
     * 提交更新作业状态
     * @param account
     * @param missionId
     * @param dicValue
     * @return
     */
    @FormUrlEncoded
    @POST("app/changeGroupWorkingState")
    Flowable<BaseArrayBean<Object>> changeGroupWorkingState(@Field("executorId") String account,
                                                            @Field("taskId") String missionId,
                                                            @Field("workingState")String dicValue);

    /**
     * 获取 作业状态列表
     * @return
     */
    @POST("app/getWorkingStateDic")
    Flowable<BaseArrayBean<Object>> getWorkingStateDic();

    /**
     * 获取当前作业状态
     * @param account
     * @param missionId
     * @return
     */
    @FormUrlEncoded
    @POST("app/getWorkingState")
    Flowable<BaseArrayBean<Object>> getWorkingState(@Field("account") String account,
                                                            @Field("missionId") String missionId);
}
