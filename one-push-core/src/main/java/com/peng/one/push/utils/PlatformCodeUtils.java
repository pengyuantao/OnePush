package com.peng.one.push.utils;

/**
 * get platform code
 * Created by Administer on 2018/2/6.
 */
public class PlatformCodeUtils {

    public static int getPlatformCodeByMetaName(String name) {
        if (name == null || name.isEmpty()) {
            return 0;
        }
        String codeStr = name.substring(name.length() - 3, name.length());
        try {
            return Integer.parseInt(codeStr);
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return 0;
        }
    }

}
