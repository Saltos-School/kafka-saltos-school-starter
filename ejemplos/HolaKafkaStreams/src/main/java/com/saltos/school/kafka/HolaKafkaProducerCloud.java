package com.saltos.school.kafka;


import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.FileInputStream;
import java.util.Date;
import java.util.Properties;

public class HolaKafkaProducerCloud {
    public static void main(String[] args) throws Exception {

        Properties props = new Properties();

        props.load(new FileInputStream("src/main/resources/cluster_0.properties"));

        // this.getClass().getClassLoader().getResourceAsStream("cluster_0.properties");
        props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

        try (Producer<String, String> producer = new KafkaProducer<>(props)) {
            for (int i = 0; true; i++) {
                Thread.sleep(2000);
                //Headers headers = List.of(new Header("tiempo", "creado el " + System.currentTimeMillis));
                ProducerRecord<String, String> producerRecord = new ProducerRecord<>("hola", "key_" + i, "Prueba " + i/* , headers*/);
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