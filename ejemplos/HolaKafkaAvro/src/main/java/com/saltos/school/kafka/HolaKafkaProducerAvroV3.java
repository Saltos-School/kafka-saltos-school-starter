package com.saltos.school.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class HolaKafkaProducerAvroV3 {

    public static void main(String[] args) throws InterruptedException, IOException {

        final Properties props = new Properties();

        props.load(new FileInputStream("src/main/resources/cluster_0.properties"));

        props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.setProperty("value.serializer", "io.confluent.kafka.streams.serdes.avro.ReflectionAvroSerializer");
        props.setProperty("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.setProperty("value.deserializer", "io.confluent.kafka.streams.serdes.avro.ReflectionAvroDeserializer");
        props.setProperty("use.schema.id", "100010");
        props.setProperty("schema.format", "avro");
        props.setProperty("auto.register.schemas", "false");
        props.setProperty("id.compatibility.strict", "true");

        // Schema schema = Schema.parse(FileInputStream("usuarios.avsc"));

        try (Producer<String, Usuario> producer = new KafkaProducer<>(props)) {
            final String topic = "holapaulavro";
            for (int i = 0; true; i++, Thread.sleep(1000L)) {
                final String key = "usuario" + i;
                final Usuario value = new Usuario();
                value.setNombre("Usuario" + i);
                value.setApellido("Apellido" + i);
                value.setEdad(i);
                value.setBloqueado(i % 2 == 0);
                value.setEmail("usuario" + i + "@email.com");
                producer.send(
                        new ProducerRecord<>(topic, key, value),
                        (metadata, exception) -> {
                            if (exception == null) {
                                System.out.println("Valor insertado " + key + ", " + value);
                                System.out.println("En el topic: " + metadata.topic());
                                System.out.println("En la particion: " + metadata.partition());
                                System.out.println("Con el offset: " + metadata.offset());
                                System.out.println("Con la marca de tiempo: " + metadata.timestamp());
                            } else {
                                exception.printStackTrace();
                            }
                        });
            }
        }

    }
}