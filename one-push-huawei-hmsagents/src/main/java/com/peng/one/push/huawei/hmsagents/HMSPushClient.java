package com.peng.one.push.huawei.hmsagents;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.text.TextUtils;

import com.huawei.android.hms.agent.HMSAgent;
import com.huawei.android.hms.agent.common.handler.ConnectHandler;
import com.huawei.android.hms.agent.push.handler.DeleteTokenHandler;
import com.huawei.android.hms.agent.push.handler.GetTokenHandler;
import com.peng.one.push.OnePush;
import com.peng.one.push.OneRepeater;
import com.peng.one.push.cache.OnePushCache;
import com.peng.one.push.core.IPushClient;
import com.peng.one.push.log.OneLog;

/**
 * HMS推送客户端
 * Created by pyt on 2017/5/15.
 */

public class HMSPushClient implements IPushClient {

    private static final String TAG = "HMSPushClient";
    private Context context;

    @Override
    public void initContext(Context context) {
        this.context = context.getApplicationContext();
        boolean isInit = HMSAgent.init((Application) context);
        OneLog.i("huawei-hmsagents initContext is " + isInit);
    }

    private void connect() {
        Activity activity = null;
        if (context instanceof Activity) {
            activity = (Activity) context;
        }

        HMSAgent.connect(activity, new ConnectHandler() {
            @Override
            public void onConnect(int rst) {
                OneLog.i("huawei-hmsagents connect onConnect=" + rst);
                if (rst == HMSAgent.AgentResultCode.HMSAGENT_SUCCESS) {
                    getToken();
                } else {
                    OneRepeater.transmitCommandResult(HMSPushClient.this.context, OnePush.TYPE_REGISTER,
                            OnePush.RESULT_ERROR, null, String.valueOf(rst), "huawei-hmsagents register error code : " + rst);
                }
            }
        });
    }

    private void getToken() {
        HMSAgent.Push.getToken(new GetTokenHandler() {
            @Override
            public void onResult(int rst) {
                OneLog.i("huawei-hmsagents getToken onResult=" + rst);
            }
        });
    }

    @Override
    public void register() {
        connect();
    }

    @Override
    public void unRegister() {
        final String token = OnePushCache.getToken(context);
        if (!TextUtils.isEmpty(token)) {
            HMSAgent.Push.deleteToken(token, new DeleteTokenHandler() {
                @Override
                public void onResult(int rst) {
                    OneLog.i("huawei-hmsagents deleteToken onResult=" + rst);
                }
            });
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

    /**
     * 注意：切换到HMS Push后，原来的setTag/getTag/deleteTag功能暂时不可用，
     * 老版本push sdk现在也无法上报新增标签，目前在华为开发者联盟portal上面仅支持根据存量的标签推送消息。
     * HMS SDK会在后续版本实现该功能。
     * */
    @Override
    public void addTag(String tag) {
//    if (TextUtils.isEmpty(tag)) {
//      return;
//    }
//    HMSAgent.Push.setTags(huaweiApiClient, Collections.singletonMap(tag, tag));
    }

    @Override
    public void deleteTag(String tag) {
//    if (TextUtils.isEmpty(tag)) {
//      return;
//    }
//    HuaweiPush.HuaweiPushApi.deleteTags(huaweiApiClient, Collections.singletonList(tag));
    }
}
