package com.peng.one.push.getui;

import android.content.Context;
import android.content.SharedPreferences;

import com.igexin.sdk.PushConsts;
import com.igexin.sdk.PushManager;
import com.igexin.sdk.Tag;
import com.peng.one.push.OnePush;
import com.peng.one.push.OneRepeater;
import com.peng.one.push.core.IPushClient;
import com.peng.one.push.log.OneLog;

import java.util.HashSet;
import java.util.Set;

/**
 * 个推
 * Created by pyt on 2017/7/14.
 */
public class GeTuiPushClient implements IPushClient {

    //个推的标记
    public static final String FILE_GETUI_NAME = "getui_tags";

    //个推标签列表
    public static final String KEY_TAGS = "key_tags";

    //清空个推所有标签的替换标签
    public static final String TAG_ONE_PUSH_CLEAR = "one-push-clear";

    private Context context;

    //默认排重
    private Set<String> tags;


    @Override
    public void initContext(Context context) {
        this.context = context.getApplicationContext();
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(FILE_GETUI_NAME, Context.MODE_PRIVATE);
        tags = sharedPreferences.getStringSet(KEY_TAGS, new HashSet<String>());
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

    /**
     * 个推设置标签和删除标签说明
     * 咨询官方客服：
     * setTag()方法是一个全量覆盖的方法，
     * 后面一次的setTag(),后面会覆盖前面的setTag
     */

    @Override
    public void addTag(String tag) {
        this.tags.add(tag);
        setTag(true,tag);
        OneLog.i("addTag-->当前标签：" + this.tags);
    }

    @Override
    public void deleteTag(String tag) {
        this.tags.remove(tag);
        setTag(false,tag);
        OneLog.i("deleteTag-->当前标签：" + this.tags);
    }

    /**
     * 刷新本地tag
     */
    private void refreshLocalTag(){
        SharedPreferences sharedPreferences = this.context.getSharedPreferences(FILE_GETUI_NAME, Context.MODE_PRIVATE);
        sharedPreferences.edit().putStringSet(KEY_TAGS, tags).apply();
    }

    /**
     * 设置标签
     */
    private void setTag(boolean isAdd, String tag) {
        int size = tags.size();
        Tag[] tagArray = null;
        if (size == 0) {
            tagArray = new Tag[1];
            tagArray[0] = new Tag();
            tagArray[0].setName(TAG_ONE_PUSH_CLEAR);
        } else {
            tagArray = new Tag[size];
            String[] tagNameArray = new String[size];
            this.tags.toArray(tagNameArray);
            for (int i = 0; i < tagNameArray.length; i++) {
                tagArray[i] = new Tag();
                tagArray[i].setName(tagNameArray[i]);
            }
        }
        int i = PushManager.getInstance().setTag(context, tagArray, tag);
        sendAddTagEvent(isAdd,tag, i);
    }


    private void sendAddTagEvent(boolean isAdd, String tag, int eventType) {
        String text = null;
        switch (eventType) {
            case PushConsts.SETTAG_SUCCESS:
                refreshLocalTag();
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
        OneRepeater.transmitCommandResult(context, isAdd?OnePush.TYPE_ADD_TAG:OnePush.TYPE_DEL_TAG, eventType, null, tag, text);
    }

}
