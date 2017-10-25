package com.peng.one.push.log;

import android.util.Log;

/**
 * Created by pyt on 2017/5/18.
 */

public class OneLog {

    private static boolean sDebug = true;

    public static final String TAG = "OneLog";

    public static void i(String log) {
        if (sDebug) {
            Log.i(TAG,log);
        }
    }

    public static void e( String log) {
        if (sDebug) {
            Log.e(TAG,log);
        }
    }

    public static void setDebug(boolean isDebug) {
        sDebug = isDebug;
    }

    public static boolean isDebug(){
        return sDebug;
    }

}
