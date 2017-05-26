package com.peng.one.push.service.huawei.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/5/17.
 */
public class HuaweiNotification {

    public static class Android{


        /**
         * notification_title : tets
         * notification_content : testa
         * extras : [{"ttttttt":1},{"rrrrrrrrrrrr":"rrrrrrrrrrr"}]
         * doings : 2
         * intent : 11111111111111111111111111
         */

        private String notification_title;
        private String notification_content;
        private int doings;
        private String intent;
        private Map<String,String> extras;
        private String url;

        public String getNotification_title() {
            return notification_title;
        }

        public void setNotification_title(String notification_title) {
            this.notification_title = notification_title;
        }

        public String getNotification_content() {
            return notification_content;
        }

        public void setNotification_content(String notification_content) {
            this.notification_content = notification_content;
        }

        public int getDoings() {
            return doings;
        }

        public void setDoings(int doings) {
            this.doings = doings;
        }

        public String getIntent() {
            return intent;
        }

        public void setIntent(String intent) {
            this.intent = intent;
        }

        public Map<String, String> getExtras() {
            return extras;
        }

        public void setExtras(Map<String, String> extras) {
            this.extras = extras;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }


        @Override
        public String toString() {
            return "Android{" +
                    "notification_title='" + notification_title + '\'' +
                    ", notification_content='" + notification_content + '\'' +
                    ", doings=" + doings +
                    ", intent='" + intent + '\'' +
                    ", extras=" + extras +
                    ", url='" + url + '\'' +
                    '}';
        }
    }

    public static class Tag{

        private List<HashMap<String, List<String>>> tags;

        public List<HashMap<String, List<String>>> getTags() {
            return tags;
        }

        public void setTags(List<HashMap<String, List<String>>> tags) {
            this.tags = tags;
        }

        @Override
        public String toString() {
            return "Tag{" +
                    "tags=" + tags +
                    '}';
        }
    }

    public static class ExcludeTag{
        private Map<String, List<String>> exclude_tags;

        public Map<String, List<String>> getExclude_tags() {
            return exclude_tags;
        }

        public void setExclude_tags(Map<String, List<String>> exclude_tags) {
            this.exclude_tags = exclude_tags;
        }

        @Override
        public String toString() {
            return "ExcludeTag{" +
                    "exclude_tags=" + exclude_tags +
                    '}';
        }
    }
}
