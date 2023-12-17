package alvin.kafka.producer;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;

public class ProducerConfig {

    private static final String BOOTSTRAP_SERVERS = "127.0.0.1:9092";

    private final KafkaProducer<String, String> kafkaProducer;
    public ProducerConfig() {
        Properties properties = new Properties();
        properties.setProperty(org.apache.kafka.clients.producer.ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        properties.setProperty(org.apache.kafka.clients.producer.ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        properties.setProperty(org.apache.kafka.clients.producer.ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class.getName());
        kafkaProducer = new KafkaProducer<>(properties);
    }

    public KafkaProducer<String, String> getKafkaProducer() {
        return this.kafkaProducer;
    }

    public void sendProducerRecord(String topicName, String msg) {
        ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, msg);
        kafkaProducer.send(producerRecord);
    }

    public void loopAsyncSendProducerRecord(String topicName) {
        Thread t = new Thread() {
            @Override
            public void run() {
                int id = 0;
                while (true) {
                    id++;
                    ProducerRecord<String, String> producerRecord = new ProducerRecord<>(topicName, "sending message " + id);
                    kafkaProducer.send(producerRecord);
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        getKafkaProducer().flush();
                        getKafkaProducer().close();
                        throw new RuntimeException(e);
                    }
                }
            }
        };
        t.start();

    }
}
