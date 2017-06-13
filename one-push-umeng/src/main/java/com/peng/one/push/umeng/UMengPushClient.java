package com.peng.one.push.umeng;

import android.content.Context;

import com.peng.one.push.OnePush;
import com.peng.one.push.OneRepeater;
import com.peng.one.push.core.IPushClient;
import com.umeng.message.IUmengCallback;
import com.umeng.message.IUmengRegisterCallback;
import com.umeng.message.PushAgent;
import com.umeng.message.UTrack;
import com.umeng.message.common.inter.ITagManager;
import com.umeng.message.tag.TagManager;

/**
 * umeng push client
 * Created by pyt on 2017/6/12.
 */

public class UMengPushClient implements IPushClient {

    public static final String ONE_PUSH_ALIAS = "ONE_PUSH";
    private Context context;
    private PushAgent mPushAgent;
    private String deviceToken;

    @Override
    public void initContext(Context context) {
        this.context = context.getApplicationContext();
        this.mPushAgent = PushAgent.getInstance(context);
        this.mPushAgent.setNotificationClickHandler(new OnePushNotificationClickHandler());
        this.mPushAgent.setMessageHandler(new OnePushMessageHandler());
    }

    @Override
    public void register() {
        new Thread() {
            @Override
            public void run() {
                //注册推送服务，每次调用register方法都会回调该接口(官方说明：可以创建一个子线程进行注册操作)
                mPushAgent.register(new IUmengRegisterCallback() {

                    @Override
                    public void onSuccess(String deviceToken) {
                        UMengPushClient.this.deviceToken = deviceToken;
                        //注册成功会返回device token
                        OneRepeater.transmitCommandResult(UMengPushClient.this.context, OnePush.TYPE_REGISTER, OnePush.RESULT_OK, deviceToken, null, "success");
                        mPushAgent.enable(null);
                    }

                    @Override
                    public void onFailure(String s, String s1) {
                        OneRepeater.transmitCommandResult(UMengPushClient.this.context, OnePush.TYPE_REGISTER, OnePush.RESULT_ERROR, null, s, s1);
                    }
                });
            }
        }.start();
    }

    @Override
    public void unRegister() {
        mPushAgent.disable(new IUmengCallback() {
            @Override
            public void onSuccess() {
                OneRepeater.transmitCommandResult(UMengPushClient.this.context, OnePush.TYPE_UNREGISTER, OnePush.RESULT_OK, UMengPushClient.this.deviceToken, null, null);
            }

            @Override
            public void onFailure(String s, String s1) {
                OneRepeater.transmitCommandResult(UMengPushClient.this.context, OnePush.TYPE_UNREGISTER, OnePush.RESULT_ERROR, UMengPushClient.this.deviceToken, null, null);

            }
        });
    }

    @Override
    public void bindAlias(final String alias) {
        mPushAgent.addAlias(alias, ONE_PUSH_ALIAS, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean isSuccess, String message) {
                OneRepeater.transmitCommandResult(UMengPushClient.this.context, OnePush.TYPE_BIND_ALIAS, isSuccess ? OnePush.RESULT_OK : OnePush.RESULT_ERROR, null, alias, message);
            }
        });
    }

    @Override
    public void unBindAlias(final String alias) {
        mPushAgent.removeAlias(alias, ONE_PUSH_ALIAS, new UTrack.ICallBack() {
            @Override
            public void onMessage(boolean isSuccess, String message) {
                OneRepeater.transmitCommandResult(UMengPushClient.this.context, OnePush.TYPE_UNBIND_ALIAS, isSuccess ? OnePush.RESULT_OK : OnePush.RESULT_ERROR, null, alias, message);

            }
        });
    }

    @Override
    public void addTag(final String tag) {
        mPushAgent.getTagManager().add(new TagManager.TCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
                //isSuccess表示操作是否成功
                OneRepeater.transmitCommandResult(UMengPushClient.this.context, OnePush.TYPE_ADD_TAG, isSuccess ? OnePush.RESULT_OK : OnePush.RESULT_ERROR, null, tag, result.errors);
            }
        }, tag);
    }

    @Override
    public void deleteTag(final String tag) {
        mPushAgent.getTagManager().delete(new TagManager.TCallBack() {
            @Override
            public void onMessage(final boolean isSuccess, final ITagManager.Result result) {
                //isSuccess表示操作是否成功
                OneRepeater.transmitCommandResult(UMengPushClient.this.context, OnePush.TYPE_DEL_TAG, isSuccess ? OnePush.RESULT_OK : OnePush.RESULT_ERROR, null, tag, result.errors);
            }
        }, tag);

    }
}
