package uk.gov.companieshouse.itemgroupconsumer;

import io.cucumber.spring.CucumberContextConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@CucumberContextConfiguration
@AutoConfigureMockMvc
@ActiveProfiles("test_main_positive")
@Import({TestConfig.class, KafkaContainerConfig.class})
public class Configuration {
}
