package com.sinovatio.mapp.utils;



import com.sinovatio.mapp.R;
import com.sinovatio.mapp.base.MyApplication;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FileConvert {
    public static void main(String[] args){
        //firstThing();
        //writeFile();
        //String DNS = "aaa/";
        //DNS = DNS.replaceAll("(^/)|(/$)","");
        //System.out.print("~~~" + DNS);
        //int a = 2122222222L;
        long b = 9223372036854774807L*3;
        System.out.println(b);
    }

    /**
     *  dealwith "oui.txt"
     */
    public static void firstThing(){
        try{
            FileReader fr = new FileReader(new File("D:/source/oui.txt"));
            BufferedReader br = new BufferedReader(fr);
            FileWriter fw = new FileWriter(new File("D:/source/new_oui.txt"));
            String str;
            String tempStr;
            while ((str = br.readLine()) != null) {
                if(str.equals("") || str.length() < 7){
                    continue;
                }
                tempStr = str.substring(0,6);
                if(regexTest(tempStr)){//检查某一行的前六位是否是mac，如果是则进行抽取
                    str = tempStr + " " + str.substring(22);//从第22位字符开始的是厂商名字，将6位的mac和厂商名字拼接起来
                    fw.write(str + "\n");
                }
            }
            br.close();
            fr.close();
            fw.flush();
            fw.close();
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public static boolean regexTest(String str){
        Pattern r = Pattern.compile("^[A-F0-9]{6}$");//mac的前六位由英文A~F及数字0-9构成
        Matcher m = r.matcher(str);
        return m.matches();
    }

    /**
     * 将new_oui.txt写入手机本地文件
     */
    public static void writeFile(){
        try {
            if(!(new File("/data/data/com.sinovatio.mapp/new_oui.txt").exists())){
                InputStream is = MyApplication.getContext().getResources().openRawResource(
                        R.raw.new_oui);
                FileOutputStream fos = new FileOutputStream("/data/data/com.sinovatio.mapp/new_oui.txt");
                byte[] buffer = new byte[400000];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.flush();
                fos.close();
                is.close();
            }

        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }


    public static void writeDbFileToDatabase(){
        try {

            if(!(new File("/data/data/com.sinovatio.mapp/databases/devicefactory.db").exists())){
                InputStream is = MyApplication.getContext().getResources().openRawResource(
                        R.raw.mapp);
                FileOutputStream fos = new FileOutputStream("/data/data/com.sinovatio" +
                        ".mapp/databases/mapp.db");
                byte[] buffer = new byte[400000];
                int count;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.flush();
                fos.close();
                is.close();
            }


        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }



}
