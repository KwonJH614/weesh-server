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
}