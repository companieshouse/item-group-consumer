package uk.gov.companieshouse.itemgroupconsumer;

import static uk.gov.companieshouse.itemgroupconsumer.ItemGroupConsumerApplication.APPLICATION_NAME_SPACE;

import consumer.deserialization.AvroDeserializer;
import java.util.Map;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.ErrorHandlingDeserializer;
import uk.gov.companieshouse.itemgroupordered.ItemGroupOrdered;
import uk.gov.companieshouse.kafka.exceptions.SerializationException;
import uk.gov.companieshouse.kafka.serialization.SerializerFactory;
import uk.gov.companieshouse.logging.Logger;
import uk.gov.companieshouse.logging.LoggerFactory;
import uk.gov.companieshouse.logging.util.DataMap;

@Configuration
@EnableKafka
public class Config {

    @Bean
    public ConsumerFactory<String, ItemGroupOrdered>
    consumerFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        return new DefaultKafkaConsumerFactory<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, ErrorHandlingDeserializer.class,
                        ErrorHandlingDeserializer.KEY_DESERIALIZER_CLASS, StringDeserializer.class,
                        ErrorHandlingDeserializer.VALUE_DESERIALIZER_CLASS, AvroDeserializer.class,
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false"),
                new StringDeserializer(),
                new ErrorHandlingDeserializer<>(new AvroDeserializer<>(ItemGroupOrdered.class)));
    }

    @Bean
    // In the unlikely event that a SerializationException should occur, the RuntimeException used to wrap
    // it is swallowed by spring/spring-kafka to which our exception types would be meaningless.
    @SuppressWarnings("squid:S112")
    public ProducerFactory<String, ItemGroupOrdered>
    producerFactory(@Value("${spring.kafka.bootstrap-servers}") String bootstrapServers,
                    MessageFlags messageFlags,
                    @Value("${invalid_message_topic}") String invalidMessageTopic) {
        return new DefaultKafkaProducerFactory<>(
                Map.of(
                        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                        ProducerConfig.ACKS_CONFIG, "all",
                        ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                        ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                        ProducerConfig.INTERCEPTOR_CLASSES_CONFIG, InvalidMessageRouter.class.getName(),
                        "message.flags", messageFlags,
                        "invalid.message.topic", invalidMessageTopic,
                        "enable.idempotence", false),
                new StringSerializer(),
                (topic, data) -> {
                    try {
                        return new SerializerFactory().getSpecificRecordSerializer(ItemGroupOrdered.class)
                            .toBinary(data); //creates a leading space
                    } catch (SerializationException e) {
                        var dataMap = new DataMap.Builder()
                            .topic(topic)
                            .kafkaMessage(data.toString())
                            .build()
                            .getLogMap();
                        getLogger().error("Caught SerializationException serializing kafka message.", dataMap);
                        throw new RuntimeException(e);
                    }
                }
        );
    }

    @Bean
    public KafkaTemplate<String, ItemGroupOrdered>
    kafkaTemplate(ProducerFactory<String, ItemGroupOrdered> producerFactory) {
        return new KafkaTemplate<>(producerFactory);
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, ItemGroupOrdered>
    kafkaListenerContainerFactory(ConsumerFactory<String, ItemGroupOrdered> consumerFactory,
                                  @Value("${consumer.concurrency}") Integer concurrency) {
        ConcurrentKafkaListenerContainerFactory<String, ItemGroupOrdered> factory =
            new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory);
        factory.setConcurrency(concurrency);
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.RECORD);
        return factory;
    }

    @Bean
    Logger getLogger(){
        return LoggerFactory.getLogger(APPLICATION_NAME_SPACE);
    }
}

