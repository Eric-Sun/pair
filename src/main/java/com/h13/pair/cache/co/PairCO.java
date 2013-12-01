package com.h13.pair.cache.co;

/**
 * Created with IntelliJ IDEA.
 * User: sunbo
 * Date: 13-11-25
 * Time: 下午4:01
 * To change this template use File | Settings | File Templates.
 */
public class PairCO {
    private String sessionId;
    private String toSessionId;
    private long createTimestamp;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getToSessionId() {
        return toSessionId;
    }

    public void setToSessionId(String toSessionId) {
        this.toSessionId = toSessionId;
    }

    public long getCreateTimestamp() {
        return createTimestamp;
    }

    public void setCreateTimestamp(long createTimestamp) {
        this.createTimestamp = createTimestamp;
    }
}
