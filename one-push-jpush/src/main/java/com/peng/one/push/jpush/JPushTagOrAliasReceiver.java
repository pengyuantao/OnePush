package com.peng.one.push.jpush;

import android.content.Context;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;
import com.peng.one.push.OnePush;
import com.peng.one.push.OneRepeater;
import java.util.Set;

/**
 * Created by pengyuantao on 2017/10/25 18:55.
 */

public class JPushTagOrAliasReceiver extends JPushMessageReceiver {

  @Override
  public void onAliasOperatorResult(Context context, JPushMessage jPushMessage) {
    OneRepeater.transmitCommandResult(context, jPushMessage.getSequence(), OnePush.RESULT_OK, null, jPushMessage.getAlias(), null);
  }

  @Override
  public void onTagOperatorResult(Context context, JPushMessage jPushMessage) {
    Set<String> tagSet = jPushMessage.getTags();
    String tag = null;
    if (!tagSet.isEmpty()){
      tag = tagSet.iterator().next();
    }
    OneRepeater.transmitCommandResult(context, jPushMessage.getSequence(), OnePush.RESULT_OK, null, tag, null);
  }

  @Override
  public void onCheckTagOperatorResult(Context context, JPushMessage jPushMessage) {
    super.onCheckTagOperatorResult(context, jPushMessage);
  }
}
