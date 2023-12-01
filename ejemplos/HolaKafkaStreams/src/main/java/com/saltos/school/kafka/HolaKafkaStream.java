package com.saltos.school.kafka;

import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.Produced;

import java.util.Properties;
import java.io.FileInputStream;

public class HolaKafkaStream {

  public static void main(String[] args) throws Exception {

    Properties props = new Properties();

    props.load(new FileInputStream("src/main/resources/cluster_0.properties"));

    props.setProperty("application.id", "HolaKafkaStream");
    props.setProperty("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
    props.setProperty("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

    // paul@csaltos.com

    //Serde<Usuario> usuarioSerde ...
    //Serde<Compras> comprasSerde ...
    Serde<String> stringSerde = Serdes.String();
    StreamsBuilder builder = new StreamsBuilder();
    builder
        .stream("hola", Consumed.with(stringSerde, stringSerde))
        .peek((k, v) -> System.out.println("Mensaje nuevo: " + v))
        .mapValues(s -> s.toUpperCase())
        //.filter(s -> s.length > 20)
        .peek((k, v) -> System.out.println("Guardando mensaje: " + v))
        .to("hola2", Produced.with(stringSerde, stringSerde));

    Topology topology = builder.build();

    KafkaStreams kafkaStreams = new KafkaStreams(topology, props);

    kafkaStreams.start();

    Thread.sleep(120000);

    kafkaStreams.close();

  }
}