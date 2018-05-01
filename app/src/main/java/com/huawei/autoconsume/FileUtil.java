package com.huawei.autoconsume;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

import de.robv.android.xposed.XposedBridge;

public class FileUtil {

    /**
     * 日志文件
     */
    private File logFile;
    /**
     * 被测应用包名
     */
    private String packageName = "";

    private static FileUtil instance;

    private FileUtil(){


    }

    /**
     * 解析traverseConfig文件获取被测应用包名
     * 每次创建日志文件时重新解析一次配置文件
     * @return
     */
    private String getPackageNameFromConfig(){
        String name = "";
        return name;
    }

    public String getPackageName(){
        return packageName;
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
                packageName = getPackageNameFromConfig();
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
