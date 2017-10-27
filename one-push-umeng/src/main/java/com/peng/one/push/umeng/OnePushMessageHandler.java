package com.peng.one.push.umeng;

import android.app.Notification;
import android.content.Context;

import com.peng.one.push.OneRepeater;
import com.umeng.message.UmengMessageHandler;
import com.umeng.message.entity.UMessage;

/**
 * Created by pyt on 2017/6/12.
 */

public class OnePushMessageHandler extends UmengMessageHandler {

    @Override
    public void dealWithCustomMessage(Context context, UMessage uMessage) {
        OneRepeater.transmitMessage(context, uMessage.custom, null, uMessage.extra);
    }

    @Override
    public Notification getNotification(Context context, UMessage uMessage) {
        OneRepeater.transmitNotification(context, 0, uMessage.title, uMessage.text, null,
            uMessage.extra);
        return null;
    }
}
