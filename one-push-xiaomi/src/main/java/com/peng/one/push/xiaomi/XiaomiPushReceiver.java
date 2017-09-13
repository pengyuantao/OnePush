package com.peng.one.push.xiaomi;

import android.content.Context;
import android.util.Log;

import com.peng.one.push.OnePush;
import com.peng.one.push.OneRepeater;
import com.peng.one.push.cache.OnePushCache;
import com.peng.one.push.log.OneLog;
import com.xiaomi.mipush.sdk.ErrorCode;
import com.xiaomi.mipush.sdk.MiPushClient;
import com.xiaomi.mipush.sdk.MiPushCommandMessage;
import com.xiaomi.mipush.sdk.MiPushMessage;
import com.xiaomi.mipush.sdk.PushMessageReceiver;

import java.util.List;

/**
 * 1、PushMessageReceiver 是个抽象类，该类继承了 BroadcastReceiver。
 * 2、需要将自定义的 XiaoMiPushReceiver 注册在 AndroidManifest.xml 文件中：
 * 3、XiaoMiPushReceiver 的 onReceivePassThroughMessage 方法用来接收服务器向客户端发送的透传消息。
 * 4、XiaoMiPushReceiver 的 onNotificationMessageClicked 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法会在用户手动点击通知后触发。
 * 5、XiaoMiPushReceiver 的 onNotificationMessageArrived 方法用来接收服务器向客户端发送的通知消息，
 * 这个回调方法是在通知消息到达客户端时触发。另外应用在前台时不弹出通知的通知消息到达客户端也会触发这个回调函数。
 * 6、XiaoMiPushReceiver 的 onCommandResult 方法用来接收客户端向服务器发送命令后的响应结果。
 * 7、XiaoMiPushReceiver 的 onReceiveRegisterResult 方法用来接收客户端向服务器发送注册命令后的响应结果。
 * 8、以上这些方法运行在非 UI 线程中。
 *
 */
public class XiaomiPushReceiver extends PushMessageReceiver {

    private static final String TAG = "XiaomiPushReceiver";

    private String mRegId;
    private String mTopic;
    private String mAlias;
    private String mAccount;
    private String mStartTime;
    private String mEndTime;

    @Override
    public void onReceivePassThroughMessage(Context context, MiPushMessage message) {
        OneLog.i("onReceivePassThroughMessage() called with: context = [" + context + "], message = [" + message + "]");
        OneRepeater.transmitMessage(context, message.getContent(), message.getDescription(), message.getExtra());
    }

    @Override
    public void onNotificationMessageClicked(Context context, MiPushMessage message) {
        OneLog.i("onNotificationMessageClicked() called with: context = [" + context + "], message = [" + message + "]");
        // 这个动作可以在activity的onResume也能监听，请看第3点相关内容
        OneRepeater.transmitNotificationClick(context,message.getNotifyId(),message.getTitle(),message.getDescription(),message.getContent(), message.getExtra());
    }

    @Override
    public void onNotificationMessageArrived(Context context, MiPushMessage message) {
        OneLog.i("onNotificationMessageArrived() called with: context = [" + context + "], message = [" + message + "]");
        OneRepeater.transmitNotification(context,message.getNotifyId(),message.getTitle(),message.getDescription(),message.getContent(), message.getExtra());
    }

    @Override
    public void onCommandResult(Context context, MiPushCommandMessage message) {

        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        String cmdArg2 = ((arguments != null && arguments.size() > 1) ? arguments.get(1) : null);
        String log;

        int commandType = -1;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                log = context.getString(R.string.register_success);
            } else {
                log = context.getString(R.string.register_fail);
            }
        } else if (MiPushClient.COMMAND_SET_ALIAS.equals(command)) {
            commandType = OnePush.TYPE_BIND_ALIAS;
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
                log = context.getString(R.string.set_alias_success, mAlias);
            } else {
                log = context.getString(R.string.set_alias_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSET_ALIAS.equals(command)) {
            commandType = OnePush.TYPE_UNBIND_ALIAS;
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAlias = cmdArg1;
                log = context.getString(R.string.unset_alias_success, mAlias);
            } else {
                log = context.getString(R.string.unset_alias_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_SET_ACCOUNT.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAccount = cmdArg1;
                log = context.getString(R.string.set_account_success, mAccount);
            } else {
                log = context.getString(R.string.set_account_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSET_ACCOUNT.equals(command)) {


            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mAccount = cmdArg1;
                log = context.getString(R.string.unset_account_success, mAccount);
            } else {
                log = context.getString(R.string.unset_account_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_SUBSCRIBE_TOPIC.equals(command)) {
            commandType = OnePush.TYPE_ADD_TAG;
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
                log = context.getString(R.string.subscribe_topic_success, mTopic);
            } else {
                log = context.getString(R.string.subscribe_topic_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_UNSUBSCRIBE_TOPIC.equals(command)) {
            commandType = OnePush.TYPE_DEL_TAG;

            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mTopic = cmdArg1;
                log = context.getString(R.string.unsubscribe_topic_success, mTopic);
            } else {
                log = context.getString(R.string.unsubscribe_topic_fail, message.getReason());
            }
        } else if (MiPushClient.COMMAND_SET_ACCEPT_TIME.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mStartTime = cmdArg1;
                mEndTime = cmdArg2;
                log = context.getString(R.string.set_accept_time_success, mStartTime, mEndTime);
            } else {
                log = context.getString(R.string.set_accept_time_fail, message.getReason());
            }
        } else {
            log = message.getReason();
        }

        if (commandType != -1) {
            OneRepeater.transmitCommandResult(context, commandType,
                    message.getResultCode() == ErrorCode.SUCCESS ? OnePush.RESULT_OK : OnePush.RESULT_ERROR,
                    null, cmdArg1, message.getReason());
        }
        OneLog.i("onCommandResult is called. " + message.toString() + " reason:" + log);
    }

    @Override
    public void onReceiveRegisterResult(Context context, MiPushCommandMessage message) {

        String command = message.getCommand();
        List<String> arguments = message.getCommandArguments();
        String cmdArg1 = ((arguments != null && arguments.size() > 0) ? arguments.get(0) : null);
        OneRepeater.transmitCommandResult(context,OnePush.TYPE_REGISTER,
                MiPushClient.COMMAND_REGISTER.equals(command)?OnePush.RESULT_OK:OnePush.RESULT_ERROR,
                cmdArg1,null,message.getReason());
        //保存这个token
        OnePushCache.putToken(context, cmdArg1);
        String log;
        if (MiPushClient.COMMAND_REGISTER.equals(command)) {
            if (message.getResultCode() == ErrorCode.SUCCESS) {
                mRegId = cmdArg1;
                log = context.getString(R.string.register_success);
            } else {
                log = context.getString(R.string.register_fail) + cmdArg1;
            }
        } else {
            log = message.getReason();
        }
        OneLog.i("onReceiveRegisterResult is called. " + " reason:" + log);
    }


}
