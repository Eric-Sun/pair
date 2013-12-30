package com.h13.pair.helpers;

import com.h13.pair.cache.co.PairCO;
import com.h13.pair.cache.service.PairCache;
import com.h13.pair.cache.service.WaitingQueueCache;
import com.h13.pair.daos.SessionDAO;
import com.h13.pair.exceptions.NoSessionForPairException;
import com.h13.pair.exceptions.SessionException;
import com.h13.pair.exceptions.SessionNotInWaitingQueueException;
import com.h13.pair.exceptions.SessionPairedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger LOG = LoggerFactory.getLogger(SessionHelper.class);
    private static Random ran = new Random();
    @Autowired
    SessionDAO sessionDAO;
    @Autowired
    WaitingQueueCache waitingQueueCache;
    @Autowired
    PairCache pairCache;


    /**
     * 检测是否已经成为一对了
     *
     * @param fromId
     * @param toId
     * @return
     */
    public boolean isPair(String fromId, String toId) {
        PairCO pair = pairCache.get(fromId, toId);
        if (pair != null) {
            return true;
        }
        return false;
    }

    /**
     * 查看是否在等待队列中，如果在的话，返回true
     *
     * @param sessionId
     * @return
     */
    public boolean inWaitingQueue(String sessionId) {
        return waitingQueueCache.check(sessionId);
    }

    /**
     * 从等待队列中去掉对应的sessionId
     *
     * @param sessionId
     * @return
     */
    public boolean quitWaitingQueue(String sessionId) {
        return waitingQueueCache.delete(sessionId);
    }


    /**
     * 创建一个session，然后处于等待的状态，并放入到等待队列中
     *
     * @throws SessionException
     */
    public String createSession() {
        String sessionId = getSessionSign();
        sessionDAO.create(sessionId);
        boolean b = waitingQueueCache.put(sessionId);
        if (b) {
            LOG.info("create session successfully. sessionId=" + sessionId);
        } else {
            LOG.info("create session fail. sessionId=" + sessionId);
        }
        return sessionId;
    }

    public PairCO doPair(String sessionId) throws SessionPairedException, NoSessionForPairException {
        // 先从队列中去掉自己
        boolean b = waitingQueueCache.delete(sessionId);
        if (!b) {
            LOG.debug("remove waiting queue. sessionId=" + sessionId);
            throw new SessionPairedException("sessionId[" + sessionId + "] have been paired.");
        }
        LOG.debug("remove waiting queue. sessionId=" + sessionId);
        // 从队列中找到匹配的id
        String toSessionId = waitingQueueCache.get();
        LOG.debug("remove waiting queue. toSessionId=" + toSessionId);
        if (toSessionId == null) {
            // 如果没有找到的话，把自己放入队列中
            waitingQueueCache.put(sessionId);
            LOG.debug("no session for pair. sessionId=" + sessionId + " toSessionId=" + toSessionId);
            throw new NoSessionForPairException("no session for pair.");
        }

        // 创建双方的关系
        PairCO pairCO = addPairCache(sessionId, toSessionId);
        sessionDAO.doPair(sessionId, toSessionId);
        sessionDAO.doPair(toSessionId, sessionId);
        LOG.info("add pair. sessionId=" + sessionId + " toSessionId=" + toSessionId);
        LOG.info("add pair. sessionId=" + toSessionId + " toSessionId=" + sessionId);
        return pairCO;
    }

    public void close(String sessionId, String toSessionId) {
        sessionDAO.close(sessionId);
        sessionDAO.close(toSessionId);
        removePairCache(sessionId, toSessionId);
        LOG.info("close pair. sessionId=" + sessionId + " toSessionId=" + toSessionId);
        LOG.info("close pair. sessionId=" + sessionId + " toSessionId=" + toSessionId);
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

    private void removePairCache(String sessionId, String toSessionId) {
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

    public void quitWait(String sessionId) throws SessionNotInWaitingQueueException {
        if (inWaitingQueue(sessionId)) {
            // 在等待队列当中
            quitWaitingQueue(sessionId);
            sessionDAO.close(sessionId);
        } else {
            throw new SessionNotInWaitingQueueException("sessionId=" + sessionId);
        }
    }


}
