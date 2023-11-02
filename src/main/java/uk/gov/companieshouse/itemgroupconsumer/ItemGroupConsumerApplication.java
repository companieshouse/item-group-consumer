package uk.gov.companieshouse.itemgroupconsumer;

import static uk.gov.companieshouse.itemgroupconsumer.EnvironmentVariablesChecker.allRequiredEnvironmentVariablesPresent;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ItemGroupConsumerApplication {

    public static final String NAMESPACE = "item-group-consumer";

    public static void main(String[] args) {
        if (allRequiredEnvironmentVariablesPresent()) {
            SpringApplication.run(ItemGroupConsumerApplication.class, args);
        }
    }

}
