package com.peng.one.push.core;

/**
 * All of the corresponding push Code
 * 别名
 * Created by pyt on 2017/5/10.
 */

public interface OnePushCode {

    int RESULT_OK = 200;

    int RESULT_ERROR = 400;

    int TYPE_REGISTER = 2021;

    int TYPE_UNREGISTER =2022;

    int TYPE_ADD_TAG = 2023;

    int TYPE_DEL_TAG = 2024;

    int TYPE_BIND_ALIAS = 2025;

    int TYPE_UNBIND_ALIAS = 2026;

    int TYPE_AND_OR_DEL_TAG = 2027;

}
