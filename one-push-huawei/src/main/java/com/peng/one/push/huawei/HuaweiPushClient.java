package com.peng.one.push.huawei;

import android.content.Context;
import android.text.TextUtils;

//import com.huawei.android.pushagent.api.PushManager;
import com.huawei.android.pushagent.PushManager;
import com.peng.one.push.cache.OnePushCache;
import com.peng.one.push.core.IPushClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

/**
 * Created by pyt on 2017/5/15.
 */

public class HuaweiPushClient implements IPushClient {

    private static final String TAG = "HuaweiPushClient";

    private Context mContext;

    @Override
    public void initContext(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public void register() {
        PushManager.requestToken(mContext);
        PushManager.enableReceiveNotifyMsg(mContext, true);
        PushManager.enableReceiveNormalMsg(mContext, true);
    }

    @Override
    public void unRegister() {
        //很奇怪的问题，就是在EMUI5.0上，就算调用取消注册token的方法，服务端任然能够通过token发送通知，fuck。
        //EMUI3.0 和EMUI4.0 没有手机测试不了
        String token = OnePushCache.getToken(mContext);
        if (!TextUtils.isEmpty(token)) {
            PushManager.deregisterToken(mContext, token);
            PushManager.enableReceiveNotifyMsg(mContext, false);
            PushManager.enableReceiveNormalMsg(mContext, false);
            PushManager.deleteTags(mContext, Arrays.asList(PushManager.getTags(mContext).keySet().toArray()));
            OnePushCache.delToken(mContext);
        }
    }

    @Override
    public void bindAlias(String alias) {
        //hua wei push is not support bind account
    }

    @Override
    public void unBindAlias(String alias) {
        //hua wei push is not support unbind account
    }

    @Override
    public void addTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        HashMap<String, String> tagsMap = new HashMap<>(1);
        tagsMap.put(tag, tag);
        PushManager.setTags(mContext, tagsMap);
    }

    @Override
    public void deleteTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        PushManager.deleteTags(mContext, Collections.singletonList(tag));
    }
}
