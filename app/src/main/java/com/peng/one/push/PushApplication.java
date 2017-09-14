package com.peng.one.push;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import java.util.List;

/**
 * 这个是自定义的Application
 * Created by pyt on 2017/5/16.
 */

public class PushApplication extends Application {

    private static final String TAG = "PushApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        String currentProcessName = getCurrentProcessName();
        //只在主进程中注册(注意：umeng推送，除了在主进程中注册，还需要在channel中注册)
        if (BuildConfig.APPLICATION_ID.equals(currentProcessName) || BuildConfig.APPLICATION_ID.concat(":channel").equals(currentProcessName)) {
            OnePush.init(this, ((platformCode, platformName) -> {
                //platformCode和platformName就是在<meta/>标签中，对应的"平台标识码"和平台名称
                boolean result = false;
                if (RomUtils.isMiuiRom()) {
                    result=  platformCode == 101;
                } else if (RomUtils.isHuaweiRom()) {
                    result= platformCode == 102;
                } else {
                    result= platformCode == 104;
                }
                Log.i(TAG, "Register-> code: "+platformCode+" name: "+platformName+" result: "+result);
                return result;
            }));
            OnePush.register();
        }
    }

    /**
     * 获取当前进程名称
     *
     * @return processName
     */
    public String getCurrentProcessName() {
        int currentProcessId = Process.myPid();
        ActivityManager activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            if (runningAppProcess.pid == currentProcessId) {
                return runningAppProcess.processName;
            }
        }
        return null;
    }

}
