package com.sinovatio.mapp.net;


import android.support.annotation.NonNull;

import com.alibaba.fastjson.JSON;
import com.sinovatio.mapp.constant.Constants;

import org.xutils.common.util.LogUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {

    private static volatile RetrofitClient instance;
    private APIService apiService;
    private String baseUrl = Constants.PRE_SERVER_URL;

    private RetrofitClient() {

    }

    public static RetrofitClient getInstance() {
        if (instance == null) {
            synchronized (RetrofitClient.class) {
                if (instance == null) {
                    instance = new RetrofitClient();
                }
            }
        }
        return instance;
    }

    /**
     * 设置Header
     *
     * @return
     */
    private Interceptor getHeaderInterceptor() {
        return new Interceptor() {
            @Override
            public Response intercept(@NonNull Chain chain) throws IOException {
//                Response response = null;
//                try {
//                    Request original = chain.request();
//                    Request.Builder requestBuilder = original.newBuilder()
//                            //添加Token
//                            .header("token", "");
//                    response = chain.proceed(requestBuilder.build());
//                } catch (java.lang.AssertionError e) {  //防止SocketTimeoutException
//                    e.printStackTrace();
//                }
//                return response;//执行新请求

                Request request = chain.request().newBuilder()
                        .addHeader("Accept", "text/plain").build();
                return chain.proceed(request);

//                Request request =  chain.request();
//                Response response = chain.proceed(request);
//   输出返回结果
//                try {
//                    Charset charset;
//                    charset = Charset.forName("UTF-8");
//                    ResponseBody responseBody = response.peekBody(Long.MAX_VALUE);
//                    Reader jsonReader = new InputStreamReader(responseBody.byteStream(), charset);
//                    BufferedReader reader = new BufferedReader(jsonReader);
//                    StringBuilder sbJson = new StringBuilder();
//                    String line = reader.readLine();
//                    do {
//                        sbJson.append(line);
//                        line = reader.readLine();
//                    } while (line != null);
//                    LogUtil.e("response: " + sbJson.toString());
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    LogUtil.e(e.getMessage(), e);
//                }
//// saveCookies(response, request.url().toString());
//                return response;
            }
        };


    }


    /**
     * 设置拦截器
     *
     * @return
     */
    private Interceptor getInterceptor() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        //显示日志
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        return interceptor;
    }

    public APIService getApi() {
        //初始化一个client,不然retrofit会自己默认添加一个
        OkHttpClient client = new OkHttpClient().newBuilder()
                .readTimeout(1, TimeUnit.MINUTES)
                .connectTimeout(1, TimeUnit.MINUTES)
                .writeTimeout(1, TimeUnit.MINUTES)
                //设置Header
                .addInterceptor(getHeaderInterceptor())
//                .addNetworkInterceptor(getHeaderInterceptor())
                //设置日志拦截器
                .addInterceptor(getInterceptor())
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                //设置网络请求的Url地址
                .baseUrl(baseUrl)
                //设置数据解析器
                .addConverterFactory(GsonConverterFactory.create())
                //设置网络请求适配器，使其支持RxJava与RxAndroid
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        //创建—— 网络请求接口—— 实例
        apiService = retrofit.create(APIService.class);
        return apiService;
    }


}
