package com.peng.one.push.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Set;


/**
 * Created by pengyt on 2018/2/4.
 */
public class MetaDataUtils {
    //the meta-data header
    private static final String META_DATA_PUSH_HEADER = "OnePush_";

    public static HashMap<String, String> getOnePushMetaData(Context context) {
        if (context == null) {
            return null;
        }
        Bundle metaData = null;
        try {
            LinkedHashMap<String, String> hashMap = new LinkedHashMap<>();
            metaData = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            if (metaData != null) {
                Set<String> allKeys = metaData.keySet();
                if (allKeys != null && !allKeys.isEmpty()) {
                    Iterator<String> iterator = allKeys.iterator();
                    while (iterator.hasNext()) {
                        String key = iterator.next();
                        if (key.startsWith(META_DATA_PUSH_HEADER)) {
                            hashMap.put(key, metaData.getString(key));
                        }
                    }
                }
            }
            return hashMap;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}
