package uk.gov.companieshouse.itemgroupconsumer.kafka;

import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.annotation.RetryableTopic;
import org.springframework.kafka.retrytopic.DltStrategy;
import org.springframework.kafka.retrytopic.SameIntervalTopicReuseStrategy;
import org.springframework.messaging.Message;
import org.springframework.retry.annotation.Backoff;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.itemgroupconsumer.exception.RetryableException;
import uk.gov.companieshouse.itemgroupconsumer.service.Service;
import uk.gov.companieshouse.itemgroupconsumer.service.ServiceParameters;
import uk.gov.companieshouse.itemgroupordered.ItemGroupOrdered;

/**
 * Consumes messages from the configured main Kafka topic.
 */
@Component
public class Consumer {

    private final Service service;
    private final MessageFlags messageFlags;

    public Consumer(Service service, MessageFlags messageFlags) {
        this.service = service;
        this.messageFlags = messageFlags;
    }

    /**
     * Consume a message from the main Kafka topic.
     *
     * @param message A message containing a payload.
     */
    @KafkaListener(
            id = "${consumer.group_id}",
            containerFactory = "kafkaListenerContainerFactory",
            topics = "${consumer.topic}",
            groupId = "${consumer.group_id}"
    )
    @RetryableTopic(
            attempts = "${consumer.max_attempts}",
            autoCreateTopics = "false",
            backoff = @Backoff(delayExpression = "${consumer.backoff_delay}"),
            dltTopicSuffix = "-error",
            dltStrategy = DltStrategy.FAIL_ON_ERROR,
            sameIntervalTopicReuseStrategy = SameIntervalTopicReuseStrategy.SINGLE_TOPIC,
            include = RetryableException.class
    )
    public void consume(Message<ItemGroupOrdered> message) {
        try {
            service.processMessage(new ServiceParameters(message.getPayload()));
        } catch (RetryableException e) {
            messageFlags.setRetryable(true);
            throw e;
        }
    }
}
