package com.peng.one.push.service.core.entity;

/**
 * 统一的返回结果
 * Created by pyt on 2017/5/17.
 */
public class OneResponse {
    //操作成功
    public static final int RESULT_OK = 0;

    private int resultCode;

    private String msg;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "OneResponse{" +
                "resultCode=" + resultCode +
                ", msg='" + msg + '\'' +
                '}';
    }
}
