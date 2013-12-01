package com.h13.pair.controllers;

import com.h13.pair.cache.co.MessageCO;
import com.h13.pair.exceptions.IsNotPairException;
import com.h13.pair.services.MessageService;
import com.h13.pair.utils.DTOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.LinkedList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: sunbo
 * Date: 13-11-25
 * Time: 下午3:56
 * To change this template use File | Settings | File Templates.
 */
@RequestMapping("/message")
@Controller
public class MessageController {

    @Autowired
    MessageService messageService;


    @RequestMapping("/send")
    @ResponseBody
    public String send(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = request.getParameter("sessionId");
        String toSessionId = request.getParameter("toSessionId");
        String content = request.getParameter("c");
        try {
            messageService.send(sessionId, toSessionId, content);
        } catch (IsNotPairException e) {
            DTOUtils.getFailureResponse(request, response, sessionId, toSessionId, IsNotPairException.ERROR_CODE);
        }
        return DTOUtils.getOriginalResponse(request, response, sessionId, toSessionId);
    }


    @RequestMapping("/receive")
    @ResponseBody
    public String receive(HttpServletRequest request, HttpServletResponse response) {
        String sessionId = request.getParameter("sessionId");
        String toSessionId = request.getParameter("toSessionId");
        List<MessageCO> list = new LinkedList<MessageCO>();
        try {
            list = messageService.receive(toSessionId, sessionId);
        } catch (IsNotPairException e) {
            DTOUtils.getFailureResponse(request, response, sessionId, toSessionId, IsNotPairException.ERROR_CODE);
        }
        return DTOUtils.getSucessResponse(request, response, sessionId, toSessionId, list);
    }


}
