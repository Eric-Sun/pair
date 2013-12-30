package com.h13.pair.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: sunbo
 * Date: 13-11-25
 * Time: 下午4:19
 * To change this template use File | Settings | File Templates.
 */
public class SessionNotInWaitingQueueException extends Exception {
    public static int ERROR_CODE = 1005;

    public SessionNotInWaitingQueueException(String msg) {
        super(msg);
    }

}
