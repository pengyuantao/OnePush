package com.peng.one.push;

import android.content.Context;

import com.peng.one.push.core.OnOnePushRegisterListener;
import com.peng.one.push.core.OnePushCode;
import com.peng.one.push.core.OnePushContext;
import com.peng.one.push.log.OneLog;

/**
 * message push access class
 * Created by pyt on 2017/5/9.
 */

public class OnePush implements OnePushCode{

    /**
     *Initialize One Push
     * @param context
     * @param listener
     */
    public static void init(Context context,OnOnePushRegisterListener listener){
        OnePushContext.getInstance().init(context, listener);
    }

    /**
     * Registered push
     */
    public static void register(){
        OnePushContext.getInstance().register();
    }

    /**
     * Cancel the registration of push
     */
    public static void unRegister() {
        OnePushContext.getInstance().unRegister();
    }

    /**
     * Binding alias
     * @param alias alias
     */
    public static void bindAlias(String alias){
        OnePushContext.getInstance().bindAlias(alias);
    }

    /**
     * Unbundling account
     * @param alias alias
     */
    public static void unBindAlias(String alias) {
        OnePushContext.getInstance().unBindAlias(alias);
    }

    /**
     * Add the tag
     * @param tag Tag
     */
    public static void addTag(String tag) {
        OnePushContext.getInstance().addTag(tag);
    }

    /**
     * Delete the tag
     * @param tag Tag
     */
    public static void deleteTag(String tag) {
        OnePushContext.getInstance().deleteTag(tag);
    }

    /**
     * Gets the current push platform type
     * @return
     */
    public static int getPushPlatFormCode(){
        return OnePushContext.getInstance().getPushPlatFormCode();
    }

    /**
     * Gets the current push platform name
     * @return
     */
    public static String getPushPlatFormName(){
        return OnePushContext.getInstance().getPushPlatFormName();
    }

    /**
     * current debug statue
     * @param isDebug
     */
    public static void setDebug(boolean isDebug) {
        OneLog.setDebug(isDebug);
    }


}
