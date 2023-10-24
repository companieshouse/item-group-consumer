package uk.gov.companieshouse.itemgroupconsumer;

import static uk.gov.companieshouse.itemgroupconsumer.TestUtils.ERROR_TOPIC;
import static uk.gov.companieshouse.itemgroupconsumer.TestUtils.INVALID_TOPIC;
import static uk.gov.companieshouse.itemgroupconsumer.TestUtils.MAIN_TOPIC;
import static uk.gov.companieshouse.itemgroupconsumer.TestUtils.RETRY_TOPIC;

import consumer.deserialization.AvroDeserializer;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CountDownLatch;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import uk.gov.companieshouse.itemgroupordered.ItemGroupOrdered;
import uk.gov.companieshouse.kafka.exceptions.SerializationException;
import uk.gov.companieshouse.kafka.serialization.SerializerFactory;

@TestConfiguration
public class TestKafkaConfig {

    @Bean
    CountDownLatch latch(@Value("${steps}") int steps) {
        return new CountDownLatch(steps);
    }

    @Bean
    KafkaConsumer<String, ItemGroupOrdered> testConsumer(
        @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        KafkaConsumer<String, ItemGroupOrdered> kafkaConsumer = new KafkaConsumer<>(
                Map.of(
                        ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers,
                        ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class,
                        ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, AvroDeserializer.class,
                        ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest",
                        ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false",
                        ConsumerConfig.GROUP_ID_CONFIG, UUID.randomUUID().toString()),
                new StringDeserializer(), new AvroDeserializer<>(ItemGroupOrdered.class));
        kafkaConsumer.subscribe(List.of(MAIN_TOPIC, ERROR_TOPIC, RETRY_TOPIC,
                INVALID_TOPIC));
        return kafkaConsumer;
    }

    @Bean
    KafkaProducer<String, ItemGroupOrdered> testProducer(
        @Value("${spring.kafka.bootstrap-servers}") String bootstrapServers) {
        return new KafkaProducer<>(
                Map.of(
                        ProducerConfig.ACKS_CONFIG, "all",
                        ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers),
                new StringSerializer(),
            (topic, data) -> {
                try {
                    return new SerializerFactory()
                        .getSpecificRecordSerializer(ItemGroupOrdered.class).toBinary(data); //creates a leading space
                } catch (SerializationException e) {
                    throw new RuntimeException(e);
                }
            });
    }
}