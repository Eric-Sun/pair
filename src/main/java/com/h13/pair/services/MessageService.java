package com.h13.pair.services;

import com.h13.pair.cache.co.MessageCO;
import com.h13.pair.exceptions.IsNotPairException;
import com.h13.pair.helpers.MessageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sunbo
 * Date: 13-11-25
 * Time: 下午4:00
 * To change this template use File | Settings | File Templates.
 */
@Service
public class MessageService {

    @Autowired
    MessageHelper messageHelper;

    public void send(String fromId, String toId, String content) throws IsNotPairException {
        messageHelper.send(fromId, toId, content);
    }

    public List<MessageCO> receive(String fromId, String toId) throws IsNotPairException {
        return messageHelper.receive(fromId, toId);
    }


}
