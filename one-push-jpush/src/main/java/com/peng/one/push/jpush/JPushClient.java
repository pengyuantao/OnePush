package com.peng.one.push.jpush;

import android.content.Context;
import android.text.TextUtils;
import cn.jpush.android.api.JPushInterface;
import com.peng.one.push.OnePush;
import com.peng.one.push.OneRepeater;
import com.peng.one.push.core.IPushClient;
import com.peng.one.push.log.OneLog;
import java.util.Collections;

/**
 * JPush Client
 * Created by pengyuantao on 2017/10/25 18:05.
 */

public class JPushClient implements IPushClient {

  private Context context;

  @Override
  public void initContext(Context context) {
    this.context = context.getApplicationContext();
    if (OneLog.isDebug()) {
      JPushInterface.setDebugMode(true);
    }
    JPushInterface.init(context);
  }

  @Override
  public void register() {
    if (JPushInterface.isPushStopped(this.context)) {
      JPushInterface.resumePush(this.context);
    }
    String token = JPushInterface.getRegistrationID(this.context);
    if (!TextUtils.isEmpty(token)) {
      OneRepeater.transmitCommandResult(this.context, OnePush.TYPE_REGISTER, 200, token, null,
          null);
    }
  }

  @Override
  public void unRegister() {
    JPushInterface.stopPush(this.context);
  }

  @Override
  public void bindAlias(String alias) {
    JPushInterface.setAlias(this.context, OnePush.TYPE_BIND_ALIAS, alias);
  }

  @Override
  public void unBindAlias(String alias) {
    JPushInterface.deleteAlias(this.context, OnePush.TYPE_UNBIND_ALIAS);
  }

  @Override
  public void addTag(String tag) {
    JPushInterface.addTags(this.context, OnePush.TYPE_ADD_TAG, Collections.singleton(tag));
  }

  @Override
  public void deleteTag(String tag) {
    JPushInterface.deleteTags(this.context, OnePush.TYPE_DEL_TAG, Collections.singleton(tag));
  }
}
