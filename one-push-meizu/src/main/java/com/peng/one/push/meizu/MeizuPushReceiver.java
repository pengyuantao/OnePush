package com.peng.one.push.meizu;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import com.meizu.cloud.pushsdk.MzPushMessageReceiver;
import com.meizu.cloud.pushsdk.platform.message.PushSwitchStatus;
import com.meizu.cloud.pushsdk.platform.message.RegisterStatus;
import com.meizu.cloud.pushsdk.platform.message.SubAliasStatus;
import com.meizu.cloud.pushsdk.platform.message.SubTagsStatus;
import com.meizu.cloud.pushsdk.platform.message.UnRegisterStatus;
import com.peng.one.push.OnePush;
import com.peng.one.push.OneRepeater;
import com.peng.one.push.log.OneLog;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Created by pengyuantao on 2017/9/25 12:47.
 */

public class MeizuPushReceiver extends MzPushMessageReceiver {

  private static final String TAG = "MeizuPushReceiver";

  @Deprecated
  @Override
  public void onRegister(Context context, String pushId) {
  }

  @Deprecated
  @Override
  public void onUnRegister(Context context, boolean success) {
  }

  @Override
  public void onPushStatus(Context context, PushSwitchStatus pushSwitchStatus) {
    //OnePush不支持这个状态
  }

  @Override
  public void onMessage(Context context, Intent intent) {
    //flyme3.0的时候使用
    OneRepeater.transmitMessage(context, Intent2Json.toJson(intent), null, null);
  }

  @Override
  public void onMessage(Context context, String message) {
    //flyme4.0以上的时候使用
    OneRepeater.transmitMessage(context, message, null, null);
  }

  @Override
  public void onRegisterStatus(Context context, RegisterStatus registerStatus) {
    OneRepeater.transmitCommandResult(context, OnePush.TYPE_REGISTER,
        RegisterStatus.SUCCESS_CODE.equals(registerStatus.getCode()) ? OnePush.RESULT_OK
            : OnePush.RESULT_ERROR, registerStatus.getPushId(), null, registerStatus.getMessage());
    OneLog.i("Meizu push register result" + registerStatus.toString());
  }

  @Override
  public void onNotificationArrived(Context context, String title, String content,
      String selfDefineContentString) {
    OneRepeater.transmitNotification(context, 0, title, content, selfDefineContentString, null);
  }

  @Override
  public void onNotificationClicked(Context context, String title, String content,
      String selfDefineContentString) {
    OneRepeater.transmitNotificationClick(context, 0, title, content, selfDefineContentString,
        null);
  }

  @Override
  public void onUnRegisterStatus(Context context, UnRegisterStatus unRegisterStatus) {
    OneRepeater.transmitCommandResult(context, OnePush.TYPE_UNREGISTER,
        unRegisterStatus.isUnRegisterSuccess() ? OnePush.RESULT_OK : OnePush.RESULT_ERROR, null,
        null, unRegisterStatus.getMessage());
    OneLog.i("Meizu push unregister result" + unRegisterStatus.toString());
  }

  @Override
  public void onSubTagsStatus(Context context, SubTagsStatus subTagsStatus) {
    List<SubTagsStatus.Tag> tagList = TagAndAliasManager.getInstance().getTagList();
    boolean isAddAlias = tagList == null || subTagsStatus.getTagList().size()>=tagList.size()&&!tagList.isEmpty();
    TagAndAliasManager.getInstance().setTagList(subTagsStatus.getTagList());
    OneRepeater.transmitCommandResult(context,
        isAddAlias ? OnePush.TYPE_ADD_TAG : OnePush.TYPE_DEL_TAG, OnePush.RESULT_OK, null,
        subTagsStatus.getTagList().toString(), subTagsStatus.getMessage());
    OneLog.i("onSubTagsStatus: " + subTagsStatus.toString());
  }

  @Override
  public void onSubAliasStatus(Context context, SubAliasStatus subAliasStatus) {
    String alias = TagAndAliasManager.getInstance().getAlias();
    boolean isSetAlias = !alias.equals(subAliasStatus.getAlias()) || !TextUtils.isEmpty(alias);
    TagAndAliasManager.getInstance().setAlias(alias);
    OneRepeater.transmitCommandResult(context,
        isSetAlias ? OnePush.TYPE_BIND_ALIAS : OnePush.TYPE_UNBIND_ALIAS, OnePush.RESULT_OK, null,
        subAliasStatus.getAlias(), subAliasStatus.getMessage());
    OneLog.i("onSubAliasStatus: " + subAliasStatus.toString());
  }
}
