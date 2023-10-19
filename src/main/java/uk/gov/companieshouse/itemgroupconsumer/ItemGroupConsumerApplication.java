package uk.gov.companieshouse.itemgroupconsumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ItemGroupConsumerApplication {

    public static final String NAMESPACE = "item-group-consumer";

    public static void main(String[] args) {
        SpringApplication.run(ItemGroupConsumerApplication.class, args);
    }

}
