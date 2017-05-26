package com.peng.one.push.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.peng.one.push.core.IPushReceiver;
import com.peng.one.push.core.OnePushCode;
import com.peng.one.push.entity.OnePushCommand;
import com.peng.one.push.entity.OnePushMsg;


/**
 * Finally unified message push processing Receiver
 * Created by pyt on 2017/5/10.
 */

public abstract class BaseOnePushReceiver extends BroadcastReceiver implements IPushReceiver, OnePushCode {

    @Override
    public final void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (OnePushAction.RECEIVE_COMMAND_RESULT.equals(action)) {
            OnePushCommand onePushCommand = TransmitDataManager.parsePushData(intent);
            onCommandResult(context, onePushCommand);
        } else if (OnePushAction.RECEIVE_NOTIFICATION.equals(action)) {
            OnePushMsg onePushMsg = TransmitDataManager.parsePushData(intent);
            onReceiveNotification(context, onePushMsg);
        } else if (OnePushAction.RECEIVE_NOTIFICATION_CLICK.equals(action)) {
            OnePushMsg onePushMsg = TransmitDataManager.parsePushData(intent);
            onReceiveNotificationClick(context, onePushMsg);
        } else if (OnePushAction.RECEIVE_MESSAGE.equals(action)) {
            OnePushMsg onePushMsg = TransmitDataManager.parsePushData(intent);
            onReceiveMessage(context, onePushMsg);
        }
    }

    @Override
    public void onReceiveNotification(Context context, OnePushMsg msg) {
        //this is method is not always invoke,if you application is dead ,when you click
        //notification ,this method is not invoke,so don't do important things in this method
    }
}
