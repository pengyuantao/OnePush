package com.peng.one.push.service.huawei;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.peng.one.push.service.Constant;
import com.peng.one.push.service.core.IPushService;
import com.peng.one.push.service.core.entity.MessageEntity;
import com.peng.one.push.service.core.entity.NotificationEntity;
import com.peng.one.push.service.core.entity.OneResponse;
import com.peng.one.push.service.huawei.demo.PushRet;
import com.peng.one.push.service.huawei.entity.HuaweiNotification;
import com.peng.one.push.service.huawei.entity.HuaweiPushResponse;
import com.peng.one.push.service.huawei.intent.HWPushIntent;
import com.peng.one.push.service.huawei.utils.HWPushUtils;
import com.peng.one.push.service.log.OneLog;

import org.apache.http.util.TextUtils;

import java.io.FileInputStream;
import java.text.SimpleDateFormat;

import nsp.NSPClient;
import nsp.OAuth2Client;
import nsp.support.common.AccessToken;
import nsp.support.common.NSPException;

/**
 * 华为推送服务
 * Created by Administrator on 2017/5/17.
 */
public class HuaweiService implements IPushService {

    private final String TIMESTAMP_NORMAL = "yyyy-MM-dd HH:mm:ss";
    private final SimpleDateFormat dataFormat = new SimpleDateFormat(TIMESTAMP_NORMAL);
    private final NSPClient client;

    public HuaweiService() {
        try {
            OAuth2Client oauth2Client = new OAuth2Client();
            oauth2Client.initKeyStoreStream(new FileInputStream("mykeystorebj.jks"), "123456");
            AccessToken access_token = oauth2Client.getAccessToken("client_credentials", Constant.HAWEI_APP_ID, Constant.HUAWEI_APP_SECRET);
            System.err.println("access token :" + access_token.getAccess_token() + ",expires time[access token 过期时间]:"
                    + access_token.getExpires_in());
            client = new NSPClient(access_token.getAccess_token());
            client.initHttpConnections(30, 50);//设置每个路由的连接数和最大连接数
            client.initKeyStoreStream(new FileInputStream("mykeystorebj.jks"), "123456");//如果访问https必须导入证书流和密码
        } catch (Exception e) {
            throw new IllegalStateException("华为推送服务初始化失败" + e.toString());
        }
    }

