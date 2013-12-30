package com.h13.pair.config;

public class Constants {

    public static class ResponseStatus {
        /**
         * 请求成功
         */
        public static int SUCCESS = 0;
        /**
         * 请求失败
         */
        public static int FAILURE = 1;
    }


    public static class SESSION {
        /**
         * 正在等待匹配
         */
        public static int WAIT = 0;
        /**
         * 已经匹配成功
         */
        public static int DONE = 1;
        /**
         * 解除匹配
         */
        public static int CLOSED = 2;
        /**
         *
         */
        public static int DELETED = 3;
    }

    public static String DEFAULT_TO_SESSION_ID = "-1";
}
