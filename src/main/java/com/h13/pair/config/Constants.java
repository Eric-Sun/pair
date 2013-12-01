package com.h13.pair.config;

public class Constants {

    public static class ResponseStatus {
        public static int SUCCESS = 0;
        public static int FAILURE = 1;
    }


    public static class SESSION {
        public static int WAIT = 0;
        public static int DONE = 1;
        public static int CLOSED = 2;
        public static int DELETED = 3;
    }

    public static String DEFAULT_TO_SESSION_ID = "-1";
}
