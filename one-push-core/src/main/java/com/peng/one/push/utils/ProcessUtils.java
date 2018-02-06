package com.peng.one.push.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Process;

import java.util.List;

/**
 * Created by pengyt on 2018/2/4.
 */

public class ProcessUtils {

    /**
     * 获取当前进程名称
     *
     * @return processName
     */
    public String getCurrentProcessName(Context context) {
        int currentProcessId = Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo runningAppProcess : runningAppProcesses) {
            if (runningAppProcess.pid == currentProcessId) {
                return runningAppProcess.processName;
            }
        }
        return null;
    }
}
