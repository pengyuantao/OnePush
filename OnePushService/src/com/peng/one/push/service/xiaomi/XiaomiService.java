package com.peng.one.push.service.xiaomi;

import com.peng.one.push.service.Constant;
import com.peng.one.push.service.core.IPushService;
import com.peng.one.push.service.core.entity.MessageEntity;
import com.peng.one.push.service.core.entity.NotificationEntity;
import com.peng.one.push.service.core.entity.OneResponse;
import com.peng.one.push.service.log.OneLog;
import com.xiaomi.push.sdk.ErrorCode;
import com.xiaomi.xmpush.server.Message;
import com.xiaomi.xmpush.server.Result;
import com.xiaomi.xmpush.server.Sender;

/**
 * 小米推送服务
 * Created by Administrator on 2017/5/19.
 */
public class XiaomiService implements IPushService {

    public static final String PACKAGE_NAME = "com.peng.one.push";
    private Sender sender = new Sender(Constant.XIAOMI_APP_SECRET);

    public XiaomiService(){

    }

    @Override
    public OneResponse sendNotification(NotificationEntity notificationEntity) {
        Message.Builder builder = new Message.Builder();
        builder.title(notificationEntity.getTitle())
                .payload(notificationEntity.getExtra())
                .description(notificationEntity.getContent())
                .notifyType(Message.NOTIFY_TYPE_SOUND)
                .passThrough(0)//设置消息是否通过透传的方式送给app，1表示透传消息，0表示通知栏消息。
                .restrictedPackageName(PACKAGE_NAME);
        setKeyValue(builder, notificationEntity.getKeyValue());
        try {
            return send(builder.build(), notificationEntity.getTokens(), notificationEntity.getAliases(), notificationEntity.getTags());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public OneResponse sendMessage(MessageEntity messageEntity) {
        Message.Builder builder = new Message.Builder();
        builder .payload(messageEntity.getMsg())
                .description(messageEntity.getExtra())
                .passThrough(1)//设置消息是否通过透传的方式送给app，1表示透传消息，0表示通知栏消息。
                .restrictedPackageName(PACKAGE_NAME);
        setKeyValue(builder, messageEntity.getKeyValue());
        try {
            return send(builder.build(), messageEntity.getTokens(), messageEntity.getAliases(), messageEntity.getTags());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 设置通知或者消息的键值对
     * @param builder
     * @param keyValue
     */
    public void setKeyValue(Message.Builder builder, Map<String, String> keyValue) {
        if (keyValue != null && !keyValue.isEmpty()) {
            Iterator<Map.Entry<String, String>> iterator = keyValue.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, String> next = iterator.next();
                builder.extra(next.getKey(), next.getValue());
            }
        }
    }

    private OneResponse send(Message message, List<String> tokens, List<String> alias, List<String> tags) throws Exception {

        Result response = null;
        if (tokens != null) {
                response = sender.send(message, tokens, 2);
        } else if (alias!= null) {
                response = sender.sendToAlias(message, alias, 2);

        } else if (tags != null) {
            response = sender.multiTopicBroadcast(message, tags, Sender.BROADCAST_TOPIC_OP.UNION, 2);
        } else {
            response = sender.broadcastAll(message, 2);
        }
        OneLog.i("发送返回：" + response.toString());
        OneResponse oneResponse = new OneResponse();
        ErrorCode errorCode = response.getErrorCode();
        oneResponse.setResultCode(errorCode == ErrorCode.Success? OneResponse.RESULT_OK : errorCode.getValue());
        oneResponse.setMsg(errorCode.getDescription());
        return oneResponse;
    }
}
