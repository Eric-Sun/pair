package com.h13.pair.helpers;

import com.h13.pair.cache.co.MessageCO;
import com.h13.pair.cache.co.PairCO;
import com.h13.pair.cache.service.MessageCache;
import com.h13.pair.cache.service.PairCache;
import com.h13.pair.exceptions.IsNotPairException;
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

    @Autowired
    PairCache pairCache;

    @Autowired
    MessageCache messageCache;

    public void send(String fromId, String toId, String content) throws IsNotPairException {
        boolean isPair = isPair(fromId, toId);
        if (!isPair)
            throw new IsNotPairException("fromId = " + fromId + " toId = " + toId);

        MessageCO msg = newMessage(fromId, toId, content);
        messageCache.put(msg);
    }


    /**
     * 检测是否已经成为一对了
     *
     * @param fromId
     * @param toId
     * @return
     */
    public boolean isPair(String fromId, String toId) {
        PairCO pair = pairCache.get(fromId, toId);
        if (pair != null)
            return true;
        return false;

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
        boolean isPair = isPair(fromId, toId);
        if (!isPair)
            throw new IsNotPairException("fromId = " + fromId + " toId = " + toId);
        return tryToReceiveMessages(fromId, toId);
    }


}
