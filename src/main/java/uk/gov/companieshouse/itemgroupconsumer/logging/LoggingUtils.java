package uk.gov.companieshouse.itemgroupconsumer.logging;

import org.apache.commons.lang.exception.ExceptionUtils;
import uk.gov.companieshouse.logging.Logger;

import java.util.HashMap;
import java.util.Map;

public class LoggingUtils {

    private final Logger logger;

    public LoggingUtils(Logger logger) {
        this.logger = logger;
    }

    public Map<String, Object> createLogMap() {
        return new HashMap<>();
    }

    public void logIfNotNull(Map<String, Object> logMap, String key, Object loggingObject) {
        if (loggingObject != null) {
            logMap.put(key, loggingObject);
        }
    }

    public static  Throwable getRootCause(final Exception exception) {
        final var rootCause = ExceptionUtils.getRootCause(exception);
        return rootCause != null ? rootCause : exception;
    }

    public Logger getLogger() {
        return logger;
    }
}