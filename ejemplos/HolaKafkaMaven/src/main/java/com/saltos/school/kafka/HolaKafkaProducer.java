package com.saltos.school.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;

public class HolaKafkaProducer {

    public static void main(String[] args) throws Exception {

        final Properties props = new Properties();

        props.setProperty("bootstrap.servers", "localhost:9092");
        props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            final String topic = "hola";
            for (int i = 0; true; i++) {
                Thread.sleep(1000L);
                final String key = "key_" + i;
                final String value = "value_" + i;
                producer.send(
                        new ProducerRecord<>(topic, key, value),
                        (metadata, exception) -> {
                            if (exception == null) {
                                System.out.println("Mensaje insertado " + key + ", " + value);
                                System.out.println("Tópico: " + metadata.topic());
                                System.out.println("Partición: " + metadata.partition());
                                System.out.println("Offset: " + metadata.offset());
                                System.out.println("Timestamp: " + metadata.timestamp());
                            } else {
                                exception.printStackTrace();
                            }
                        });
            }
        }

    }
}