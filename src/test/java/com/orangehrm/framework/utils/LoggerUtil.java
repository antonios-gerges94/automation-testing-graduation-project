package com.orangehrm.framework.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class LoggerUtil {
    private static final Logger LOGGER = LogManager.getLogger(LoggerUtil.class);

    private LoggerUtil() { }

    public static void info(String message) { LOGGER.info(message); }
    public static void error(String message, Throwable throwable) { LOGGER.error(message, throwable); }
}
