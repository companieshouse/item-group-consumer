package uk.gov.companieshouse.itemgroupconsumer;

import java.util.Map;
import org.apache.kafka.clients.producer.ProducerInterceptor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import uk.gov.companieshouse.itemgroupordered.ItemGroupOrdered;

/**
 * Routes a message to the invalid letter topic if a non-retryable error has been thrown during
 * message processing.
 */
public class InvalidMessageRouter implements ProducerInterceptor<String, ItemGroupOrdered> {

  private MessageFlags messageFlags;
  private String invalidMessageTopic;

  @Override
  public ProducerRecord<String, ItemGroupOrdered>
  onSend(ProducerRecord<String, ItemGroupOrdered> producerRecord) {
    if (messageFlags.isRetryable()) {
      messageFlags.destroy();
      return producerRecord;
    } else {
      return new ProducerRecord<>(this.invalidMessageTopic, producerRecord.key(),
          producerRecord.value());
    }
  }

  @Override
  public void onAcknowledgement(RecordMetadata metadata, Exception exception) {
  }

  @Override
  public void close() {
  }

  @Override
  public void configure(Map<String, ?> configs) {
    this.messageFlags = (MessageFlags) configs.get("message.flags");
    this.invalidMessageTopic = (String) configs.get("invalid.message.topic");
  }
}
