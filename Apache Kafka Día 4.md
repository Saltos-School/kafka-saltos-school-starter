# Apache Kafka Día 4

## Reseteo de consumer groups offsets

```bash
./kafka-consumer-groups.sh --bootstrap-server pkc-4r087.us-west2.gcp.confluent.cloud:9092 --command-config cluster_1.properties --list

./kafka-consumer-groups.sh --bootstrap-server pkc-4r087.us-west2.gcp.confluent.cloud:9092 --command-config cluster_1.properties --topic holaavro --group test1 --reset-offsets --shift-by 0 --export --dry-run

./kafka-consumer-groups.sh --bootstrap-server pkc-4r087.us-west2.gcp.confluent.cloud:9092 --command-config cluster_1.properties --topic holaavro --group test1 --reset-offsets --from-file offsets.txt --dry-run

./kafka-consumer-groups.sh --bootstrap-server pkc-4r087.us-west2.gcp.confluent.cloud:9092 --command-config cluster_1.properties --topic holaavro --group test1 --reset-offsets --from-file offsets.txt --execute
```

## Crear un productor usando Avro

* Referirse al código en `ejemplos/HolaKafkaAvro` y ejecutar el programa
  `HolaKafkaProducerAvro`

## Crear un consumer usando Avro

* Referirse al código en `ejemplos/HolaKafkaAvro` y ejecutar el programa
  `HolaKafkaConsumerAvro`
