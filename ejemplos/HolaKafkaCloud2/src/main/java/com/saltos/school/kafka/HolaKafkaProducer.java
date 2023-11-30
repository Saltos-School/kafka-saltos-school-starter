package com.saltos.school.kafka;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Date;
import java.util.Properties;

public class HolaKafkaProducer {
    public static void main(String[] args) throws Exception {

        Properties props = new Properties();

        props.setProperty("bootstrap.servers", "localhost:9092,localhost:9095,localhost:9097");
        props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("acks", "all"); // 0, 1, all

        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            for (int i = 0; true; i++) {
                Thread.sleep(2000);
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>("hola", "key_" + i, "Prueba " + i);
                producer.send(producerRecord,
                        (metadata, exception) -> {
                            if (exception != null) {
                                System.err.println("Error al insertar el mensaje");
                                exception.printStackTrace();
                            } else {
                                System.out.println("Mensaje enviado");
                                System.out.println("Partition: " + metadata.partition());
                                System.out.println("Offset: " + metadata.offset());
                                System.out.println("Timestamp: " + new Date(metadata.timestamp()));
                            }
                        });
            }
        }

    }
}