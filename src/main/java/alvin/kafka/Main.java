package alvin.kafka;

import alvin.kafka.consumer.ConsumerConfig;
import alvin.kafka.producer.ProducerConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

    private static final Logger log = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        log.info("starting kafka demo");

        ConsumerConfig consumerConfig = new ConsumerConfig();
        consumerConfig.asyncConsumeEvent("demo_topic");

        ProducerConfig producerConfig = new ProducerConfig();
        producerConfig.loopAsyncSendProducerRecord("demo_topic");




    }
}