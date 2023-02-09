package uk.gov.companieshouse.itemgroupconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.gov.companieshouse.itemgroupconsumer.logging.LoggingUtils;

import javax.annotation.PostConstruct;
import java.util.Map;

@SpringBootApplication
public class ItemGroupConsumerApplication {

    private final LoggingUtils logger;

    public ItemGroupConsumerApplication(LoggingUtils logger) {
        this.logger = logger;
    }

    public static void main(String[] args) {
        SpringApplication.run(ItemGroupConsumerApplication.class, args);
    }

    /**
     * Note this method has been added only to verify structured logging configuration.
     */
    @PostConstruct
    public void startupApplication() {
        final Map<String, Object> map = logger.createLogMap();
        map.put(LoggingUtils.KEY, "Test key value for structured log message.");
        logger.getLogger().debug("App started up.", map);
    }

}
