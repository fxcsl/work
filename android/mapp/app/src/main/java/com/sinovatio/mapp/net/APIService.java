package com.sinovatio.mapp.net;


import com.sinovatio.mapp.bean.BaseObjectBean;
import com.sinovatio.mapp.bean.LoginBean;
import com.sinovatio.mapp.bean.Upload_Base_Bean;
import com.sinovatio.mapp.bean.VersionBean;

import io.reactivex.Flowable;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface APIService {

    /**
     * 登陆
     *
     * @param username 账号
     * @param password 密码
     * @return
     */
    @FormUrlEncoded
    @POST("user/login")
    Flowable<BaseObjectBean<LoginBean>> login(@Field("username") String username,
                                              @Field("password") String password);

    @FormUrlEncoded
    @POST("uploadInfo")
    Flowable<BaseObjectBean> uploadInfo(@Field("location") String location);

    @GET("checkAPKVersion")
    Flowable<BaseObjectBean<VersionBean>> checkAPKVersion();



    @Headers("Content-Type:text/plain;charset=utf-8")
    @POST("base_station")
    Flowable<BaseObjectBean<Object>> base_station(@Body Upload_Base_Bean<String> ss);

    @Headers("Content-Type:text/plain;charset=utf-8")
    @POST("wifi_ap")
    Flowable<BaseObjectBean<Object>> wifi_ap(@Body  Upload_Base_Bean<String> wifi_beanUpload_base_bean);

    @Headers("Content-Type:text/plain;charset=utf-8")
    @POST("imsi")
    Flowable<BaseObjectBean<VersionBean>> imsi(@Body Upload_Base_Bean<String> bean);

    @Headers("Content-Type:text/plain;charset=utf-8")
    @POST("ap_same_terminal")
    Flowable<BaseObjectBean<VersionBean>> ap_same_terminal(@Body Upload_Base_Bean<String> bean);
}
