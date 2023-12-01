# Apache Kafka Día 3

## Usar Confluent Cloud

* Acceder a <https://confluent.cloud/signup> para crear una cuenta gratis o usar
  la cuenta de la clase que es `key@csaltos.com` y la contraseña es
  `GaudeixBarcelona.327`

* Crear un nuevo tópico sin esquema y probar desde la sección de mensajes que
  funciona

* Crear otro tópico con un esquema basado en Avro para los valores usando el
  siguiente contenido:

```json
{
  "type": "record",
  "namespace": "com.saltos.school.kafka",
  "name": "Usuario",
  "doc": "Cuenta de Usuario.",
  "fields": [
    {
      "name": "nombre",
      "type": "string",
      "doc": "Nombre del usuario"
    },
    {
      "name": "apellido",
      "type": "string",
      "doc": "Apellido del usuario"
    },
    {
      "name": "edad",
      "type": "int",
      "doc": "Edad del usuario"
    }
  ]
}
```

## Crear un productor usando Java

* Referirse al código en `ejemplos/HolaKafkaMaven` y ejecutar el programa
  `HolaKafkaProducer`

## Crear un consumidor usando Java

* Manteniendo ejecutando el productor del ejemplo anterior ahora ejecutar
  el programa `HolaKafkaConsumer`

Hacer cambios en el group ID del consumer, ejecutar varias veces y
analizar qué pasa cuando cae un broker de Kafka o se pausa el productor
o los consumidores.

## Rotación de datos con Kafka

Kafka puede conservar todos los datos que procesa pero normalmente por
optimización de recursos se rotan los datos planteando políticas de rotación
por tiempo y también por tamaño de uso en disco.

Se puede definir un valor por defecto de politicas de rotación de datos pero
también se los puede definir por tópico.

Existen tres modos de rotación de datos:

* Delete
* Compact
* Compact/Delete

El primer modo de `Delete` borra los mensajes que se desbordan de la política de
retención al usuar mucho espacio de disco o simplemente ser muy viejos y superar
el tiempo de retención configurado.

El segundo modo de `Compact` lo que hace es consolidar todos los datos por los
keys de cada uno de los mensajes, solo manteniendo el mensaje más reciendo por
cada key.

El tercer modo de `Compact/Delete` descarta los mensajes que todavía superan
las políticas de rotación de datos desde de hacer la consolidación de los datos
por keys.

Ejemplos de politicas de retención con `Compact` incluyen el tópico interno
llamado `__consumer_offsets` que se usa para saber en dónde va la ejecución de
cada grupo de consumidores y ya que está información es crucial para el sistema
no se usa el modo `Delete` si no el `Compact` para mantener los offsets más
recientes por grupo e consumidores intentando no perder datos.

Otro ejemplo puede ser un tópico que tienen logs de servidores que se utilizan
solo los de la reciente semana y se puede borrar los datos viejos sin problemas
en ese caso se pondría una configuración de retención de solo 1 semana para la
rotación de datos y se utilizaría el valor de `Delete`.

Un ejemplo adicional puede ser el usar un tópico de registro de eventos de las compras más recientes de los usuarios donde se marca un límite por disco de 1 terabyte y si se excede este espacio se usa un `Compact/Delete` en donde se
consolidan los mensajes por ID de usuario pero si aún así no alcanza el espacio
se borrarían los datos de las compras más recientes de los usuarios más antiguos
que posiblemente ya no están activos en el sistema.
 