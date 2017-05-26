package com.peng.one.push.service.huawei.intent;

import com.alibaba.fastjson.JSON;

import org.apache.http.util.TextUtils;

import java.util.Map;

/**
 * Created by Administrator on 2017/5/17.
 */
public class HWPushIntent {

    private String intentUri;

    private HWPushIntent() {
    }

    @Override
    public String toString() {
        return intentUri;
    }

    public static class Builder{

        private static final String URI_TEXT = "OnePush://com.peng.one.push/notification";

        private static final String INTENT_END= "#Intent;scheme=OnePush;launchFlags=0x10000000;end";

        private String title;

        private String content;

        private String extraMsg;

        private Map<String, String> keyValue;

        public Builder title(String title){
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            this.content = content;
            return this;
        }

        public Builder extraMsg(String extraMsg) {
            this.extraMsg = extraMsg;
            return this;
        }

        public Builder keyValue(Map<String, String> keyValue) {
            this.keyValue = keyValue;
            return this;
        }

        public HWPushIntent build(){
            if (TextUtils.isEmpty(title)) {
                throw new IllegalArgumentException("title must not null");
            }

            if (TextUtils.isEmpty(content)) {
                throw new IllegalArgumentException("content must not null");
            }
            //?title=%s&content=%s&extra=%s
            StringBuilder builder = new StringBuilder(URI_TEXT);
            builder.append("?").append("title").append("=").append(title);
            builder.append("&").append("content").append("=").append(content);
            if (!TextUtils.isEmpty(extraMsg)) {
                builder.append("&").append("extraMsg").append("=").append(extraMsg);
            }

            if (keyValue != null && !keyValue.isEmpty()) {
                builder.append("&").append("keyValue").append("=").append(JSON.toJSONString(keyValue));
            }

            HWPushIntent hwPushIntent = new HWPushIntent();
            hwPushIntent.intentUri = generate(builder.toString());
            return hwPushIntent;
        }

        private static String generate(String dataText) {

            StringBuilder uri = new StringBuilder(128);
            final int N = dataText.length();
            String scheme = null;
            String data = null;

            for (int i = 0; i < N; i++) {
                char c = dataText.charAt(i);
                if ((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z')
                        || c == '.' || c == '-') {
                    continue;
                }
                if (c == ':' && i > 0) {
                    // Valid scheme.
                    scheme = dataText.substring(0, i);
                    uri.append("intent:");
                    data = dataText.substring(i + 1);
                    break;
                }
                break;
            }
            uri.append(data);
            uri.append(INTENT_END);
            return uri.toString();
        }
    }

}
