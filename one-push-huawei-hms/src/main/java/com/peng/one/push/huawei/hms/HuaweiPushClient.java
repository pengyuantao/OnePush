package com.peng.one.push.huawei.hms;

import android.content.Context;
import android.text.TextUtils;

import com.huawei.hms.api.ConnectionResult;
import com.huawei.hms.api.HuaweiApiClient;
import com.huawei.hms.support.api.client.ResultCallback;
import com.huawei.hms.support.api.push.HuaweiPush;
import com.huawei.hms.support.api.push.TokenResult;
import com.peng.one.push.cache.OnePushCache;
import com.peng.one.push.core.IPushClient;
import com.peng.one.push.log.OneLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by pyt on 2017/5/15.
 */

public class HuaweiPushClient implements IPushClient {

    private static final String TAG = "HuaweiPushClient";

    private Context mContext;
    private HuaweiApiClient mHuaweiApiClient;

    @Override
    public void initContext(Context context) {
        this.mContext = context.getApplicationContext();
    }

    @Override
    public void register() {

        mHuaweiApiClient = new HuaweiApiClient.Builder(mContext).addApi(HuaweiPush.PUSH_API)
                .addConnectionCallbacks(new HuaweiApiClient.ConnectionCallbacks() {
                    @Override
                    public void onConnected() {
                        //华为移动服务client连接成功，在这边处理业务自己的事件
                        OneLog.i("HuaweiApiClient 连接成功");
                        HuaweiPush.HuaweiPushApi.getToken(mHuaweiApiClient).setResultCallback(new ResultCallback<TokenResult>() {
                            @Override
                            public void onResult(TokenResult tokenResult) {
                                OneLog.i("token " + tokenResult.getTokenRes().getToken());
                            }
                        });
                    }

                    @Override
                    public void onConnectionSuspended(int i) {
                        if (mHuaweiApiClient != null) {
                            mHuaweiApiClient.connect();
                        }
                    }
                })
                .addOnConnectionFailedListener(new HuaweiApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(ConnectionResult connectionResult) {
                        OneLog.i("HuaweiApiClient连接失败，错误码：" + connectionResult.getErrorCode());
                        if (connectionResult.getErrorCode() == 2) {
                            // 系统的华为服务版本太低，可以继续使用个推
                        }
                    }
                }).build();
        mHuaweiApiClient.connect();

        new Thread(new Runnable() {
            @Override
            public void run() {
                HuaweiPush.HuaweiPushApi.enableReceiveNormalMsg(mHuaweiApiClient, true);
                HuaweiPush.HuaweiPushApi.enableReceiveNotifyMsg(mHuaweiApiClient, true);
            }
        }).start();
    }

    @Override
    public void unRegister() {

//        mHuaweiApiClient.disconnect();
        final String token = OnePushCache.getToken(mContext);
        if (!TextUtils.isEmpty(token)) {
            new Thread() {
                @Override
                public void run() {
                    super.run();
                    HuaweiPush.HuaweiPushApi.deleteToken(mHuaweiApiClient, token);
                    HuaweiPush.HuaweiPushApi.enableReceiveNormalMsg(mHuaweiApiClient, false);
                    HuaweiPush.HuaweiPushApi.enableReceiveNotifyMsg(mHuaweiApiClient, false);
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
        HashMap<String, String> tagsMap = new HashMap<>(1);
        tagsMap.put(tag, tag);
        HuaweiPush.HuaweiPushApi.setTags(mHuaweiApiClient, tagsMap);
    }

    @Override
    public void deleteTag(String tag) {
        if (TextUtils.isEmpty(tag)) {
            return;
        }
        List<String> list = new ArrayList<>();
        list.add(tag);
        HuaweiPush.HuaweiPushApi.deleteTags(mHuaweiApiClient, list);
    }
}
