# Apache Kafka Día 2

## Mejorar el rendimiento de un tópico con más particiones

Incrementar el número de particiones de un tópico es una operación que se puede
hacer en caliente pero es recomendable hacerla en frío.

Cuando se hace la operación en caliente, los productores y consumidores pueden
perder datos y hay que tener cuidado durante la operación para poder volver
a emitir los mensajes que se hayan podido perder.

Para evitar posibles perdidas de datos es mejor hacer el cambio en frío lo que
implica detener los productores y consumidores, hacer el cambio y volver a
reiniciar los productores y consumidores nuevamente.

* Como práctica podemos incremente el numero de particiones de nuestro primer
  tópico llamado `hola` con el siguiente commando:

```bash
cd /kafka/bin/windows
./kafka-topics.bat --bootstrap-server localhost:9095 --alter --topic hola --partitions 5
```

* Tomar en cuenta que no se puede reducir el número de particiones porque eso
  implicaría directamente perdidas de datos, si se necesita imperativamente
  reducir el número de particiones hay que detener todos los productores y
  consumidores y rejeecutar todos los mensajes en un nuevo topico con la
  configuración de mensajes que se necesite.

* Opcionalmente se puede utilizar streams para intentar hacer la operación en
  caliente donde los mensajes del tópico original pasan a un tópico nuevo
  con la reconfiguración de particiones mientras los consumidores y productores
  se migran al nuevo tópico reconfigurado.

## Mejorar la disponibilidad incrementando el número de réplicas de un tópico

Es recomendable tener al menos una réplica de cada partición de los tópicos,
esto se configura mediante el replication factor, lastimosamente para hacer el
cambio no funciona directamente este comando:

```bash
./kafka-topics.bat --bootstrap-server localhost:9095 --alter --topic hola --partitions 5 --replication-factor 3
```

El cambio hay que hacer lo explícitamente ya que una vez creado el tópico,
Kafka no sabe cómo se tienen que distribuir las copias pero se puede usar
archivos de configuración de replicas para lo cual crear un archivo de
reparticionado llamado `hola.json` con el siguiente contenido:

```json
{
  "version": 1,
  "partitions": [
      { "topic" : "hola", "partition" : 0, "replicas" : [0, 1, 2] },
      { "topic" : "hola", "partition" : 1, "replicas" : [0, 1, 2] },
      { "topic" : "hola", "partition" : 2, "replicas" : [0, 1, 2] },
      { "topic" : "hola", "partition" : 3, "replicas" : [0, 1, 2] },
      { "topic" : "hola", "partition" : 4, "replicas" : [0, 1, 2] }
  ]
}
```

* Aplicar esta nueva configuración de replicas con el commando:

```bash
./kafka-reassign-partitions.bat --bootstrap-server localhost:9095 --reassignment-json-file hola.json --execute
```

> Con tópicos grandes en producción usar la opción `--throttle` para que se
  haga en segundo plano con menor impacto para las producers y consumers activos

* Chequear si la nueva configuración está lista con el comando:

```bash
./kafka-reassign-partitions.bat --bootstrap-server localhost:9095 --reassignment-json-file hola.json --verify
```

* Cuando la nueva configuración esté lista inspeccionar el nuevo tópico con el comando

```bash
./kafka-topics.bat --bootstrap-server localhost:9095 --describe --topic hola
```

## Lograr la alta disponibilidad configuran un mínimo de replicas sanas

Para esto se cambia el valor de configuración de insync min replicas con el
commando:

```bash
./kafka-configs.sh --bootstrap-server localhost:9092 --alter --topic hola --add-config min.insync.replicas=2
```

## Posibles configuraciones de replicacion factor y min insync replicas

* Se puede tener un replication factor de 1 y un min insync replicas también en
  1 que se puede usar para desarrollo en local ya que si hay el más mínimo fallo
  se pueden perder datos

* Se puede usar un replication factor de 3 con un min insync replicas también de
  3 pero eso hace rígido al sistema ya que no se puede perder ningún segmento,
  esto es los datos tienen varias copias pero todas las copias son necesarias
  para considerar a los datos activos y válidos

* En producción es recomendable usar el replication factor de 3 y el min insync
  replicas de 2, esto hace posible que hayan varias copias pero si se pierde una
  el sistema pueda continuar mientras se intenta recuperar

* En casos de datos críticos es recomendable usar un replication factor de 5 y
  un min insync replicas de 3

  > No es recomendable usar valores de replication factor grandes como 20 o 50
    ya que eso saturaría el sistema innecesariamente, tal vez el caso más
    extremo que se puede configurar es un replication factor de 7 y un min
    insync replicas de 4 pero sería para casos de producción de datos altamente
    críticos y que cuenten con buenos recursos (este caso permite perder hasta
    3 copias y el sistema continuaría adelante)

  > La formula para calcular los posibles valores útiles es `N div 2 + 1` siendo
    `N` el replication factor y `div` la división de enteros sin decimales.

## Leer datos por grupos de consumidores

Normalmente en Kafka no se hacen lecturas con consumidores aislados sino que
los consumidores conforman aplicaciones que ejecutan en paralelo, por ejemplo
una aplicación de reportes mensuales que lee datos del mes anterior y produce
datos en formato PDF podría ser un ejemplo de un aplicación que es un consumidor
de un tópico de Kafka que puede tener tres instancias donde si cae una pasa a
funcionar la otra, como una especie de mini cluster de aplicación de reportes
mensuales.

Para leer un topico de forma orquestada usando grupos de consumidores se utiliza
el consumer group ID que desde consola se pueden ejecutar así:

```bash
cd /kafka/bin/windows
./kafka-console-consumer.bat --bootstrap-server localhost:9095 --topic hola --group test1
```

> Probar crear mensajes con un productor usando del tópico `hola` mientras se
  ejecutan dos consumidores que usen el mismo group ID `test1` y observar como
  funciona, pausar un consumidor y reanudarlo, crear más mensajes mientras el
  otro consumidor está activo y adicionalmente detener los dos consumidores
  mientras se crean más mensajes con el productor y analizar lo que pasa cuando
  los consumidores del mismo grupo vuelven a funcionar

En Kafka cada aplicación debe tener su group ID que normalmente está versionado
y por entornos con nombres como `prod_reporte_pdf_v1` por ejemplo.

En Kafka se pueden usar varias aplicaciones escribiendo y leyendo a varios
tópicos para lo cual simplemente las aplicaciones que sean consumidores pueden
declarar valores de group ID diferentes.

Probar a ejecutar un consumidor del grupo `test1` y otro del `test2` y probar
lo que pasa si ejecutan al mismo tiempo leyendo datos nuevos de un productor y
adicionalmente analizar qué pasando cuando se detiene uno y reanuda el otro.

En general cada consumidor con valores de group ID diferentes lleva su propia
secuencia y eso se usa para cuando son aplicaciones diferentes mientras que
se usan valores de group ID iguales cuando son lectores que forman parte de
una misma aplicación redundada para mejor alta disponibilidad.
