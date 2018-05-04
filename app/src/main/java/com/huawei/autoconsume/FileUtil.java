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




    public static ConfigBeen getConfigBeen(){
        File file = new File("sdcard/TraverseConfig.txt");
        String configStr = getStrFromFile(file);
        ConfigBeen configBeen = new ConfigBeen();
        if (!TextUtils.isEmpty(configStr)){
            try {
                JSONObject jsonObject = new JSONObject(configStr);
                configBeen.setPackageName(jsonObject.getString("packageName"));
//                XposedBridge.log("解析得当前配置文件：" + configBeen.toString());
            } catch (JSONException e) {
                e.printStackTrace();
                XposedBridge.log(e.getMessage());
            }
        }else{
            configBeen.setPackageName("com.waakka.login");
//            XposedBridge.log("配置文件为空：" + configBeen.toString());
        }
        return configBeen;
    }


    public static File getLogFile(){
        String path1 = Environment.getExternalStorageDirectory() + "/u2test";
        String path2 = path1 + "/UiAutomation";
        String pathtxt = path2 + "/Consume.txt";
        File file1 = new File(path1);
        File file2 = new File(path2);
        File logFile = new File(pathtxt);
        if (!logFile.exists()){
            XposedBridge.log("文件不存在，创建文件夹及文件");
            if (!file1.exists()){
                file1.mkdirs();
                XposedBridge.log("创建文件夹成功 file1=" + file1.getAbsolutePath());
            }
            if (!file2.exists()){
                file2.mkdirs();
                XposedBridge.log("创建文件夹成功 file2=" + file2.getAbsolutePath());
            }
            try {
                logFile.createNewFile();
                XposedBridge.log("创建文件成功 logFile=" + logFile.getAbsolutePath());
            } catch (IOException e) {
                e.printStackTrace();
                XposedBridge.log("创建文件失败 " + e.getMessage());
            }
        }
        return logFile;
    }


    /**
     * 读取文件中string
     * @param file
     * @return
     */
    public static String getStrFromFile(File file){
        String content = "";
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
        return content;
    }




}
