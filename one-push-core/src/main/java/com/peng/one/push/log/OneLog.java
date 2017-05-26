package com.peng.one.push.log;

import android.util.Log;

/**
 * Created by pyt on 2017/5/18.
 */

public class OneLog {

    private static boolean sDebug = true;

    public static void i(String tag, String log) {
        if (sDebug) {
            Log.i(tag,log);
        }
    }

    public static void e(String tag, String log) {
        if (sDebug) {
            Log.e(tag,log);
        }
    }

    public static void setDebug(boolean isDebug) {
        sDebug = isDebug;
    }

}
