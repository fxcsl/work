package com.sinovatio.iesi.tools;

import android.app.Activity;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.sinovatio.iesi.BaseActivity;
import com.sinovatio.iesi.BaseFragment;
import com.sinovatio.iesi.R;

/**
 * glide 图片加载缓存工具
 * 2019-07-17 zjp
 */
public class GlideUtil {

    /**
     * 目标档案头像样式
     */
    public static RequestOptions options = new RequestOptions()
            .placeholder(R.mipmap.target_photo)//占位图/默认图片
            .fitCenter() //放大缩小至view大小
            .circleCrop();//剪切圆形
    //         .error(R.drawable.error)//加载失败 占位图
    //  .override(200, 100) //设置加载图片大小
    // override(Target.SIZE_ORIGINAL)加载原始大小
    // .skipMemoryCache(true);//禁用内存缓存
    // .diskCacheStrategy(DiskCacheStrategy.NONE);//禁用硬盘缓存  默认自动缓存


    /**
     * @param mContext
     * @param url
     * @param view
     * @param options
     */
    public static void viewHttpPic_activity(Context mContext, String url, ImageView view,RequestOptions options){

        Glide.with(mContext)//建议传入 activity 或者fragment 对象  可以使图片加载和活动生命周期同步
                .load(url)//图片地址 也可以是gif等动图
                .error(ContextCompat.getDrawable(mContext,R.mipmap.target_photo))
                .apply(options)//设置占位图//
                .into(view);//显示目标view
        //.into(simpleTarget);//图片加载监听
    }
}
