package com.h13.pair.services;

import com.h13.pair.cache.co.PairCO;
import com.h13.pair.exceptions.NoSessionForPairException;
import com.h13.pair.exceptions.SessionPairedException;
import com.h13.pair.helpers.SessionHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created with IntelliJ IDEA.
 * User: sunbo
 * Date: 13-11-26
 * Time: 下午7:46
 * To change this template use File | Settings | File Templates.
 */
@Service
public class SessionService {

    @Autowired
    SessionHelper sessionHelper;

    public String createSession() {
        String sessionId = sessionHelper.createSession();
        return sessionId;
    }

    public PairCO pairSession(String sessionId) throws SessionPairedException, NoSessionForPairException {
        return sessionHelper.doPair(sessionId);
    }

    public void closeSession(String sessionId, String toSessionId) {
        sessionHelper.close(sessionId, toSessionId);
    }

}
