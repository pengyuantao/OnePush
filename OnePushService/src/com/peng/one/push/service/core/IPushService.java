package com.peng.one.push.service.core;

import com.peng.one.push.service.core.entity.MessageEntity;
import com.peng.one.push.service.core.entity.NotificationEntity;
import com.peng.one.push.service.core.entity.OneResponse;

/**
 * 推送服务的统一的接口
 * Created by Administrator on 2017/5/16.
 */
public interface IPushService {

    /**
     * 发送通知栏消息
     * @param notificationEntity
     * @return
     */
    OneResponse sendNotification(NotificationEntity notificationEntity);

    /**
     * 发送透传消息
     * @param messageEntity
     * @return
     */
    OneResponse sendMessage(MessageEntity messageEntity);


}
