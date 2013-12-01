package com.h13.pair.controllers;

import com.h13.pair.cache.co.PairCO;
import com.h13.pair.config.Constants;
import com.h13.pair.exceptions.NoSessionForPairException;
import com.h13.pair.exceptions.SessionPairedException;
import com.h13.pair.services.SessionService;
import com.h13.pair.utils.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created with IntelliJ IDEA.
 * User: sunbo
 * Date: 13-11-26
 * Time: 下午4:00
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/session")
@Controller
public class SessionController {
    @Autowired
    SessionService sessionService;


    @RequestMapping("/create")
    @ResponseBody
    public String create(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = null;
        PairCO pairCO = null;
        try {
            sessionId = sessionService.createSession();
            pairCO = sessionService.pairSession(sessionId);
            return DTOUtils.getSucessResponse(request, response, sessionId, pairCO.getToSessionId());
        } catch (SessionPairedException e) {
            return DTOUtils.getFailureResponse(request, response, sessionId, Constants.DEFAULT_TO_SESSION_ID, SessionPairedException.ERROR_CODE);
        } catch (NoSessionForPairException e) {
            return DTOUtils.getFailureResponse(request, response, sessionId, Constants.DEFAULT_TO_SESSION_ID, NoSessionForPairException.ERROR_CODE);
        }
    }

    @RequestMapping("/pair")
    @ResponseBody
    public String pair(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = null;
        PairCO pairCO = null;
        try {
            sessionId = request.getParameter("sessionId");
            pairCO = sessionService.pairSession(sessionId);
            return DTOUtils.getSucessResponse(request, response, sessionId, pairCO.getToSessionId());
        } catch (SessionPairedException e) {
            return DTOUtils.getFailureResponse(request, response, sessionId, Constants.DEFAULT_TO_SESSION_ID, SessionPairedException.ERROR_CODE);
        } catch (NoSessionForPairException e) {
            return DTOUtils.getFailureResponse(request, response, sessionId, Constants.DEFAULT_TO_SESSION_ID, NoSessionForPairException.ERROR_CODE);
        }
    }


    @RequestMapping("/close")
    @ResponseBody
    public String close(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = null;
        String toSessionId = null;
        sessionId = request.getParameter("sessionId");
        toSessionId = request.getParameter("toSessionId");
        sessionService.closeSession(sessionId, toSessionId);
        return DTOUtils.getSucessResponse(request, response, sessionId, toSessionId);
    }


}
