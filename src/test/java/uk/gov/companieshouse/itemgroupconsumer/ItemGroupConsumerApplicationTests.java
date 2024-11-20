package uk.gov.companieshouse.itemgroupconsumer;

import static java.util.Arrays.stream;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;
import static uk.gov.companieshouse.itemgroupconsumer.environment.EnvironmentVariablesChecker.RequiredEnvironmentVariables.BACKOFF_DELAY;

import java.util.Arrays;
import java.util.Objects;

import org.junit.contrib.java.lang.system.EnvironmentVariables;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import uk.gov.companieshouse.itemgroupconsumer.config.TestConfig;
import uk.gov.companieshouse.itemgroupconsumer.environment.EnvironmentVariablesChecker;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test_main_positive.properties")
@Import(TestConfig.class)
@ActiveProfiles("test_main_positive")
class ItemGroupConsumerApplicationTests {

    private static final String TOKEN_STRING_VALUE = "token value";

    private static EnvironmentVariables ENVIRONMENT_VARIABLES;

    @BeforeAll
    static void setUp() {
        ENVIRONMENT_VARIABLES = new EnvironmentVariables();
        stream(EnvironmentVariablesChecker.RequiredEnvironmentVariables.values()).forEach(variable -> {
            if (Objects.requireNonNull(variable) == EnvironmentVariablesChecker.RequiredEnvironmentVariables.SERVER_PORT) {
                ENVIRONMENT_VARIABLES.set(variable.getName(), "8080");
            } else {
                ENVIRONMENT_VARIABLES.set(variable.getName(), TOKEN_STRING_VALUE);
            }
        });
    }

    @AfterAll
    static void tearDown() {
        final String[] AllEnvironmentVariableNames =
            Arrays.stream(EnvironmentVariablesChecker.RequiredEnvironmentVariables.class.getEnumConstants())
                .map(Enum::name)
                .toArray(String[]::new);
        ENVIRONMENT_VARIABLES.clear(AllEnvironmentVariableNames);
    }

    @SuppressWarnings("squid:S2699") // at least one assertion
    @DisplayName("Context loads")
    @Test
    void contextLoads() {
    }

    @DisplayName("runs app when all required environment variables are present")
    @Test
    void runsAppWhenAllRequiredEnvironmentVariablesPresent() {

        try (final var app = mockStatic(SpringApplication.class)) {
            app.when(() -> SpringApplication.run(ItemGroupConsumerApplication.class, new String[0])).thenReturn(null);

            ItemGroupConsumerApplication.main(new String[]{});

            app.verify(() -> SpringApplication.run(ItemGroupConsumerApplication.class, new String[0]));
        }

    }

    @DisplayName("does not run app when a required environment variable is missing")
    @Test
    void doesNotRunAppWhenRequiredEnvironmentVariableMissing() {

        ENVIRONMENT_VARIABLES.clear(BACKOFF_DELAY.getName());

        try (final var app = mockStatic(SpringApplication.class)) {

            ItemGroupConsumerApplication.main(new String[]{});

            app.verify(() -> SpringApplication.run(ItemGroupConsumerApplication.class, new String[0]), times(0));
        }

    }

}
