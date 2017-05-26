package com.peng.one.push.service.huawei.utils;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * 和华为推送相
 * Created by Administrator on 2017/5/18.
 */
public class HWPushUtils {

    public static String map2HwExtra(Map<String,String> map){
        StringBuilder builder = new StringBuilder();
        builder.append(',')
                .append("\"extras\"")
                .append(':')
                .append('[');
        Set<Map.Entry<String, String>> entries = map.entrySet();
        Iterator<Map.Entry<String, String>> iterator = entries.iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, String> next = iterator.next();
            builder.append('{')
                    .append('\"')
                    .append(next.getKey())
                    .append('\"')
                    .append(':')
                    .append('\"')
                    .append(next.getValue())
                    .append('\"')
                    .append('}')
                    .append(',');
        }
        int finishLen = builder.length();
        builder.deleteCharAt(finishLen - 1);
        builder.append(']');
        return builder.toString();
    }
}
