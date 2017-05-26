package com.peng.one.push.core;

/**
 * Created by pyt on 2017/5/9.
 */

public interface OnOnePushRegisterListener {

    /**
     * When you start the use of registration
     *
     * the code and name is you defined meta-data in the AndroidManifest.xml
     * like OnePush_name_code
     * @param platformCode The code of the platform
     * @param platformName The name of the platform
     * @return
     */
    boolean onRegisterPush(int platformCode,String platformName);

}
