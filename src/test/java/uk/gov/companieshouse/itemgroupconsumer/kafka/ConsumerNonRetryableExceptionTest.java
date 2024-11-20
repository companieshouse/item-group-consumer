package uk.gov.companieshouse.itemgroupconsumer.kafka;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static uk.gov.companieshouse.itemgroupconsumer.util.TestConstants.ITEM_GROUP_ORDERED;
import static uk.gov.companieshouse.itemgroupconsumer.util.TestUtils.ERROR_TOPIC;
import static uk.gov.companieshouse.itemgroupconsumer.util.TestUtils.INVALID_TOPIC;
import static uk.gov.companieshouse.itemgroupconsumer.util.TestUtils.MAIN_TOPIC;
import static uk.gov.companieshouse.itemgroupconsumer.util.TestUtils.RETRY_TOPIC;

import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.springframework.test.context.ActiveProfiles;
import java.time.Duration;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import uk.gov.companieshouse.itemgroupconsumer.exception.NonRetryableException;
import uk.gov.companieshouse.itemgroupconsumer.service.Service;
import uk.gov.companieshouse.itemgroupconsumer.service.ServiceParameters;
import uk.gov.companieshouse.itemgroupconsumer.util.TestUtils;
import uk.gov.companieshouse.itemgroupordered.ItemGroupOrdered;

@SpringBootTest
@ActiveProfiles("test_main_nonretryable")
class ConsumerNonRetryableExceptionTest extends AbstractKafkaIntegrationTest {

    @Autowired
    private KafkaProducer<String, ItemGroupOrdered> testProducer;
    @Autowired
    private KafkaConsumer<String, ItemGroupOrdered> testConsumer;

    @Autowired
    private CountDownLatch latch;

    @MockBean
    private Service service;

    @BeforeEach
    public void drainKafkaTopics() {
        testConsumer.poll(Duration.ofSeconds(1));
    }

    @Test
    void testRepublishToInvalidMessageTopicIfNonRetryableExceptionThrown() throws InterruptedException {
        //given
        doThrow(NonRetryableException.class).when(service).processMessage(any());

        //when
        testProducer.send(new ProducerRecord<>(MAIN_TOPIC, 0, System.currentTimeMillis(), "key",
                ITEM_GROUP_ORDERED));
        if (!latch.await(5L, TimeUnit.SECONDS)) {
            fail("Timed out waiting for latch");
        }
        ConsumerRecords<?, ?> consumerRecords = KafkaTestUtils.getRecords(testConsumer, Duration.ofMillis(10000L), 2);

        //then
        assertThat(TestUtils.noOfRecordsForTopic(consumerRecords, MAIN_TOPIC)).isEqualTo(1);
        assertThat(TestUtils.noOfRecordsForTopic(consumerRecords, RETRY_TOPIC)).isZero();
        assertThat(TestUtils.noOfRecordsForTopic(consumerRecords, ERROR_TOPIC)).isZero();
        assertThat(TestUtils.noOfRecordsForTopic(consumerRecords, INVALID_TOPIC)).isEqualTo(1);
        verify(service).processMessage(new ServiceParameters(ITEM_GROUP_ORDERED));
    }
}