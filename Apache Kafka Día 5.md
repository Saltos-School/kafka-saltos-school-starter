# Apache Kafka Día 5

## Evolve an Avro schema

<https://docs.confluent.io/platform/current/schema-registry/fundamentals/schema-evolution.html#compatibility-types>

```json
{
  "doc": "Cuenta de Usuario.",
  "fields": [
    {
      "doc": "Nombre del usuario",
      "name": "nombre",
      "type": "string"
    },
    {
      "doc": "Apellido del usuario",
      "name": "apellido",
      "type": "string"
    },
    {
      "doc": "Edad del usuario",
      "name": "edad",
      "type": "int"
    },
    {
      "default": false,
      "doc": "Usuario bloqueado",
      "name": "bloqueado",
      "type": "boolean"
    }
  ],
  "name": "Usuario",
  "namespace": "com.saltos.school.kafka",
  "type": "record"
}
```

```json
{
  "doc": "Cuenta de Usuario.",
  "fields": [
    {
      "default": null,
      "doc": "Correo electrónico del usuario",
      "name": "email",
      "type": [
        "null",
        "string"
      ]
    },
    {
      "doc": "Nombre del usuario",
      "name": "nombre",
      "type": "string"
    },
    {
      "doc": "Apellido del usuario",
      "name": "apellido",
      "type": "string"
    },
    {
      "doc": "Edad del usuario",
      "name": "edad",
      "type": "int"
    },
    {
      "default": false,
      "doc": "Usuario bloqueado",
      "name": "bloqueado",
      "type": "boolean"
    }
  ],
  "name": "Usuario",
  "namespace": "com.saltos.school.kafka",
  "type": "record"
}
```

## Connectors

DataGen sample

## KafkaStreams

Código de ejemplo HolaKafkaStreams

## KSQL

Código de ejemplo compras.sql
