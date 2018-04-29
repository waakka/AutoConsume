package com.huawei.autoconsume;

import android.content.Context;

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
        BufferedWriter bw = null;

        try {
            fos = new FileOutputStream(FileUtil.getInstance().getLogFile(),true);
            bw = new BufferedWriter(new OutputStreamWriter(fos));
            bw.write(msg);
            bw.flush();
            XposedBridge.log("write file success ");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            XposedBridge.log("FileNotFoundException " + e.getMessage());
        } catch (IOException e) {
            e.printStackTrace();
            XposedBridge.log("IOException " + e.getMessage());
        } finally {
            try {
                if (bw != null)
                    bw.close();
                if (fos != null)
                    fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
