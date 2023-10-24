package uk.gov.companieshouse.itemgroupconsumer;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test_main_positive.properties")
@Import(TestKafkaConfig.class)
@ActiveProfiles("test_main_positive")
class ItemGroupConsumerApplicationTests {

    @SuppressWarnings("squid:S2699") // at least one assertion
    @DisplayName("Context loads")
    @Test
    void contextLoads() {
    }

}
