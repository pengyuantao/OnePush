package com.peng.one.push.xiaomi;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;

import com.peng.one.push.cache.OnePushCache;
import com.peng.one.push.core.IPushClient;
import com.xiaomi.mipush.sdk.MiPushClient;

/**
 * Created by pyt on 2017/5/15.
 */

public class XiaomiPushClient implements IPushClient {

    public static final String MI_PUSH_APP_ID = "MI_PUSH_APP_ID";
    public static final String MI_PUSH_APP_KEY = "MI_PUSH_APP_KEY";

    private String mAppId;
    private String mAppKey;
    private Context mContext;

    @Override
    public void initContext(Context context) {
        this.mContext = context.getApplicationContext();
        //读取小米对应的appId和appSecret
        try {
            Bundle metaData = context.getPackageManager().getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
            mAppId = metaData.getString(MI_PUSH_APP_ID);
            mAppKey = metaData.getString(MI_PUSH_APP_KEY);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void register() {
        if (TextUtils.isEmpty(mAppId) || TextUtils.isEmpty(mAppKey)) {
            throw new IllegalArgumentException("xiaomi push appId or appKey is not init," +
                    "check you AndroidManifest.xml is has MI_PUSH_APP_ID or MI_PUSH_APP_KEY meta-data flag please");
        }
        MiPushClient.registerPush(mContext, mAppId, mAppKey);
    }

    @Override
    public void unRegister() {
        String token = OnePushCache.getToken(mContext);
        if (!TextUtils.isEmpty(token)) {
            MiPushClient.unregisterPush(mContext);
            OnePushCache.delToken(mContext);
        }
    }

    @Override
    public void bindAlias(String alias) {
        MiPushClient.setAlias(mContext,alias,null);
    }

    @Override
    public void unBindAlias(String alias) {
        MiPushClient.unsetAlias(mContext, alias, null);
    }


    @Override
    public void addTag(String tag) {
        MiPushClient.subscribe(mContext,tag,null);
    }

    @Override
    public void deleteTag(String tag) {
        MiPushClient.unsubscribe(mContext, tag, null);
    }
}
