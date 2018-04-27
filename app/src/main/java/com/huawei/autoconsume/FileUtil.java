package com.huawei.autoconsume;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

import de.robv.android.xposed.XposedBridge;

public class FileUtil {

    public File logFile;

    private static FileUtil instance;

    private FileUtil(){
//        String path1 = "sdcard" + " /u2test/Uiautomation";
        String path1 = Environment.getExternalStorageDirectory() + " /u2test/Uiautomation";
        String path2 = path1 + "/consume.txt";
        File path = new File(path1);
        logFile = new File(path2);
        if (!path.exists()){
            path.mkdirs();
        }
        if (!logFile.exists()){
            try {
                path.createNewFile();
                XposedBridge.log("creat file success filePath=" + logFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                XposedBridge.log("creat file faile " + e.getMessage());
            }
        }

    }

    public static FileUtil getInstance(){
        if (instance == null){
            instance = new FileUtil();
        }
        return instance;
    }




}
