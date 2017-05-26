package com.peng.one.push.service;

import com.peng.one.push.service.core.IPushService;
import com.peng.one.push.service.core.entity.MessageEntity;
import com.peng.one.push.service.core.entity.NotificationEntity;
import com.peng.one.push.service.huawei.HuaweiService;
import com.peng.one.push.service.huawei.intent.HWPushIntent;
import com.peng.one.push.service.xiaomi.XiaomiService;

import java.util.HashMap;

/**
 * Created by Administrator on 2017/5/17.
 */
public class Main {

    public static void main(String[] args) {
        NotificationEntity.Builder notificationBuilder = new NotificationEntity.Builder();
        NotificationEntity notificationEntity = notificationBuilder
//                .tokens("oiDmX2DFrSkuU5DQ7RVE3JD6CB6N5ZJ/uRoPMPIONTk=")
                .doing(NotificationEntity.DOING_CLIENT_CUSTOM)
                .extra("[{\"orderId\":\"9385-0094-4432-2432-5554-3424\"}]")
                .title("OnePush通知标题")
                .content("通知-->你只需要这一个推送SDK----OnePush")
                .tags("test")
//                .aliases("test")
                .keyValue("key_1", "value_1")
                .keyValue("key_2", "value_2")
                .build();

        MessageEntity.Builder MessageBuilder = new MessageEntity.Builder();
        MessageEntity messageEntity = MessageBuilder
//                .tokens("oiDmX2DFrSkuU5DQ7RVE3JD6CB6N5ZJ/uRoPMPIONTk=")
                .extra("[{\"orderId\":\"9385-0094-4432-2432-5554-3424\"}]")
                .tags("test")
//                .aliases("test")
                .msg("透传-->你只需要这一个推送SDK----OnePush")
                .keyValue("key_1", "value_1")
                .keyValue("key_2", "value_2")
                .build();

        IPushService huaweiService = new HuaweiService();
        IPushService xiaomiService = new XiaomiService();

        HWPushIntent.Builder builder = new HWPushIntent.Builder();

        HashMap<String, String> keyValueMap = new HashMap<>();
        keyValueMap.put("key1", "value1");
        keyValueMap.put("key2", "value2");
        keyValueMap.put("key3", "value3");
        String s = builder.title("标题").content("通知内容").extraMsg("额外信息")
                .keyValue(keyValueMap).build().toString();
        System.out.println(s);

        try {
            //发送通知-->小米
            sendNotification(notificationEntity, xiaomiService);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //发送透传-->小米
            sendMessage(messageEntity, xiaomiService);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //发送通知-->华为
            sendNotification(notificationEntity, huaweiService);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            //发送透传-->华为(由于华为PUSH_SDK透传的特殊性，必须制定token)
            sendMessage(new MessageEntity.Builder()
                    .tokens("0a000005984ecae0300000463600CN01")
                    .extra("[{\"orderId\":\"9385-0094-4432-2432-5554-3424\"}]")
                    .msg("透传-->你只需要这一个推送SDK----OnePush")
                    .keyValue("key_1", "value_1")
                    .keyValue("key_2", "value_2")
                    .build(), huaweiService);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**
     * 发送通知
     *
     * @param notificationEntity
     * @param pushService
     */
    public static void sendNotification(NotificationEntity notificationEntity, IPushService pushService) {
        pushService.sendNotification(notificationEntity);
    }

    /**
     * 发送消息
     *
     * @param messageEntity
     * @param pushService
     */
    public static void sendMessage(MessageEntity messageEntity, IPushService pushService) {
        pushService.sendMessage(messageEntity);
    }


}
