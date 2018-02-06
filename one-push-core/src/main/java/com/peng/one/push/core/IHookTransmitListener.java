package com.peng.one.push.core;

/**
 * Created by Administer on 2018/2/7.
 */
public interface IHookTransmitListener {
    void transmitCommandResult(int type, int resultCode, String token
            , String extraMsg, String error);
}
