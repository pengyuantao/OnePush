package com.peng.one.push.service.log;

/**
 * log工具
 * Created by Administrator on 2017/5/17.
 */
public class OneLog {

    private static final String TAG = "OneLog";

    public static final String LOG_FORMAT = "%s---->%s";


    public static void i(String tag, String log) {
        System.out.println(String.format(LOG_FORMAT, tag, log));
    }

    public static void i(String log) {
        i(TAG, log);
    }

}
