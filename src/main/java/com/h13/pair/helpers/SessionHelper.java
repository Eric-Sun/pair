package com.h13.pair.helpers;

import com.h13.pair.cache.co.PairCO;
import com.h13.pair.cache.service.PairCache;
import com.h13.pair.cache.service.WaitingQueueCache;
import com.h13.pair.daos.SessionDAO;
import com.h13.pair.exceptions.NoSessionForPairException;
import com.h13.pair.exceptions.SessionException;
import com.h13.pair.exceptions.SessionPairedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;

/**
 * 每一个用户为一个session
 * User: sunbo
 * Date: 13-11-26
 * Time: 下午4:00
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SessionHelper {
    private Random ran = new Random();
    @Autowired
    SessionDAO sessionDAO;
    @Autowired
    WaitingQueueCache waitingQueueCache;
    @Autowired
    PairCache pairCache;

    /**
     * 创建一个session，然后处于等待的状态，并放入到等待队列中
     *
     * @throws SessionException
     */
    public String createSession() {
        String sessionId = getSessionSign();
        sessionDAO.create(sessionId);
        boolean b = waitingQueueCache.put(sessionId);
        return sessionId;
    }

    public PairCO doPair(String sessionId) throws SessionPairedException, NoSessionForPairException {
        // 先从队列中去掉自己
        boolean b = waitingQueueCache.delete(sessionId);
        if (!b)
            throw new SessionPairedException("sessionId[" + sessionId + "] have been paired.");
        // 从队列中找到匹配的id
        String toSessionId = waitingQueueCache.get();
        if (toSessionId == null)  {
            // 如果没有找到的话，把自己放入队列中
            waitingQueueCache.put(sessionId);
            throw new NoSessionForPairException("no session for pair.");
        }

        // 创建双方的关系
        PairCO pairCO = addPairCache(sessionId, toSessionId);
        sessionDAO.doPair(sessionId,toSessionId);
        sessionDAO.doPair(toSessionId,sessionId);
        return pairCO;
    }

    public void close(String sessionId,String toSessionId){
        sessionDAO.close(sessionId);
        sessionDAO.close(toSessionId);
        removePairCache(sessionId,toSessionId);
    }


    private PairCO addPairCache(String sessionId, String toSessionId) {
        PairCO pairCO = new PairCO();
        pairCO.setSessionId(sessionId);
        pairCO.setToSessionId(toSessionId);
        pairCache.put(pairCO);
        PairCO pairCO1 = new PairCO();
        pairCO1.setSessionId(toSessionId);
        pairCO1.setToSessionId(sessionId);
        pairCache.put(pairCO1);
        return pairCO;
    }

    private void removePairCache(String sessionId,String toSessionId){
        PairCO pairCO = new PairCO();
        pairCO.setSessionId(sessionId);
        pairCO.setToSessionId(toSessionId);
        pairCache.delete(pairCO);
        PairCO pairCO1 = new PairCO();
        pairCO1.setSessionId(toSessionId);
        pairCO1.setToSessionId(sessionId);
        pairCache.delete(pairCO1);
    }

    /**
     * 随机生成一个session
     *
     * @return
     */
    private String getSessionSign() {
        int i1 = ran.nextInt(10);
        int i2 = ran.nextInt(10);
        int i3 = ran.nextInt(10);
        int i4 = ran.nextInt(10);
        return System.currentTimeMillis() + i1 + i2 + i3 + i4 + "";
    }

}
