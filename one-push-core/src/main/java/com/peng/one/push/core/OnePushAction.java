package com.peng.one.push.core;

/**
 * The name of the Action moves to receive broadcast
 * Created by pyt on 2017/5/10.
 */

public interface OnePushAction {
    String ACTION_RECEIVE_NOTIFICATION = "com.peng.one.push.ACTION_RECEIVE_NOTIFICATION";
    String ACTION_RECEIVE_NOTIFICATION_CLICK = "com.peng.one.push.ACTION_RECEIVE_NOTIFICATION_CLICK";
    String ACTION_RECEIVE_MESSAGE = "com.peng.one.push.ACTION_RECEIVE_MESSAGE";
    String ACTION_RECEIVE_COMMAND_RESULT = "com.peng.one.push.ACTION_RECEIVE_COMMAND_RESULT";
}
