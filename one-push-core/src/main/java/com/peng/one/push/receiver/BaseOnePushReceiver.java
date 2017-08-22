package com.peng.one.push.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;

import com.peng.one.push.core.IPushReceiver;
import com.peng.one.push.core.OnePushCode;
import com.peng.one.push.entity.OnePushCommand;
import com.peng.one.push.entity.OnePushMsg;
import com.peng.one.push.log.OneLog;


/**
 * Finally unified message push processing Receiver
 * Created by pyt on 2017/5/10.
 */

public abstract class BaseOnePushReceiver extends BroadcastReceiver implements IPushReceiver, OnePushCode {

    @Override
    public final void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        Parcelable parcelable = TransmitDataManager.parsePushData(intent);
        if (OnePushAction.RECEIVE_COMMAND_RESULT.equals(action)) {
            onCommandResult(context, (OnePushCommand) parcelable);
        } else if (OnePushAction.RECEIVE_NOTIFICATION.equals(action)) {
            onReceiveNotification(context, (OnePushMsg) parcelable);
        } else if (OnePushAction.RECEIVE_NOTIFICATION_CLICK.equals(action)) {
            onReceiveNotificationClick(context,  (OnePushMsg) parcelable);
        } else if (OnePushAction.RECEIVE_MESSAGE.equals(action)) {
            onReceiveMessage(context, (OnePushMsg) parcelable);
        }
        OneLog.i(String.format("%s--%s", action, String.valueOf(parcelable)));
    }

    @Override
    public void onReceiveNotification(Context context, OnePushMsg msg) {
        //this is method is not always invoke,if you application is dead ,when you click
        //notification ,this method is not invoke,so don't do important things in this method
    }
}
