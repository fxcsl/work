package com.sinovatio.mapp.utils;

import android.content.Context;

import com.sinovatio.mapp.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class WriterDBUtils {
    //这里的信息字段和外部db文件中信息保持一致。
    public static final String DB_NAME = "mapp.db";

    /**
     *利用文件流进行拷贝
     */
    public static void copyDBFromRaw(Context context) {
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            StringBuffer stringBuffer = new StringBuffer();
            stringBuffer.append("/data/data/");
            stringBuffer.append(context.getPackageName());
            stringBuffer.append("/databases");
            File dir=new File(stringBuffer.toString());
            if(!dir.exists()){//防止databases文件夹不存在，不然，会报ENOENT (No such file or directory)的异常
                dir.mkdirs();
            }
            stringBuffer.append("/");
            stringBuffer.append(DB_NAME);
            File file = new File(stringBuffer.toString());
            if (file == null || !file.exists()) {//数据库不存在，则进行拷贝数据库的操作。
                inputStream = context.getResources().openRawResource(R.raw.mapp);
                outputStream = new FileOutputStream(file.getAbsolutePath());
                byte[] b = new byte[1024];
                int length;
                while ((length = inputStream.read(b)) > 0) {
                    outputStream.write(b, 0, length);
                }
                //写完后刷新
                outputStream.flush();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {//关闭流，释放资源
                    inputStream.close();
                }
                if(outputStream!=null){
                    outputStream.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
