package com.saltos.school.kafka;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Properties;

public class HolaKafkaConsumer {

    public static void main(String[] args) {
        Properties props = new Properties();

        props.setProperty("client.id", "app1node1");
        props.setProperty("group.id", "test1");
        props.setProperty("bootstrap.servers", "localhost:9092,localhost:9095,localhost:9097");
        props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("enable.auto.commit", "false");
        props.setProperty("auto.offset.reset", "earliest"); // earliest o latest

        Consumer<String, String> consumer = new KafkaConsumer<>(props);
        consumer.subscribe(List.of("hola"));
        Duration pollTimeout = Duration.ofSeconds(1);
        while(true) {
            ConsumerRecords<String, String> registros = consumer.poll(pollTimeout);
            registros.forEach(registro -> {
                System.out.println("Mensaje recibido");
                System.out.println("Topic: " + registro.topic());
                System.out.println("Key: " + registro.key());
                System.out.println("Value: " + registro.value());
                System.out.println("Partition: " + registro.partition());
                System.out.println("Offset: " + registro.offset());
                System.out.println("Timestamp: " + new Date(registro.timestamp()));
            });
            consumer.commitSync();
        }
    }

}
