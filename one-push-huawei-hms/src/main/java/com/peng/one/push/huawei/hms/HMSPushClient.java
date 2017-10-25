package com.peng.one.push.huawei.hms;

import android.content.Context;
import android.text.TextUtils;
import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;
import com.peng.one.push.OnePush;
import com.peng.one.push.OneRepeater;
import com.peng.one.push.cache.OnePushCache;
import com.peng.one.push.core.IPushClient;
import com.peng.one.push.log.OneLog;
import java.util.Collections;

/**
 * HMS推送客户端
 * Created by pyt on 2017/5/15.
 */

public class HMSPushClient implements IPushClient {

    private static final String TAG = "HMSPushClient";

    private Context context;
    private HuaweiApiClient huaweiApiClient;

    @Override
    public void initContext(Context context) {
        this.context = context.getApplicationContext();
        huaweiApiClient = new HuaweiApiClient.Builder(context)
            .addApi(HuaweiPush.PUSH_API)
            .addConnectionCallbacks(new HuaweiApiClient.ConnectionCallbacks() {
                @Override
                public void onConnected() {
                    //华为移动服务client连接成功，在这边处理业务自己的事件
                    OneLog.i("HMS connect success!");
                    getToken();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            HuaweiPush.HuaweiPushApi.enableReceiveNormalMsg(huaweiApiClient, true);
                            HuaweiPush.HuaweiPushApi.enableReceiveNotifyMsg(huaweiApiClient, true);
                        }
                    }).start();
                }

                @Override
                public void onConnectionSuspended(int i) {
                    if (huaweiApiClient != null) {
                        huaweiApiClient.connect();
                    }
                    OneLog.i("HMS disconnect,retry.");
                }
            })
            .addOnConnectionFailedListener(new HuaweiApiClient.OnConnectionFailedListener() {
                @Override
                public void onConnectionFailed(ConnectionResult connectionResult) {
                    OneLog.i("HuaweiApiClient连接失败，错误码：" + connectionResult.getErrorCode());
                    OneRepeater.transmitCommandResult(HMSPushClient.this.context, OnePush.TYPE_REGISTER, OnePush.RESULT_ERROR,
                        null, String.valueOf(connectionResult.getErrorCode()), null);
                }
            })
            .build();
    }

    private void  getToken(){
        HuaweiPush.HuaweiPushApi.getToken(huaweiApiClient).setResultCallback(new ResultCallback<TokenResult>() {
            @Override
            public void onResult(TokenResult tokenResult) {
                OneLog.i("token " + tokenResult.getTokenRes().getToken());
                OneRepeater.transmitCommandResult(HMSPushClient.this.context,
                    OnePush.TYPE_REGISTER, OnePush.RESULT_OK,
                    tokenResult.getTokenRes().getToken(), null, null);
            }
        });
    }

    @Override
    public void register() {
        if (!huaweiApiClient.isConnected()) {
            huaweiApiClient.connect();
        }
        getToken();
    }

    @Override
    public void unRegister() {
//        huaweiApiClient.disconnect();
        final String token = OnePushCache.getToken(context);
        if (!TextUtils.isEmpty(token)) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    HuaweiPush.HuaweiPushApi.deleteToken(huaweiApiClient, token);
                    HuaweiPush.HuaweiPushApi.enableReceiveNormalMsg(huaweiApiClient, false);
                    HuaweiPush.HuaweiPushApi.enableReceiveNotifyMsg(huaweiApiClient, false);
                }
            }.start();
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
        HuaweiPush.HuaweiPushApi.setTags(huaweiApiClient, Collections.singletonMap(tag,tag));
    }

    @Override
    public void deleteTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        HuaweiPush.HuaweiPushApi.deleteTags(huaweiApiClient, Collections.singletonList(tag));
    }
}
