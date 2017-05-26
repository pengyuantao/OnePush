package com.peng.one.push.service.core.entity;

import org.apache.http.util.TextUtils;

/**
 * 透传消息实体类
 * Created by Administrator on 2017/5/17.
 */
public class MessageEntity {

    private String msg;

    private String extra;

    private Map<String, String> keyValue;

    private List<String> tokens;

    private List<String> aliases;

    private List<String> tags;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
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

    public static class Builder{

        private String msg;

        private String extra;

        private Map<String, String> keyValue;

        private List<String> tokens;

        private List<String> aliases;

        private List<String> tags;

        public Builder msg(String msg) {
            if (TextUtils.isEmpty(msg)) {
                throw new IllegalArgumentException("you through message param msg is null!" );
            }
            this.msg = msg;
            return this;
        }

        public Builder extra(String extra) {
            this.extra = extra;
            return this;
        }

        public Builder tokens(String... tokens) {
            if (tokens.length == 0) {
                throw new IllegalArgumentException("you tokens is null");
            }
            if (this.tokens == null || this.tokens.isEmpty()) {
                this.tokens = new ArrayList<String>();
            }
            this.tokens.addAll(Arrays.asList(tokens));
            return this;
        }

        public Builder aliases(String... aliases) {
            if (aliases.length == 0) {
                throw new IllegalArgumentException("you aliases is null");
            }
            if (this.aliases == null || this.aliases.isEmpty()) {
                this.aliases = new ArrayList<String>();
            }
            this.aliases.addAll(Arrays.asList(aliases));
            return this;
        }

        public Builder tags(String... tags) {
            if (tags.length == 0) {
                throw new IllegalArgumentException("you tags is null");
            }
            if (this.tags == null || this.tags.isEmpty()) {
                this.tags = new ArrayList<String>();
            }
            this.tags.addAll(Arrays.asList(tags));
            return this;
        }

        public Builder keyValue(String key, String value) {
            if (this.keyValue == null) {
                this.keyValue = new HashMap<>();
            }
            this.keyValue.put(key, value);
            return this;
        }

        public MessageEntity build(){
            MessageEntity messageEntity = new MessageEntity();
            messageEntity.msg = this.msg;
            messageEntity.extra = this.extra;
            messageEntity.tokens = this.tokens;
            messageEntity.aliases = this.aliases;
            messageEntity.tags = this.tags;
            messageEntity.keyValue = this.keyValue;
            return messageEntity;
        }

    }

}
