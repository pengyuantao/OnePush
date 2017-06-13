package com.peng.one.push.umeng;

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

}
