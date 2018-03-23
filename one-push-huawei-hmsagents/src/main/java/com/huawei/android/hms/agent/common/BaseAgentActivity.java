package com.huawei.android.hms.agent.common;

import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

/**
 * 基础activity，用来处理公共的透明参数
 */
public class BaseAgentActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestActivityTransparent();
    }

    /**
     * 启用透明的跳板Activity
     */
    private void requestActivityTransparent() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();
        if (window != null) {
            window.addFlags(0x04000000);
        }
    }
}
