package com.peng.one.push.huawei.hms;


import android.content.Context;
import android.os.Bundle;

import com.huawei.hms.support.api.push.PushReceiver;
import com.peng.one.push.OnePush;
import com.peng.one.push.OneRepeater;
import com.peng.one.push.cache.OnePushCache;
import com.peng.one.push.log.OneLog;

import org.json.JSONArray;

import java.nio.charset.Charset;

/**
 *
 * Created by pyt on 2017/5/15.
 */

public class HuaweiPushReceiver extends PushReceiver {

    private static final String TAG = "HuaweiPushReceiver";

    @Override
    public void onToken(Context context, String token,Bundle bundle) {
        super.onToken(context, token);
        OneLog.i( "onToken() called with: context = [" + context + "], token = [" + token + "], bundle = [" + bundle + "]");
        //save token when you call unregister method
        OnePushCache.putToken(context, token);
        OneRepeater.transmitCommandResult(context, OnePush.TYPE_REGISTER,OnePush.RESULT_OK,token,null,null);
    }

    @Override
    public void onPushState(Context context, boolean b) {
        super.onPushState(context, b);
    }

    @Override
    public void onToken(Context context, String s) {
        super.onToken(context, s);
    }

    @Override
    public void onPushMsg(Context context, byte[] bytes, String s) {
        super.onPushMsg(context, bytes, s);
        OneLog.i("onPushMsg() called with: context = [" + context + "], bytes = [" + bytes + "], s = [" + s + "]");
        OneRepeater.transmitMessage(context, new String(bytes, Charset.forName("UTF-8")), null, null);
    }

    @Override
    public void onEvent(Context context, Event event, Bundle bundle) {
        super.onEvent(context, event, bundle);

        if (event == Event.NOTIFICATION_CLICK_BTN) {//通知栏中的按钮被点击

        } else if(event == Event.NOTIFICATION_OPENED) {//通知栏被打开（后台的发送通知必须包含键值对，否者该方法不会被调用）

            //EMUI4.0 and EMUI5.0 is not use
            OneLog.e("onEvent() called with: context = [" + context + "], event = [" + event + "], bundle = [" + bundle + "]");
            if (bundle != null) {
                try {
                    String msg = new JSONArray(bundle.getString("pushMsg")).getJSONObject(0).getString("message");
//                OneRepeater.transmitMessage(context, msg, null, null);
                    OneRepeater.transmitNotificationClick(context, 0, null, null, msg, null);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
