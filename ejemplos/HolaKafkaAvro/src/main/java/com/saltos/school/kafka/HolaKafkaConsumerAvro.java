package com.saltos.school.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.io.FileInputStream;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

public class HolaKafkaConsumerAvro {

    public static void main(String[] args) throws Exception {

        final Properties props = new Properties();

        props.load(new FileInputStream("src/main/resources/cluster_1.properties"));

        props.put("client.id", "HolaKafkaConsumerAvro");
        props.put("group.id", "test1");
        props.put("auto.offset.reset", "earliest");
        props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("value.serializer", "io.confluent.kafka.serializers.KafkaAvroSerializer");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "io.confluent.kafka.serializers.KafkaAvroDeserializer");

        Duration pollTimeout = Duration.ofSeconds(1);

        try (Consumer<String, Usuario> consumer = new KafkaConsumer<>(props)) {
            consumer.subscribe(List.of("holaavro"));
            while (true) {
                ConsumerRecords<String, Usuario> registros = consumer.poll(pollTimeout);
                registros.forEach(registro -> {
                    System.out.println("Key: " + registro.key());
                    System.out.println("Value: " + registro.value());
                    System.out.println("Offset: " + registro.offset());
                    System.out.println("Partition: " + registro.partition());
                    System.out.println("Topic: " + registro.topic());
                    System.out.println("Timestamp: " + registro.timestamp());
                });
            }
        }
    }
}