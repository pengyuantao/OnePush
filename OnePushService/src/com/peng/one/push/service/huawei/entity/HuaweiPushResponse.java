package com.peng.one.push.service.huawei.entity;

/**
 * Created by Administrator on 2017/5/19.
 */
public class HuaweiPushResponse {


    /**
     * result_desc : Illegal parameter
     * result_code : 80200001
     * request_id : 150591431857577375000000
     */

    private String result_desc;
    private int result_code;
    private String request_id;

    public String getResult_desc() {
        return result_desc;
    }

    public void setResult_desc(String result_desc) {
        this.result_desc = result_desc;
    }

    public int getResult_code() {
        return result_code;
    }

    public void setResult_code(int result_code) {
        this.result_code = result_code;
    }

    public String getRequest_id() {
        return request_id;
    }

    public void setRequest_id(String request_id) {
        this.request_id = request_id;
    }
}
