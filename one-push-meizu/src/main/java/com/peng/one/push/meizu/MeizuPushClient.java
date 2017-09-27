package com.peng.one.push.meizu;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import com.meizu.cloud.pushsdk.PushManager;
import com.peng.one.push.OnePush;
import com.peng.one.push.core.IPushClient;
import com.peng.one.push.core.OnePushAction;
import com.peng.one.push.log.OneLog;

/**
 * 魅族推送
 * Created by pengyuantao on 2017/9/25 11:50.
 */

public class MeizuPushClient implements IPushClient {

  //对应manifest的key
  public static final String MEIZU_PUSH_APP_ID = "MEIZU_PUSH_APP_ID";
  public static final String MEIZU_PUSH_APP_KEY = "MEIZU_PUSH_APP_KEY";

  private Context context;
  private int appId;
  private String appKey;


  @Override
  public void initContext(Context context) {
    this.context = context;
    try {
      Bundle metaData = context.getPackageManager()
          .getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA).metaData;
      this.appId = metaData.getInt(MEIZU_PUSH_APP_ID);
      this.appKey = metaData.getString(MEIZU_PUSH_APP_KEY);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
      OneLog.i("can't find MEIZU_PUSH_APP_ID or MEIZU_PUSH_APP_KEY in AndroidManifest.xml");
    }
  }

  @Override
  public void register() {
    PushManager.register(context, String.valueOf(appId), appKey);
    PushManager.switchPush(context,String.valueOf(appId), appKey, PushManager.getPushId(context), 1, true);
  }

  @Override
  public void unRegister() {
    PushManager.switchPush(context,String.valueOf(appId), appKey, PushManager.getPushId(context), 1, false);
    PushManager.unRegister(context, String.valueOf(appId), appKey);
  }

  @Override
  public void bindAlias(String s) {
    PushManager.subScribeAlias(context,String.valueOf(appId), appKey, PushManager.getPushId(context), s);
  }

  @Override
  public void unBindAlias(String s) {
    PushManager.unSubScribeAlias(context,String.valueOf(appId), appKey, PushManager.getPushId(context), s);
  }

  @Override
  public void addTag(String s) {
    PushManager.subScribeTags(context,String.valueOf(appId), appKey, PushManager.getPushId(context),s);
  }

  @Override
  public void deleteTag(String s) {
    PushManager.unSubScribeTags(context,String.valueOf(appId), appKey, PushManager.getPushId(context),s);
  }


}
