package com.h13.pair.exceptions;

/**
 * Created with IntelliJ IDEA.
 * User: sunbo
 * Date: 13-11-25
 * Time: 下午4:19
 * To change this template use File | Settings | File Templates.
 */
public class SessionException extends Exception {
    public static int ERROR_CODE = 1002;

    public SessionException(String msg) {
        super(msg);
    }

}
