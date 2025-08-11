package com.example.weesh.core.foundation.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoggingUtil {
    private static final Logger logger = LoggerFactory.getLogger(LoggingUtil.class);

    public static void info(String message) {
        logger.info(message);
    }
    public static void error(String message, Throwable t) {
        logger.error(message, t);
    }
    public static void debug(String message) {
        logger.debug(message);
    }
    public static void warn(String message) {
        logger.warn(message);
    }

    public static void error(String s, String requestURI, String message) {
        logger.error(requestURI, message, s);
    }

    public static void infoSetVelue(String message, String username) {
        logger.info(message, username);
    }
}