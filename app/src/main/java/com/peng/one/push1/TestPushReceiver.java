package com.peng.one.push1;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.peng.one.push.OnePush;
import com.peng.one.push.entity.OnePushCommand;
import com.peng.one.push.entity.OnePushMsg;
import com.peng.one.push.receiver.BaseOnePushReceiver;

/**
 * Created by pyt on 2017/5/16.
 */
public class TestPushReceiver extends BaseOnePushReceiver {

    private static final String TAG = "TestPushReceiver";

    public static final String LOG_LINE = "-------%s-------";

    @Override
    public void onReceiveNotification(Context context, OnePushMsg msg) {
        super.onReceiveNotification(context, msg);
        Log.i(TAG, "onReceiveNotification: " + msg.toString());
        MainActivity.sendLogBroadcast(context, generateLogByOnePushMsg("收到通知",msg));
    }

    @Override
    public void onReceiveNotificationClick(Context context, OnePushMsg msg) {
        Log.i(TAG, "onReceiveNotificationClick: " + msg.toString());
        MainActivity.start(context, generateLogByOnePushMsg("通知栏点击",msg));
    }

    @Override
    public void onReceiveMessage(Context context, OnePushMsg msg) {
        Log.i(TAG, "onReceiveMessage: " + msg.toString());
        MainActivity.sendLogBroadcast(context, generateLogByOnePushMsg("透传消息", msg));
    }

    @Override
    public void onCommandResult(Context context, OnePushCommand command) {
        //注册消息推送失败，再次注册
        if (command.getType() == OnePush.TYPE_REGISTER && command.getResultCode() == OnePush.RESULT_ERROR) {
            OnePush.register();
        }
        Log.i(TAG, "onCommandResult: " + command.toString());
        MainActivity.sendLogBroadcast(context, generateLogByOnePushCommand(command));
    }

    public String generateLogByOnePushMsg(String type, OnePushMsg onePushMsg) {
        StringBuilder builder = new StringBuilder();
        builder.append(String.format(LOG_LINE, type)).append("\n");
        if (onePushMsg.getMsg() != null) {
            builder.append("消息内容：" + onePushMsg.getMsg()).append("\n");
        } else {
            builder.append("通知标题：" + onePushMsg.getTitle()).append("\n");
            builder.append("通知内容：" + onePushMsg.getContent()).append("\n");
        }
        if (!TextUtils.isEmpty(onePushMsg.getExtraMsg())) {
            builder.append("额外信息：" + onePushMsg.getExtraMsg()).append("\n");
        }

        if (onePushMsg.getKeyValue() != null && !onePushMsg.getKeyValue().isEmpty()) {
            builder.append("键值对：").append(onePushMsg.getKeyValue().toString()).append("\n");
        }
        return builder.toString();
    }

    public String generateLogByOnePushCommand(OnePushCommand onePushCommand) {
        StringBuilder builder = new StringBuilder();
        String type = null;
        switch (onePushCommand.getType()) {
            case OnePushCommand.TYPE_ADD_TAG:
                type = "添加标签";
                break;
            case OnePushCommand.TYPE_DEL_TAG:
                type = "删除标签";
                break;
            case OnePushCommand.TYPE_BIND_ALIAS:
                type = "绑定别名";
                break;
            case OnePushCommand.TYPE_UNBIND_ALIAS:
                type = "解绑别名";
                break;
            case OnePushCommand.TYPE_REGISTER:
                type = "注册推送";
                break;
            case OnePushCommand.TYPE_UNREGISTER:
                type = "取消注册推送";
                break;
            case OnePushCommand.TYPE_AND_OR_DEL_TAG:
                type = "添加或删除标签";
                break;
            default:
                type = "未定义类型";
                break;
        }
        builder.append(String.format(LOG_LINE, type)).append("\n");
        if (!TextUtils.isEmpty(onePushCommand.getToken())) {
            builder.append("推送token：").append(onePushCommand.getToken()).append("\n");
        }
        if (!TextUtils.isEmpty(onePushCommand.getExtraMsg())) {
            builder.append("额外信息(tag/alias)：").append(onePushCommand.getExtraMsg()).append("\n");
        }
        builder.append("操作结果：").append(onePushCommand.getResultCode() == OnePushCommand.RESULT_OK ? "成功" : onePushCommand.getError());
        return builder.toString();
    }

}
