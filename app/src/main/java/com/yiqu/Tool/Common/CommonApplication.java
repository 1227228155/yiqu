package com.yiqu.Tool.Common;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.view.Gravity;
import android.widget.Toast;

import com.yiqu.Tool.Function.CommonFunction;
import com.yiqu.Tool.Function.InitFunction;
import com.ui.App;

import cn.jpush.android.api.JPushInterface;

/**
 */
public class CommonApplication extends Application {

    /***
     * 前后台标记
     */
    public static boolean isActive;

    private boolean initialised;
    private boolean initialisedInUiThread;

    private Toast messageToast;
    private Toast longMessageToast;

    private static CommonApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.init(this);
        JPushInterface.setDebugMode(true);
        instance = this;

        if (CommonFunction.isEmpty(CommonFunction.GetPackageName())) {
            // 百度定位sdk定位服务或者类似启动remote service的第三方库运行在一个单独的进程
            // 每次定位服务启动的时候，都会调用application::onCreate,创建新的进程
            // 这个特殊处理是，如果从pid找不到对应的processInfo,processName，
            // 则此application::onCreate是被service调用的，直接返回
            return;
        }

        initialised = false;
        initialisedInUiThread = false;

        IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
        registerReceiver(myReceiver, filter );
    }

    public synchronized void initialise() {
        if (!initialised) {
            initialised = true;

            InitFunction.Initialise(this);

//            if (RecordConstant.Debug) {
//                StrictMode.ThreadPolicy.Builder threadPolicyBuilder =
//                        new StrictMode.ThreadPolicy.Builder().detectAll().penaltyLog();
//                StrictMode.VmPolicy.Builder vmPolicyBuilder =
//                        new StrictMode.VmPolicy.Builder().detectAll().penaltyLog();
//                StrictMode.setThreadPolicy(threadPolicyBuilder.build());
//                StrictMode.setVmPolicy(vmPolicyBuilder.build());
//            }
        }
    }

    public synchronized void initialiseInUIThread() {
        if (!initialisedInUiThread) {
            initialisedInUiThread = true;

            messageToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
            longMessageToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
        }
    }

    private BroadcastReceiver myReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {

            String action  = intent.getAction();
            if(Intent.ACTION_SCREEN_OFF.equals(action)){
                App.isActive = false;
            }
        }
    };

    @Override
    public void onTerminate() {

        try{
            unregisterReceiver(myReceiver);
        }catch(Exception e){
            e.printStackTrace();
        }
        super.onTerminate();
    }

    public static CommonApplication getInstance() {
        return instance;
    }

    public synchronized boolean isInitialised() {
        return initialised;
    }

    public void showToast(String text, String source) {
        showToast(text, source, false);
    }

    public void showToast(String text, String source, boolean debug) {
        if (CommonFunction.isEmpty(text)) {
            return;
        }

        if (messageToast == null) {
            messageToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        }

        messageToast.setText(text);
        messageToast.show();
    }

    public void showToast(String text, String source, boolean debug, boolean isLong) {
        showToast(text, source, debug, isLong, Gravity.BOTTOM);
    }

    public void showToast(String text, String source, boolean debug, boolean isLong, int gravity) {
        if (CommonFunction.isEmpty(text)) {
            return;
        }

        if (isLong) {
            if (longMessageToast == null) {
                longMessageToast = Toast.makeText(this, "", Toast.LENGTH_LONG);
            }

            longMessageToast.setText(text);
            longMessageToast.setGravity(gravity, 0, 0);
            longMessageToast.show();
        } else {
            if (messageToast == null) {
                messageToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
            }

            messageToast.setText(text);
            messageToast.setGravity(gravity, 0, 0);
            messageToast.show();
        }
    }
}