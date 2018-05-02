package com.huawei.autoconsume;

import android.os.Environment;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

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
        File file = new File("sdcard/traverseConfig.txt");
        if (file.exists()){
            String encouding = "UTF-8";
            String content = "";
            BufferedReader reader = null;
            InputStreamReader isr = null;
            FileInputStream fis = null;
            String line = "";
            try {
                fis = new FileInputStream(file);
                isr = new InputStreamReader(fis,encouding);
                reader = new BufferedReader(isr);
                while ((line = reader.readLine()) != null){
                    content += line;
                }
                JSONObject jsonObject = new JSONObject(content);
                name = jsonObject.getString("packageName");
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            } finally {
                if (reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    reader = null;
                }
                if (isr != null){
                    try {
                        isr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    isr = null;
                }
                if (fis != null){
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    fis = null;
                }

            }
        }
        return name;
    }

    public String getPackageName(){
        return packageName;
    }

    public File getLogFile(){
        String path1 = "sdcard" + "/u2test/UiAutomation";
//        String path1 = Environment.getExternalStorageDirectory() + "/traverseTest";
        String path2 = path1 + "/Consume.txt";
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
