package com.peng.one.push.core;

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

    private IPushClient mIPushState;

    private int mPlatformCode;

    private String mPlatformName;

    private Context mContext;

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

    public void init(Context context, OnOnePushRegisterListener listener) {
        this.mContext = context.getApplicationContext();
        try {
            //find all support push platform
            Bundle metaData = mContext.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
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
                        mIPushState = iPushClient;
                        this.mPlatformCode = platformCode;
                        this.mPlatformName = platformName;
                        //invoke IPushClient initContext method
                        OneLog.i(TAG, "current register platform is "+metaPlatformName);
                        iPushClient.initContext(mContext);
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

    }

    public void register() {
        mIPushState.register();
    }

    public void unRegister() {
        mIPushState.unRegister();
    }

    public void bindAlias(String alias) {
        mIPushState.bindAlias(alias);
    }

    public void unBindAlias(String alias) {
        mIPushState.unBindAlias(alias);
    }

    public void addTag(String tag) {
        mIPushState.addTag(tag);
    }

    public void deleteTag(String tag) {
        mIPushState.deleteTag(tag);
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
