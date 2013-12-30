package com.h13.pair.cache.service;

import com.h13.pair.cache.co.MessageCO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.LinkedList;
import java.util.List;

/**
 * 发送消息的缓存，存放的是一个list
 * key:mj:msg:${fromId}_${toId}
 * value:消息list
 * User: sunbo
 * Date: 13-7-16
 * Time: 下午4:53
 * To change this template use File | Settings | File Templates.
 */
@Service
public class MessageCache {
    private static String PREFIX = "mj:msg:";

    @Resource(name = "messageCOTemplate")
    private RedisTemplate<String, MessageCO> messageCOTemplate;

    public void put(MessageCO messageCO) {
        String fromId = messageCO.getFromId();
        String toId = messageCO.getToId();
        String key = PREFIX + fromId + "_" + toId;
        messageCOTemplate.opsForList().rightPush(key, messageCO);
    }

    public List<MessageCO> get(String fromId, String toId) {
        String key = PREFIX + fromId + "_" + toId;
        long size = messageCOTemplate.opsForList().size(key);
        List<MessageCO> list = new LinkedList<MessageCO>();
        for (int i = 0; i < size; i++) {
            list.add(messageCOTemplate.opsForList().leftPop(key));
        }
        return list;
    }

}
