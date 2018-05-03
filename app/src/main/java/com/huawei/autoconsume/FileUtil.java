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
        File file = new File("sdcard/TraverseConfig.txt");
        String configStr = getStrFromFile(file);
        if (!TextUtils.isEmpty(configStr)){
            try {
                JSONObject jsonObject = new JSONObject(configStr);
                name = jsonObject.getString("packageName");
                XposedBridge.log("解析得当前被测包名为：" + name);
                Log.e(TAG,"解析得当前被测包名为：" + name);
            } catch (JSONException e) {
                e.printStackTrace();
                XposedBridge.log(e.getMessage());
                Log.e(TAG,e.getMessage());
            }
        }
        return name;
    }

    public String getPackageName(){
        return packageName;
    }

    public File getLogFile(){
        String path1 = "sdcard" + "/u2test/UiAutomation";
//        String path1 = Environment.getExternalStorageDirectory() + "/u2test/UiAutomation";
        String path2 = path1 + "/Consume.txt";
        File path = new File(path1);
        logFile = new File(path2);
        if (!path.exists()){
            path.mkdirs();
        }
        Log.e(TAG,"日志文件另路径：" + logFile.getAbsolutePath());
        if (!logFile.exists()){
            try {
                path.createNewFile();
                packageName = getPackageNameFromConfig();
                XposedBridge.log("creat file success filePath=" + logFile.getAbsolutePath());
                Log.e(TAG,"creat file success filePath=" + logFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                XposedBridge.log("creat file faile " + e.getMessage());
                Log.e(TAG,"creat file faile " + e.getMessage());
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
