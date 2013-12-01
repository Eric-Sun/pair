package com.h13.pair.cache.service;

import com.h13.pair.cache.co.PairCO;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * Created with IntelliJ IDEA.
 * User: sunbo
 * Date: 13-11-25
 * Time: 下午4:03
 * To change this template use File | Settings | File Templates.
 */
@Service
public class PairCache {
    private static String PREFIX = "mj:pair:";

    @Resource(name = "pairCOTemplate")
    private RedisTemplate<String, PairCO> pairCOTemplate;

    public void put(PairCO pairCO) {
        String key = PREFIX + pairCO.getSessionId() + "_" + pairCO.getToSessionId();
        pairCOTemplate.opsForValue().set(key, pairCO);
    }

    public void delete(PairCO pairCO) {
        String key = PREFIX + pairCO.getSessionId() + "_" + pairCO.getToSessionId();
        pairCOTemplate.delete(key);
    }

    public PairCO get(String fromId, String toId) {
        String key = PREFIX + fromId + "_" + toId;
        return pairCOTemplate.opsForValue().get(key);
    }

}
