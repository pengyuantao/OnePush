package com.peng.one.push.jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import cn.jpush.android.api.JPushInterface;
import com.peng.one.push.OnePush;
import com.peng.one.push.OneRepeater;
import com.peng.one.push.log.OneLog;
import com.peng.one.push.utils.JsonUtils;
import java.util.Iterator;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pengyuantao on 2017/10/25 18:26.
 */

public class JPushReceiver extends BroadcastReceiver {

  @Override
  public void onReceive(Context context, Intent intent) {
    String action = intent.getAction();
    Bundle bundle = intent.getExtras();
    //防止下面的bundle为null
    if (bundle == null) {
      bundle = new Bundle();
    }
    if (JPushInterface.ACTION_REGISTRATION_ID.equals(action)) {
      String token =bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
      OneRepeater.transmitCommandResult(context, OnePush.TYPE_REGISTER,
              TextUtils.isEmpty(token)?OnePush.RESULT_ERROR:OnePush.RESULT_OK,
              token, null, null);
    } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(action)) {
      String title = bundle.getString(JPushInterface.EXTRA_TITLE);
      String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
      String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
      String msgId = bundle.getString(JPushInterface.EXTRA_MSG_ID);
      OneRepeater.transmitMessage(context, message, extra, null);
    } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(action)) {
      String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
      String content = bundle.getString(JPushInterface.EXTRA_ALERT);
      String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
      int notifyId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
      try {
        OneRepeater.transmitNotification(context, notifyId, title, content, null,
            JsonUtils.toMap(new JSONObject(extra)));
      } catch (JSONException localJSONException2) {
        localJSONException2.printStackTrace();
        OneRepeater.transmitNotification(context, notifyId, title, content, extra, null);
      }
    } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(action)) {
      String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
      String content = bundle.getString(JPushInterface.EXTRA_ALERT);
      String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
      int notifyId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
      try {
        OneRepeater.transmitNotificationClick(context, notifyId, title, content, null,
            JsonUtils.toMap(new JSONObject(extra)));
      } catch (JSONException localJSONException1) {
        localJSONException1.printStackTrace();
        OneRepeater.transmitNotificationClick(context, notifyId, title, content, extra, null);
      }
    } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(action)) {
      if (bundle.getBoolean(JPushInterface.EXTRA_CONNECTION_CHANGE)) {
        OneRepeater.transmitCommandResult(context, OnePush.TYPE_REGISTER, OnePush.RESULT_OK,
            JPushInterface.getRegistrationID(context), null, null);
      } else {
        OneRepeater.transmitCommandResult(context, OnePush.TYPE_UNREGISTER, OnePush.RESULT_OK,
            JPushInterface.getRegistrationID(context), null, null);
      }
    } else {
      OneLog.i(action + "----" + bundle.keySet());
    }
  }
}
