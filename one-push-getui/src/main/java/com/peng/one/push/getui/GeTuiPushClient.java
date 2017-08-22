package com.peng.one.push.getui;

import android.content.Context;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;
import com.peng.one.push.OnePush;
import com.peng.one.push.OneRepeater;
import com.peng.one.push.core.IPushClient;

/**
 * 个推
 * Created by pyt on 2017/7/14.
 */
public class GeTuiPushClient implements IPushClient {

    private Context context;

    @Override
    public void initContext(Context context) {
        this.context = context.getApplicationContext();
    }

    @Override
    public void register() {
        PushManager.getInstance().initialize(this.context, GeTuiPushService.class);
        // com.getui.demo.DemoIntentService 为第三方自定义的推送服务事件接收类
        PushManager.getInstance().registerPushIntentService(this.context, GeTuiReceiverService.class);
        PushManager.getInstance().turnOnPush(context);
    }

    @Override
    public void unRegister() {
        PushManager.getInstance().turnOffPush(context);
    }

    @Override
    public void bindAlias(String alias) {
        boolean isSuccess = PushManager.getInstance().bindAlias(context, alias);
        OneRepeater.transmitCommandResult(context, OnePush.TYPE_BIND_ALIAS,
                isSuccess ? OnePush.RESULT_OK : OnePush.RESULT_ERROR, null, alias,
                isSuccess?context.getString(R.string.bind_alias_success): context.getString(R.string.bind_alias_fail));
    }

    @Override
    public void unBindAlias(String alias) {
        boolean isSuccess = PushManager.getInstance().unBindAlias(context, alias, true);
        OneRepeater.transmitCommandResult(context, OnePush.TYPE_UNBIND_ALIAS,
                isSuccess ? OnePush.RESULT_OK : OnePush.RESULT_ERROR, null, alias,
                isSuccess?context.getString(R.string.unbind_alias_success): context.getString(R.string.unbind_alias_fail));
    }

    @Override
    public void addTag(String tag) {
        Tag tag1 = new Tag();
        tag1.setName(tag);
        int i = PushManager.getInstance().setTag(context, new Tag[]{tag1}, tag);
        sendAddTagEvent(tag,i);
    }

    @Override
    public void deleteTag(String tag) {
    }


    private void sendAddTagEvent(String tag,int eventType) {
        String text = null;
        switch (eventType) {
            case PushConsts.SETTAG_SUCCESS:
                text = "设置标签成功";
                break;

            case PushConsts.SETTAG_ERROR_COUNT:
                text = "设置标签失败, tag数量过大, 最大不能超过200个";
                break;

            case PushConsts.SETTAG_ERROR_FREQUENCY:
                text = "设置标签失败, 频率过快, 两次间隔应大于1s";
                break;

            case PushConsts.SETTAG_ERROR_REPEAT:
                text = "设置标签失败, 标签重复";
                break;

            case PushConsts.SETTAG_ERROR_UNBIND:
                text = "设置标签失败, 服务未初始化成功";
                break;

            case PushConsts.SETTAG_ERROR_EXCEPTION:
                text = "设置标签失败, 未知异常";
                break;

            case PushConsts.SETTAG_ERROR_NULL:
                text = "设置标签失败, tag 为空";
                break;

            case PushConsts.SETTAG_NOTONLINE:
                text = "还未登陆成功";
                break;

            case PushConsts.SETTAG_IN_BLACKLIST:
                text = "该应用已经在黑名单中,请联系售后支持!";
                break;

            case PushConsts.SETTAG_NUM_EXCEED:
                text = "已存 tag 超过限制";
                break;

            default:
                break;
        }
        OneRepeater.transmitCommandResult(context, OnePush.TYPE_ADD_TAG, eventType, null, tag, text);
    }

}
