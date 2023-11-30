package com.saltos.school.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class HolaKafkaCloudConsumer {

    public static void main(String[] args) throws Exception {

        final Properties props = new Properties();

        props.load(new FileInputStream("src/main/resources/cluster_1.properties"));

        props.put("client.id", "HolaKafkaCloudConsumer");
        props.put("group.id", "test1");
        props.put("auto.offset.reset", "earliest");
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