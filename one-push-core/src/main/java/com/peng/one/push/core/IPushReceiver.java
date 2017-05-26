package com.peng.one.push.core;

import android.content.Context;

import com.peng.one.push.entity.OnePushCommand;
import com.peng.one.push.entity.OnePushMsg;

/**
 * Created by pyt on 2017/3/23.
 */

public interface IPushReceiver {

    /**
     * When you received notice
     *
     * @param context
     * @param msg
     */
    void onReceiveNotification(Context context, OnePushMsg msg);

    /**
     * When you received the notice by clicking
     *
     * @param context
     * @param msg
     */
    void onReceiveNotificationClick(Context context, OnePushMsg msg);

    /**
     * When I received passthrough message
     *
     * @param context
     * @param msg
     */
    void onReceiveMessage(Context context, OnePushMsg msg);

    /**
     * When the client calls to execute the command, the callback
     * such as
     * @see IPushClient#addTag(String)
     * @see IPushClient#deleteTag(String)
     * @see IPushClient#bindAlias(String)
     * @see IPushClient#unBindAlias(String)
     * @see IPushClient#unRegister()
     * @see IPushClient#register()
     *
     * @param context
     * @param command
     */
    void onCommandResult(Context context, OnePushCommand command);

}
