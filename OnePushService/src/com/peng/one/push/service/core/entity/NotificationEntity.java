package com.peng.one.push.service.core.entity;

import com.alibaba.fastjson.annotation.JSONField;

import org.apache.http.util.TextUtils;

/**
 * 通知实体类
 * Created by Administrator on 2017/5/17.
 */
public class NotificationEntity {

    public static final int DOING_CLIENT_CUSTOM = 1;
    public static final int DOING_OPEN_APP = 2;
    public static final int DOING_OPEN_URL = 3;
    public static final int DOING_OPEN_COUSTOM_PAGE = 4;

    private NotificationEntity() {
    }

    //需要进行操作的动作（客户端自定义处理，打开应用，打开指定页面，打开链接-没卵用，打开富文本-没卵用）
    private int doing;

    private String title;

    private String content;

    private String extra;

    private String intent;

    private String url;

    @JSONField(serialize = false)
    private Map<String, String> keyValue;

    private List<String> tokens;

    private List<String> aliases;

    private List<String> tags;


    public int getDoing() {
        return doing;
    }

    public void setDoing(int doing) {
        this.doing = doing;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getIntent() {
        return intent;
    }

    public void setIntent(String intent) {
        this.intent = intent;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> tokens) {
        this.tokens = tokens;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public Map<String, String> getKeyValue() {
        return keyValue;
    }

    public void setKeyValue(Map<String, String> keyValue) {
        this.keyValue = keyValue;
    }

    @Override
    public String toString() {
        return "NotificationEntity{" +
                "doing=" + doing +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", extra='" + extra + '\'' +
                ", intent='" + intent + '\'' +
                ", url='" + url + '\'' +
                ", keyValue=" + keyValue +
                ", tokens=" + tokens +
                ", aliases=" + aliases +
                ", tags=" + tags +
                '}';
    }

    public static class Builder {

        private int doing;

        private String title;

        private String content;

        private String extra;

        private String intent;

        private String url;

        private Map<String, String> keyValue;

        private List<String> tokens;

        private List<String> aliases;

        private List<String> tags;

        public Builder doing(int doing) {
            this.doing = doing;
            return this;
        }

        public Builder title(String title) {
            if (TextUtils.isEmpty(title)) {
                throw new IllegalArgumentException("notification title is null");
            }
            this.title = title;
            return this;
        }

        public Builder content(String content) {
            if (TextUtils.isEmpty(content)) {
                throw new IllegalArgumentException("notification content is null");
            }
            this.content = content;
            return this;
        }

        public Builder intent(String intent) {
            this.intent = intent;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder extra(String extra) {
            this.extra = extra;
            return this;
        }

        public Builder keyValue(String key, String value) {
            if (this.keyValue == null) {
                this.keyValue = new HashMap<>();
            }
            this.keyValue.put(key, value);
            return this;
        }

        public Builder tokens(String... tokens) {
            if (tokens.length == 0) {
                throw new IllegalArgumentException("you tokens is null");
            }
            if (this.tokens == null) {
                this.tokens = new ArrayList<String>();
            }
            this.tokens.addAll(Arrays.asList(tokens));
            return this;
        }

        public Builder aliases(String... aliases) {
            if (aliases.length == 0) {
                throw new IllegalArgumentException("you aliases is null");
            }
            if (this.aliases == null) {
                this.aliases = new ArrayList<String>();
            }
            this.aliases.addAll(Arrays.asList(aliases));
            return this;
        }

        public Builder tags(String... tags) {
            if (tags.length == 0) {
                throw new IllegalArgumentException("you tags is null");
            }
            if (this.tags == null) {
                this.tags = new ArrayList<String>();
            }
            this.tags.addAll(Arrays.asList(tags));
            return this;
        }

        public NotificationEntity build() {
            NotificationEntity notificationEntity = new NotificationEntity();
            notificationEntity.doing = this.doing;
            notificationEntity.title = this.title;
            notificationEntity.content = this.content;
            notificationEntity.intent = this.intent;
            notificationEntity.url = this.url;
            notificationEntity.tokens = this.tokens;
            notificationEntity.aliases = this.aliases;
            notificationEntity.tags = this.tags;
            notificationEntity.extra = this.extra;
            notificationEntity.keyValue = this.keyValue;
            return notificationEntity;
        }
    }

}
