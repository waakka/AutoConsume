package com.huawei.autoconsume;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

import de.robv.android.xposed.XposedBridge;

public class FileUtil {

    private File logFile;

    private static FileUtil instance;

    private FileUtil(){


    }

    public File getLogFile(){
        String path1 = "sdcard" + "/traverseTest";
//        String path1 = Environment.getExternalStorageDirectory() + "/traverseTest";
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
        return logFile;
    }

    public static FileUtil getInstance(){
        if (instance == null){
            instance = new FileUtil();
        }
        return instance;
    }




}
