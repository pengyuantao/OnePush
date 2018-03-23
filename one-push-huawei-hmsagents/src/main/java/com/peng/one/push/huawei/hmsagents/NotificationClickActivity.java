package com.peng.one.push.huawei.hmsagents;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;

import com.peng.one.push.OneRepeater;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by pyt on 2017/5/15.
 */

public class NotificationClickActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Uri uri = getIntent().getData();
        if (uri != null) {
            String title = uri.getQueryParameter("title");
            String content = uri.getQueryParameter("content");
            String extraMsg = uri.getQueryParameter("extraMsg");
            String keyValue = uri.getQueryParameter("keyValue");
            OneRepeater.transmitNotificationClick(getApplicationContext(), -1, title, content, extraMsg, json2Map(keyValue));
        }
        finish();
    }

    /**
     * json转换map
     * @param json
     * @return
     */
    private Map<String,String> json2Map(String json){
        try {
            JSONObject jsonObject = new JSONObject(json);
            Map<String, String> map = new HashMap<>();
            Iterator<String> iterator = jsonObject.keys();
            while (iterator.hasNext()) {
                String key = iterator.next();
                String value = jsonObject.getString(key);
                map.put(key, value);
            }
            return map;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }
}
