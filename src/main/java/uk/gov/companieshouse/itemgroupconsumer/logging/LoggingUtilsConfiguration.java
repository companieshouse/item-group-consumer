package uk.gov.companieshouse.itemgroupconsumer.logging;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;

@Configuration
public class LoggingUtilsConfiguration {

    public static final String APPLICATION_NAMESPACE = "item-group-consumer";

    @Bean
    Logger getLogger(){
        return LoggerFactory.getLogger(APPLICATION_NAMESPACE);
    }

    @Bean
    public LoggingUtils getLoggingUtils(Logger logger) {
        return new LoggingUtils(logger);
    }
}
