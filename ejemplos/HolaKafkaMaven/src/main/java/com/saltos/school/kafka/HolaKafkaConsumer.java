package com.saltos.school.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class HolaKafkaConsumer {

    public static void main(String[] args) throws Exception {

        final Properties props = new Properties();

        props.put("client.id", "HolaKafkaConsumer");
        props.put("group.id", "test1");
        props.put("auto.offset.reset", "earliest");
        props.put("bootstrap.servers", "localhost:9092");
        props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        Duration pollTimeout = Duration.ofSeconds(1);

        try (Consumer<String, String> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(List.of("hola"));
            while (true) {
                ConsumerRecords<String, String> data = consumer.poll(pollTimeout);
                data.forEach(mensaje -> {
                    System.out.println("Key: " + mensaje.key());
                    System.out.println("Value: " + mensaje.value());
                    System.out.println("Offset: " + mensaje.offset());
                    System.out.println("Partition: " + mensaje.partition());
                    System.out.println("Topic: " + mensaje.topic());
                    System.out.println("Timestamp: " + mensaje.timestamp());
                });
            }
        }
    }
}