package com.peng.onepush.demo;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Process;
import android.util.Log;

import com.peng.demo.BuildConfig;
import com.peng.one.push.OnePush;
import com.peng.one.push.core.OnOnePushRegisterListener;

import java.util.List;

/**
 * Created by pyt on 2017/5/16.
 */

public class PushApplication extends Application {

    private static final String TAG = "PushApplication";

    @Override
    public void onCreate() {
        super.onCreate();
        //只在主进程中注册
        if (BuildConfig.APPLICATION_ID.equals(getCurrentProcessName())) {
            OnePush.init(this, new OnOnePushRegisterListener() {
                @Override
                public boolean onRegisterPush(int platformCode, String platformName) {
                    if (RomUtils.isHuaweiRom() && platformCode == 102) {
                        Log.i(TAG, "onRegisterPush: 华为推送");
                        return true;
                    } else if (platformCode == 101) {
                        Log.i(TAG, "onRegisterPush: 小米推送");
                        return true;
                    }
                    return false;
                }
            });
            OnePush.register();
        }
    }


    /**
     * 获取当前进程名称
     *
     * @return
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
