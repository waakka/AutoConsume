package com.huawei.autoconsume;

import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import de.robv.android.xposed.XposedBridge;

public class ConsumeUtil {

    public static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss SSS");


    public static String getTimeStr(long ms){
        if (ms == -1){
            return "点击时间未知";
        }
        return sdf.format(new Date(ms));
    }

    public static String getCurTimeStr(){
        return sdf.format(new Date());
    }


    public static void showLog(String msg){
        XposedBridge.log(msg);
        write(msg);
    }

    public static void write(String msg){
        FileOutputStream fos = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;


        try {
            fos = new FileOutputStream(FileUtil.getInstance().logFile.getAbsoluteFile());
            osw = new OutputStreamWriter(fos,"utf-8");
            bw = new BufferedWriter(osw);
            bw.write(msg);
        } catch (FileNotFoundException e) {
            XposedBridge.log("creat IOfile faile:" + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
            XposedBridge.log("write faile" + e.getMessage());
        }finally {
//            try {
//                bw.close();
//                osw.close();
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//                XposedBridge.log("close faile" + e.getMessage());
//            }
        }
    }


}
