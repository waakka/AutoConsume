package com.huawei.autoconsume;

import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;

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

    public static final String TAG = "autoConsume";

    /**
     * 日志文件
     */
    private File logFile;
    /**
     * 被测应用包名
     */
    public String packageName = "";

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
        File file = new File("sdcard/TraverseConfig.txt");
        String configStr = getStrFromFile(file);
        if (!TextUtils.isEmpty(configStr)){
            try {
                JSONObject jsonObject = new JSONObject(configStr);
                name = jsonObject.getString("packageName");
                XposedBridge.log("解析得当前被测包名为：" + name);
            } catch (JSONException e) {
                e.printStackTrace();
                XposedBridge.log(e.getMessage());
            }
        }else{
            XposedBridge.log("配置文件为空，暂时返回com.waakka.login");
            name = "com.waakka.login";
        }
        return name;
    }

    public String getPackageName(){
//        String path1 = "sdcard" + "/traverseTest";
//        String path1 = Environment.getExternalStorageDirectory() + "/u2test/UiAutomation";
        String path1 = Environment.getExternalStorageDirectory() + "/u2test";
        String path2 = path1 + "/UiAutomation";
        String pathtxt = path2 + "/Consume.txt";
        File file1 = new File(path1);
        File file2 = new File(path2);
        logFile = new File(pathtxt);
        if (!logFile.exists()){
            if (!file1.exists()){
                file1.mkdirs();
                XposedBridge.log("创建文件夹成功 file1=" + file1.getAbsolutePath());
            }
            if (!file2.exists()){
                file2.mkdirs();
                XposedBridge.log("创建文件夹成功 file2=" + file2.getAbsolutePath());
            }
            XposedBridge.log("文件不存在，创建文件并解析配置文件");
            try {
                logFile.createNewFile();
                XposedBridge.log("创建文件成功 logFile=" + logFile.getAbsolutePath() + "解析得包名=" + packageName);
            } catch (IOException e) {
                e.printStackTrace();
                XposedBridge.log("创建文件失败 " + e.getMessage());
            }
        }
        packageName = getPackageNameFromConfig();
        return TextUtils.isEmpty(packageName)?"com.waakka.login":packageName;
    }

    public File getLogFile(){
        String path1 = Environment.getExternalStorageDirectory() + "/u2test";
        String path2 = path1 + "/UiAutomation";
        String pathtxt = path2 + "/Consume.txt";
        File file1 = new File(path1);
        File file2 = new File(path2);
        logFile = new File(pathtxt);
        if (!logFile.exists()){
            if (!file1.exists()){
                file1.mkdirs();
                XposedBridge.log("创建文件夹成功 file1=" + file1.getAbsolutePath());
            }
            if (!file2.exists()){
                file2.mkdirs();
                XposedBridge.log("创建文件夹成功 file2=" + file2.getAbsolutePath());
            }
            XposedBridge.log("文件不存在，创建文件并解析配置文件");
            try {
                logFile.createNewFile();
                XposedBridge.log("创建文件成功 logFile=" + logFile.getAbsolutePath() + "解析得包名=" + packageName);
            } catch (IOException e) {
                e.printStackTrace();
                XposedBridge.log("创建文件失败 " + e.getMessage());
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

    /**
     * 读取文件中string
     * @param file
     * @return
     */
    private String getStrFromFile(File file){
        String content = "";
        Log.e(TAG,"配置文件路径：" + file.getAbsolutePath());
        if (file.exists()){
            String encouding = "UTF-8";
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
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                XposedBridge.log(e.getMessage());
                Log.e(TAG,e.getMessage());
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
                XposedBridge.log(e.getMessage());
                Log.e(TAG,e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
                XposedBridge.log(e.getMessage());
                Log.e(TAG,e.getMessage());
            } finally {
                if (reader != null){
                    try {
                        reader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        XposedBridge.log(e.getMessage());
                        Log.e(TAG,e.getMessage());
                    }
                    reader = null;
                }
                if (isr != null){
                    try {
                        isr.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        XposedBridge.log(e.getMessage());
                        Log.e(TAG,e.getMessage());
                    }
                    isr = null;
                }
                if (fis != null){
                    try {
                        fis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                        XposedBridge.log(e.getMessage());
                        Log.e(TAG,e.getMessage());
                    }
                    fis = null;
                }

            }
        }
        Log.e(TAG,"解析成功，返回配置文件详情" + content);
        return content;
    }




}
