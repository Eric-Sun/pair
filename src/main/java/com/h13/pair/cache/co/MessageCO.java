package com.h13.pair.cache.co;

/**
 * Created with IntelliJ IDEA.
 * User: sunbo
 * Date: 13-11-25
 * Time: 下午2:43
 * To change this template use File | Settings | File Templates.
 */
public class MessageCO {
    private String msgId;
    private String fromId;
    private String toId;
    private String content;
    private long timestamp;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getMsgId() {
        return msgId;
    }

    public void setMsgId(String msgId) {
        this.msgId = msgId;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getToId() {
        return toId;
    }

    public void setToId(String toId) {
        this.toId = toId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
