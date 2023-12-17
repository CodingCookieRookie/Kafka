package alvin.kafka.consumer;

import alvin.kafka.Main;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

public class ConsumerConfig {

    private static final Logger log = LoggerFactory.getLogger(ConsumerConfig.class);
    private static final String BOOTSTRAP_SERVERS = "127.0.0.1:9092";
    private final String groupID = "consumerGroup1";

    private KafkaConsumer<String, String> consumer;

    public ConsumerConfig() {
        Properties properties = new Properties();
        properties.setProperty(org.apache.kafka.clients.consumer.ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.setProperty(org.apache.kafka.clients.consumer.ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(org.apache.kafka.clients.consumer.ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class.getName());
        properties.setProperty(org.apache.kafka.clients.consumer.ConsumerConfig.GROUP_ID_CONFIG, groupID);
        properties.setProperty(org.apache.kafka.clients.consumer.ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        consumer = new KafkaConsumer<>(properties);
    }

    public KafkaConsumer<String, String> getKafkaConsumer() {
        return this.consumer;
    }

    public void consumeEvent(String topicName) {
        this.consumer.subscribe(Arrays.asList(topicName));
        System.out.println("consume event");
        log.info("topic name, {}", topicName);
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(Duration.ofMillis(100));
            for (ConsumerRecord<String, String> record : records) {
                log.info(String.format("Key: %s    |    Value: %s", record.key(), record.value()));
                log.info(String.format("Partition: %d    |    Offset: %d", record.partition(), record.offset()));
            }
        }
    }

    public void asyncConsumeEvent(String topicName) {
        Thread t = new Thread() {
            @Override
            public void run() {
                consumeEvent(topicName);
            }
        };
        t.start();
    }
}