    @Override
    public OneResponse sendNotification(NotificationEntity notificationEntity) {
        HashMap<String, Object> pushParams = new HashMap<>();
        //推送的实体类
        HuaweiNotification.Android android = new HuaweiNotification.Android();
        //设置华为推送类型和推送目标
        setPushTypeAndTarget(pushParams, notificationEntity.getTokens(), notificationEntity.getAliases(), notificationEntity.getTags());
        //根据通知不同的doing，设置不同消息内容
        setNotificationDoing(notificationEntity, android);
        //设置标题和内容
        android.setNotification_title(notificationEntity.getTitle());
        android.setNotification_content(notificationEntity.getContent());
        //将当前的推送实体，转换成json
        String pushJson = setNotificationKeyValue(JSON.toJSONString(android), notificationEntity);
        //设置推送的Json
        pushParams.put("android", pushJson);
        //设置http超时时间
        client.setTimeout(10000, 15000);
        //接口调用
        try {
            //{"result_desc":"Illegal parameter","result_code":80200001,"request_id":"150591431857577375000000"}
            //{"result_code":0,"request_id":"1380075138"}
            String rsp = client.call("openpush.openapi.notification_send", pushParams, String.class);
            OneLog.i("推送通知参数:" + pushParams.toString());
            OneLog.i("推送通知响应:" + rsp);
            HuaweiPushResponse response = JSON.parseObject(rsp, HuaweiPushResponse.class);
            OneResponse oneResponse = new OneResponse();
            if (response.getResult_code() == 0) {
                oneResponse.setResultCode(OneResponse.RESULT_OK);
                oneResponse.setMsg("success");
            } else {
                oneResponse.setResultCode(response.getResult_code());
                oneResponse.setMsg(response.getResult_desc());
            }
            return oneResponse;
        } catch (NSPException e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param messageEntity
     * @return
     */
    @Override
    public OneResponse sendMessage(MessageEntity messageEntity) {
        HashMap<String, Object> pushParams = new HashMap<>();
        if (messageEntity.getTokens() == null || messageEntity.getTokens().isEmpty()) {
            throw new IllegalArgumentException("华为官方PushSDK,只提供token方式发送Message，不过官方推送后台，却有根据Tag推送的方式，fuck！\n"+
                    "如果非要使用根据Tag,推送，直接模拟请求华为后台的http请求，或者就是自己服务器备份tag，然后查询tag对应的所有token，然后进行群发！");
        }
        setPushTypeAndTarget(pushParams, messageEntity.getTokens(), null, null);
        //必选
        //0：高优先级
        //1：普通优先级
        //缺省值为1
        int priority = 0;

        //消息是否需要缓存，必选
        //0：不缓存
        //1：缓存
        //  缺省值为0
        int cacheMode = 1;
        //标识消息类型（缓存机制），必选
        //由调用端赋值，取值范围（1~100）。当TMID+msgType的值一样时，仅缓存最新的一条消息
        int msgType = 1;
        String tokens = (String) pushParams.get("tokens");
        if (!TextUtils.isEmpty(tokens)) {
            String[] split = tokens.split(",");
            for (String s : split) {
                System.out.println(s);
            }
            pushParams.put("deviceTokenList", split);
        }
        pushParams.put("message", messageEntity.getMsg());
        pushParams.put("priority", priority);
        pushParams.put("cacheMode", cacheMode);
        pushParams.put("msgType", msgType);

        //设置http超时时间
        client.setTimeout(10000, 15000);
        //接口调用
        PushRet resp = null;
        try {
            System.out.println(pushParams.toString());
            resp = client.call("openpush.message.batch_send", pushParams, PushRet.class);
            System.out.println(resp);
        } catch (NSPException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将HashMap转换成华为特定的keyvalue的json
     *
     * @param pushJson
     * @param notificationEntity
     */
    private String setNotificationKeyValue(String pushJson, NotificationEntity notificationEntity) {
        //华为推送只支持KeyValue
        if (notificationEntity.getKeyValue() != null && !notificationEntity.getKeyValue().isEmpty()) {
            //添加兼容华为的keyValue类型(华为的keyValue类型： [{"key":""value},{}])
            StringBuilder stringBuilder = new StringBuilder(pushJson);
            int initLen = stringBuilder.length();
            stringBuilder.deleteCharAt(initLen - 1);
            stringBuilder.append(HWPushUtils.map2HwExtra(notificationEntity.getKeyValue()));
            stringBuilder.append("}");
            pushJson = stringBuilder.toString();
        }
        return pushJson;
    }

    /**
     * 设置Notification的doing类型
     * 1.打开app
     * 2.自定义打开页面
     * 3.打开链接
     *
     * @param notificationEntity
     * @param android
     */
    private void setNotificationDoing(NotificationEntity notificationEntity, HuaweiNotification.Android android) {

        switch (notificationEntity.getDoing()) {
            case NotificationEntity.DOING_CLIENT_CUSTOM:
                //需要指定点击的Activity的Intent
                android.setDoings(2);
                android.setIntent(new HWPushIntent.Builder().title(notificationEntity.getTitle()).content(notificationEntity.getContent()).extraMsg(notificationEntity.getExtra()).keyValue(notificationEntity.getKeyValue()).build().toString());

                break;
            case NotificationEntity.DOING_OPEN_APP:
                android.setDoings(1);
                break;

            case NotificationEntity.DOING_OPEN_COUSTOM_PAGE:
                if (TextUtils.isEmpty(notificationEntity.getIntent())) {
                    throw new IllegalArgumentException("DOING_OPEN_COUSTOM_PAGE you must set intent");
                }
                android.setDoings(2);
                android.setIntent(notificationEntity.getIntent());
                break;

            case NotificationEntity.DOING_OPEN_URL:
                if (TextUtils.isEmpty(notificationEntity.getUrl())) {
                    throw new IllegalArgumentException("DOING_OPEN_URL you must set url");
                }
                android.setDoings(3);
                android.setUrl(notificationEntity.getUrl());
                break;
            default:
                OneLog.i("不支持的doing类型!");
                throw new IllegalArgumentException("do not support doing " + notificationEntity.getDoing());
        }
    }

    /**
     * 根据token,alias,tags设置华为不同的推送类型和推送目标
     * <p>
     * 1：指定用户，必须指定tokens字段
     * 2：所有人，无需指定tokens，tags，exclude_tags
     * 3：一群人，必须指定tags或者exclude_tags字段
     *
     * @param pushParams
     * @param tokens
     * @param alias
     * @param tags
     */
    private void setPushTypeAndTarget(Map<String, Object> pushParams, List<String> tokens, List<String> alias, List<String> tags) {

        if (tokens != null) {

            pushParams.put("push_type", 1);
            StringBuilder builder = new StringBuilder();
            for (String s : tokens) {
                builder.append(s).append(",");
            }
            builder.deleteCharAt(builder.length() - 1);
            pushParams.put("tokens", builder.toString());
        } else if (alias != null) {
            //华为推送不支持这个别名推送
            pushParams.put("push_type", 2);
        } else if (tags != null) {
            pushParams.put("push_type", 3);
            //需要校验传过来的tags,是否有，如果有其中一个标签没有，那么华为就不会给你推送
            //构建tag的json
            HuaweiNotification.Tag tagBean = new HuaweiNotification.Tag();
            List<String> allTags = getAllTags();

            List<HashMap<String, List<String>>> tagList = new ArrayList<>();
            for (String s : tags) {
                HashMap<String, List<String>> map = new HashMap<>();
                if (allTags.contains(s)) {
                    map.put(s, Collections.singletonList(s));
                    tagList.add(map);
                } else {
                    OneLog.i("不包含标签："+s);
                }
            }
            if (tagList == null) {
                throw new IllegalArgumentException("提供的标签没有一个和华为的标签对应上");
            }
            tagBean.setTags(tagList);
            pushParams.put("tags", JSON.toJSONString(tagBean));
        } else {
            pushParams.put("push_type", 2);
        }
    }

    public List<String> getAllTags() {

        //设置http超时时间
        client.setTimeout(10000, 15000);
        //接口调用
        String rsp = null;
        try {
            rsp = client.call("openpush.openapi.query_app_tags", new HashMap<String, Object>(), String.class);
            List<String> tagList = new ArrayList<>();
            JSONObject jsonObject = JSON.parseObject(rsp);
            String tags = jsonObject.getString("tags");
            JSONArray jsonArray = JSON.parseArray(tags);
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject item = jsonArray.getJSONObject(i);
                Set<String> strings = item.keySet();
                String next = strings.iterator().next();
                tagList.add(next);
            }
            OneLog.i("查询应用标签接口响应：" + rsp);
            return tagList;
        } catch (NSPException e) {
            e.printStackTrace();
        }

        //打印响应
        //响应样例：{"request_id":"1373593606","tags":[{"应用激活状态":["已激活"]},{"name":["1","hkdajc","hshshsh","jkg\n","rt","tyu","yu"]},{"age":["1","25","45","56","6644","gh","容易"]}]}
        return null;
    }


}
