package com.huawei.autoconsume;

import android.app.Activity;
import android.content.res.Resources;
import android.view.MotionEvent;
import android.view.View;

import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class MainTest implements IXposedHookLoadPackage {



    public static final int TYPE_NORMAL = 0;
    public static final int TYPE_LOGIN = 1;

    private String packageName;

    private long lastClickTime = -1;
    private String lastActivityName = "";

    private String curActivityName;
    private long curTime;
    /**
     * 常规界面或登录界面
     * 0：常规界面
     * 1：登录界面
     */
    private int type;


    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {

        if (!lpparam.packageName.equalsIgnoreCase(FileUtil.getInstance().getPackageName())){
            XposedBridge.log("包名不符，配置包名："+ FileUtil.getInstance().getPackageName() +"当前包名："+lpparam.packageName);
            return;
        }else{
            XposedBridge.log("包名符合，开始hook。配置包名："+ FileUtil.getInstance().getPackageName() +"当前包名："+lpparam.packageName);
        }

        /**
         * 暂时kook view的dispatchTouchEvent
         * 正式hook traverTest的deviceClick
         */
        findAndHookMethod("android.view.View",lpparam.classLoader,
                "dispatchTouchEvent", MotionEvent.class,new XC_MethodHook(){
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        MotionEvent event = (MotionEvent) param.args[0];
                        View view = (View) param.thisObject;
                        int id = view.getId();
                        Resources resources = view.getResources();
                        String resourceName = resources.getResourceName(id);
                        XposedBridge.log("本次操作控件的resourceName = " + resourceName);
                        //TODO 增加resoureName的判断，如果符合解析的登录button则本次操作为登录操作
                        int action = event.getAction();
                        switch(action) {
                            case MotionEvent.ACTION_UP:
                                lastClickTime = System.currentTimeMillis();
                                type = TYPE_NORMAL;
                                break;
                        }
                    }
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    }
                });




        /**
         * 界面绘制结束hook节点
         */
        findAndHookMethod("android.app.Activity",lpparam.classLoader,
                "onWindowFocusChanged", boolean.class,new XC_MethodHook(){
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        boolean hasFocus = (boolean) param.args[0];
                        Activity activity = (Activity) param.thisObject;
//                        XposedBridge.log("activityName = " + activity.getClass().getSimpleName());
                        if(hasFocus){
                            XposedBridge.log(ConsumeUtil.getCurTimeStr()
                                    + "****************"+activity.getClass().getName()+"获得焦点 ");
                            // 记录当前时间
                            curTime = System.currentTimeMillis();
                            // 记录当前ActivityName
                            curActivityName = activity.getClass().getName();
                            // 判断当前失去焦点的activity是否是lastActivityName
                            if (!curActivityName.equals(lastActivityName)){
                                // 不是则说明发生跳转,生成Consume类并打印
                                if (lastClickTime == -1){
                                    //当前新界面不是通过deviceClick方法触发新界面
                                    //adb启动acctivity或app内部自动跳转或back、home等造成
                                    //暂不处理
                                    Consume consume = new Consume();
                                    // 起始时间及起始activityName暂不记录
                                    consume.setStartActivity("");
                                    consume.setStartTime(0);
                                    consume.setStoptActivity(curActivityName);
                                    consume.setStopTime(curTime);
                                    consume.setType(-1);
                                    ConsumeUtil.showLog(consume.toString());
                                }else{
                                    Consume consume = new Consume();
                                    consume.setStartActivity(lastActivityName);
                                    consume.setStartTime(lastClickTime);
                                    consume.setStoptActivity(curActivityName);
                                    consume.setStopTime(curTime);
                                    consume.setType(type);
                                    ConsumeUtil.showLog(consume.toString());
                                }
                                lastActivityName = curActivityName;
                            }
                            //归零点击记录
                            lastClickTime = -1;
                        }else{
                            XposedBridge.log(ConsumeUtil.getCurTimeStr()
                                    + "****************"+activity.getClass().getName()+"丢失焦点 ");
                        }
                    }
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    }
                });


    }
}
