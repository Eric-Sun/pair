package com.h13.pair.cache.service;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * 等到队列的存储
 * User: sunbo
 * Date: 13-7-16
 * Time: 下午4:53
 * To change this template use File | Settings | File Templates.
 */
@Service
public class WaitingQueueCache {
    private static String KEY = "mj:waiting";

    @Resource(name = "waitingCOTemplate")
    private RedisTemplate<String, String> waitingCOTemplate;

    public boolean put(String sessionId) {
        return waitingCOTemplate.opsForSet().add(KEY, sessionId);
    }

    public String get() {
        return waitingCOTemplate.opsForSet().pop(KEY);
    }

    public boolean delete(String sessionId) {
        return waitingCOTemplate.opsForSet().remove(KEY, sessionId);
    }

}
