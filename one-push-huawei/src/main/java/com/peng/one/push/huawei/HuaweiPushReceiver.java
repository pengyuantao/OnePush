package com.peng.one.push.huawei;


import android.content.Context;
import android.os.Bundle;

import com.huawei.android.pushagent.PushReceiver;
import com.peng.one.push.OnePush;
import com.peng.one.push.OneRepeater;
import com.peng.one.push.cache.OnePushCache;
import com.peng.one.push.log.OneLog;

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
    public void onPushMsg(Context context, byte[] bytes, String s) {
        super.onPushMsg(context, bytes, s);
        OneLog.i("onPushMsg() called with: context = [" + context + "], bytes = [" + bytes + "], s = [" + s + "]");
        OneRepeater.transmitMessage(context, new String(bytes, Charset.forName("UTF-8")), null, null);
    }

    @Override
    public void onEvent(Context context, Event event, Bundle bundle) {
        super.onEvent(context, event, bundle);

        if (event == Event.NOTIFICATION_CLICK_BTN) {//通知栏中的按钮被点击

        } else if(event == Event.NOTIFICATION_OPENED){//通知栏被打开（后台的发送通知必须包含键值对，否者该方法不会被调用）
            //将华为比较特别的keyValue的json方式进行转换(有点鸡肋)
            //注意：如果app被用户给清理掉，这个方法是不会被调用的，所以建议后台发送通知，以打开指令页面的方式，这样就可以有效的控制Click事件的处理

//            String pushMsg = bundle.getString("pushMsg");
//            try {
//                HashMap<String, String> map = new HashMap<>();
//                JSONArray jsonArray = new JSONArray(pushMsg);
//                for (int i = 0; i < jsonArray.length(); i++) {
//                    JSONObject jsonObject = (JSONObject) jsonArray.get(i);
//                    String key = jsonObject.keys().next();
//                    String value = jsonObject.getString(key);
//                    map.put(key, value);
//                }
                //不建议处理这NOTIFICATION_OPENED事件，如果服务端发送自定义的click通知，那么就会导致click事件转发两次
                //一次是这里，另外一次是在NotificationClickActivity()
//                OneRepeater.transmitNotificationClick(context, -1, null, null, null, map);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }

        }else if (event == Event.PLUGINRSP) {//标签上报返回
            OneRepeater.transmitCommandResult(context,OnePush.TYPE_AND_OR_DEL_TAG,bundle.getBoolean("isReportSuccess")?OnePush.RESULT_OK:OnePush.RESULT_ERROR,null,bundle.toString(),null);
        }

        //EMUI4.0 and EMUI5.0 is not use
        OneLog.i( "onEvent() called with: context = [" + context + "], event = [" + event + "], bundle = [" + bundle + "]");

    }
}
