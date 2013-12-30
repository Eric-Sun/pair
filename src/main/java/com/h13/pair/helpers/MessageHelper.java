package com.h13.pair.helpers;

import com.h13.pair.cache.co.MessageCO;
import com.h13.pair.cache.co.PairCO;
import com.h13.pair.cache.service.MessageCache;
import com.h13.pair.cache.service.PairCache;
import com.h13.pair.exceptions.IsNotPairException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sunbo
 * Date: 13-11-25
 * Time: 下午4:09
 * To change this template use File | Settings | File Templates.
 */
@Service
public class MessageHelper {
    private static Logger LOG = LoggerFactory.getLogger(MessageHelper.class);

    @Autowired
    MessageCache messageCache;

    @Autowired
    SessionHelper sessionHelper;

    public void send(String fromId, String toId, String content) throws IsNotPairException {
        boolean isPair = sessionHelper.isPair(fromId, toId);
        if (!isPair)
            throw new IsNotPairException("sessionId = " + fromId + " toSessionId = " + toId);

        MessageCO msg = newMessage(fromId, toId, content);
        messageCache.put(msg);
        LOG.info("send message. sessionId=" + fromId + " toSessionId=" + toId);
        if (LOG.isDebugEnabled())
            LOG.debug("send message. sessionId=" + fromId + " toSessionId=" + toId + " message=" + content);
    }


    private MessageCO newMessage(String fromId, String toId, String content) {
        MessageCO msg = new MessageCO();
        msg.setFromId(fromId);
        msg.setToId(toId);
        msg.setContent(content);
        msg.setTimestamp(System.currentTimeMillis());
        return msg;
    }


    private List<MessageCO> tryToReceiveMessages(String fromId, String toId) {
        return messageCache.get(fromId, toId);
    }


    public List<MessageCO> receive(String fromId, String toId) throws IsNotPairException {
        boolean isPair = sessionHelper.isPair(fromId, toId);
        if (!isPair) {
            LOG.info("is not pair. sessionId=" + fromId + " toSessionId=" + toId);
            throw new IsNotPairException("fromId = " + fromId + " toId = " + toId);
        }
        List<MessageCO> msgList = tryToReceiveMessages(fromId, toId);
        LOG.info("receive message. sessionId=" + fromId + " toSessionId=" + toId);
        if (LOG.isDebugEnabled()) {
            for (MessageCO message : msgList) {
                LOG.debug("receive message. sessionId=" + fromId + " toSessionId=" +
                        toId + " message=" + message.getContent());
            }
        }
        return msgList;
    }


}
