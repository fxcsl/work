package com.sinovatio.iesi.tools;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinovatio.iesi.R;

import static com.sinovatio.iesi.Myapplication.context;

public class ToastUtil  {

    public ToastUtil(Context context) {

    }

    public static void toast(String s){
        Toast.makeText(context,s,Toast.LENGTH_SHORT).show();
    }


    private static TextView mTextView;
    private static ImageView mImageView;

    public static void showToast(String message,int imag) {
        //加载Toast布局
        View toastRoot = LayoutInflater.from(context).inflate(R.layout.toast, null);
        //初始化布局控件
        mTextView = (TextView) toastRoot.findViewById(R.id.message);
        mImageView = (ImageView) toastRoot.findViewById(R.id.imageView);
        //为控件设置属性
        mTextView.setText(message);
        if(imag==0){
            mImageView.setVisibility(View.GONE);
        }else {
            mImageView.setImageResource(imag);
        }

        //Toast的初始化
        Toast toastStart = new Toast(context);
        //获取屏幕高度
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        int height = wm.getDefaultDisplay().getHeight();
        //Toast的Y坐标是屏幕高度的1/3，不会出现不适配的问题
        toastStart.setGravity(Gravity.TOP, 0, height / 4);
        toastStart.setDuration(Toast.LENGTH_SHORT);
        toastStart.setView(toastRoot);
        toastStart.show();
    }
}
