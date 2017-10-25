package com.peng.one.push.core;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import com.peng.one.push.log.OneLog;

import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * the one push context
 * <p>
 * Created by pyt on 2017/5/9.
 */

public class OnePushContext {

    private static final String TAG = "OnePushContext";
    //the meta-data header
    private static final String META_DATA_PUSH_HEADER = "OnePush_";
    //the meta_data split symbol
    public static final String METE_DATA_SPLIT_SYMBOL = "_";

    private IPushClient mIPushClient;

    private int mPlatformCode;

    private String mPlatformName;

    //all support push platform map
    private LinkedHashMap<String, String> mAllSupportPushPlatformMap = new LinkedHashMap<>();

    /**
     * Using the simple interest
     *
     * @return
     */
    public static OnePushContext getInstance() {
        return Single.sInstance;
    }

    private OnePushContext() {

    }

    public void init(Application application, OnOnePushRegisterListener listener) {
        Context context = application.getApplicationContext();
        try {
            //find all support push platform
            Bundle metaData = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            if (metaData != null) {
                Set<String> allKeys = metaData.keySet();
                if (allKeys != null && !allKeys.isEmpty()) {
                    Iterator<String> iterator = allKeys.iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        if (key.startsWith(META_DATA_PUSH_HEADER)) {
                            mAllSupportPushPlatformMap.put(key, metaData.getString(key));
                        }
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (mAllSupportPushPlatformMap.isEmpty()) {
            throw new IllegalArgumentException("have none push platform,check AndroidManifest.xml is have meta-data name is start with OnePush_");
        }

        //choose custom push platform
        Iterator<Map.Entry<String, String>> iterator = mAllSupportPushPlatformMap.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            String metaPlatformName = next.getKey();
            String metaPlatformClassName = next.getValue();
            StringBuilder stringBuilder = new StringBuilder(metaPlatformName).delete(0, 8);
            int len = stringBuilder.length();
            int lastIndexSymbol = stringBuilder.lastIndexOf(METE_DATA_SPLIT_SYMBOL);
            int platformCode = Integer.parseInt(stringBuilder.substring(lastIndexSymbol + 1, len));
            String platformName = stringBuilder.substring(0, lastIndexSymbol);
            try {
                Class<?> currentClz = Class.forName(metaPlatformClassName);
                Class<?>[] interfaces = currentClz.getInterfaces();
                List<Class<?>> allInterfaces = Arrays.asList(interfaces);
                if (allInterfaces.contains(IPushClient.class)) {
                    //create object with no params
                    IPushClient iPushClient = (IPushClient) currentClz.newInstance();
                    if (listener.onRegisterPush(platformCode, platformName)) {
                        this.mIPushClient = iPushClient;
                        this.mPlatformCode = platformCode;
                        this.mPlatformName = platformName;
                        //invoke IPushClient initContext method
                        OneLog.i("current register platform is "+metaPlatformName);
                        iPushClient.initContext(application);
                        break;
                    }
                } else {
                    throw new IllegalArgumentException(metaPlatformClassName + "is not implements " + IPushClient.class.getName());
                }

            } catch (ClassNotFoundException e) {
                throw new IllegalArgumentException("can not find class " + metaPlatformClassName);
            } catch (InstantiationException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        //clear cache client
        mAllSupportPushPlatformMap.clear();
        if (mIPushClient == null) {
            throw new IllegalStateException("onRegisterPush must at least one of them returns to true");
        }
    }

    public void register() {
        OneLog.i( String.format("%s--%s", getPushPlatFormName(), "register()"));
        mIPushClient.register();
    }

    public void unRegister() {
        OneLog.i( String.format("%s--%s", getPushPlatFormName(), "unRegister()"));
        mIPushClient.unRegister();
    }

    public void bindAlias(String alias) {
        OneLog.i( String.format("%s--%s", getPushPlatFormName(), "bindAlias("+alias+")"));
        mIPushClient.bindAlias(alias);
    }

    public void unBindAlias(String alias) {
        OneLog.i( String.format("%s--%s", getPushPlatFormName(), "unBindAlias("+alias+")"));
        mIPushClient.unBindAlias(alias);
    }

    public void addTag(String tag) {
        OneLog.i(String.format("%s--%s", getPushPlatFormName(), "addTag("+tag+")"));
        mIPushClient.addTag(tag);
    }

    public void deleteTag(String tag) {
        OneLog.i(String.format("%s--%s", getPushPlatFormName(), "deleteTag("+tag+")"));
        mIPushClient.deleteTag(tag);
    }

    public int getPushPlatFormCode() {
        return mPlatformCode;
    }

    public String getPushPlatFormName() {
        return mPlatformName;
    }

    private static class Single {
        static OnePushContext sInstance = new OnePushContext();
    }

}
