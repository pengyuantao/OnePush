package com.peng.one.push.utils;

import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * 系统标识
 * Created by pyt on 2017/3/27.
 */

public class RomUtils {

    private static final String TAG = "RomUtils";

    private static String getSystemProperty(String propName) {
        String line;
        BufferedReader input = null;
        try {
            Process p = Runtime.getRuntime().exec("getprop " + propName);
            input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);
            line = input.readLine();
            input.close();
        } catch (IOException ex) {
            Log.e(TAG, "Unable to read sysprop " + propName, ex);
            return null;
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    Log.e(TAG, "Exception while closing InputStream", e);
                }
            }
        }
        return line;
    }


    public static float getEMUIVersion(){
        String systemProperty = getSystemProperty("ro.build.version.emui");
        if (TextUtils.isEmpty(systemProperty)) {
            return 0;
        }
        String[] split = systemProperty.split("_");
        if (split.length == 2) {
            return Float.valueOf(split[1]);
        }
        return 0;
    }


    /**
     * 判断是否为华为UI
     */
    public static boolean isHuaweiRom() {
        String manufacturer = Build.MANUFACTURER;
        return !TextUtils.isEmpty(manufacturer) && manufacturer.contains("HUAWEI");
    }

    /**
     * 判断是否为小米UI
     */
    public static boolean isMiuiRom() {
        return !TextUtils.isEmpty(getSystemProperty("ro.miui.ui.version.name"));
    }


    /**
     *  判断当前是否为EMUI并支持HMS
     * @return
     */
    public static boolean isEmuiSupportHMS(){
        try {
            String systemProperty = getSystemProperty("ro.build.hw_emui_api_level");
            return !TextUtils.isEmpty(systemProperty) && Integer.valueOf(systemProperty) >= 9;
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     *
     * "ro.build.user" -> "flyme"
     * "persist.sys.use.flyme.icon" -> "true"
     * "ro.flyme.published" -> "true"
     * "ro.build.display.id" -> "Flyme OS 5.1.2.0U"
     * "ro.meizu.setupwizard.flyme" -> "true"
     *
     * 判断是否为魅族UI
     * @return
     */
    public static boolean isFlymeRom(){
        return "flyme".equalsIgnoreCase(getSystemProperty("ro.build.user"));
    }

}
