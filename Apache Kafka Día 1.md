# Apache Kafka Día 1

## Instalación de Kafka en local

* Asegurarse tener JDK 8 o JDK 11 instalado con el JAVA_HOME configurado

* Descargar Kafka desde <https://kafka.apache.org/downloads> (actualmente usamos
  la versión de Kafka 3.6.0 con Scala 2.13)

* Desempaquetar `kafka_2.13-3.6.0.tgz` y copiarlo en la raíz o en alguna
  carpeta de trabajo con el nombre de `kafka`

## Iniciar Kafka en local

* Cambiar el valor de `dataDir` del archivo `kafka/config/zookeeper.properties`
  a `/kafka/zookeeper/data`

* Iniciar Zookeeper con los comandos:

```bash
cd /kafka
bin/windows/zookeeper-server-start.bat config/zookeeper.properties
```

* Cambiar el valor de `log.dirs` del archivo `kafka/config/server.properties` a
  `/kafka/data`

* Iniciar Kafka con los comandos:

```bash
cd /kafka
bin/windows/kafka-server-start.bat config/server.properties
```

> Agregar el directorio de trabajo a los comandos de `/kafka` si se ha copiado
  los archivos a otro directorio (en Windows las rutas se cambian
  automáticamente a direcciones con la unidad local usando contrabarras)

> Si hay problemas de permisos al iniciar `kafka-server-start.bat` se puede
  editar el archivo `kafka-server-start.bat` para comentar la llamada a `wmic`
  y usar solo las opciones para Windows de 64 bits

## Chequear la instalación

* Lanzar Zookeeper CLI para inspeccionar la configuración de nuestro nuevo Kafka
  usando los commandos:

```bash
cd /kafka
bin/windows/zookeeper-shell.bat localhost:2181
```

  > Dentro de Zookeeper CLI ejecutar el comando `ls /` y `ls /brokers` para
    verificar que la instalación de Kafka tiene la configuración creada
    adecuadamente.

## Crear nuevos tópicos de pruebas

* Crear un primer tópico con los comandos:

```bash
cd /kafka/bin/windows
kafka-topics.bat --bootstrap-server localhost:9092 --create --topic hola
```

* A continuación inspeccionar el nuevo tópico con los siguientes commandos
  en la misma carpeta:

```bash
kafka-topics.bat --bootstrap-server localhost:9092 --list
kafka-topics.bat --bootstrap-server localhost:9092 --describe --topic hola
```

* Crear un tópico nuevo con 5 particiones con el commando:

```bash
kafka-topics.bat --bootstrap-server localhost:9092 --create --topic hola5 --partitions 5
```

> Al inspeccionar este nuevo tópico veremos que tiene particiones de la 0 a la 4

* Crear un tópico con 50 particiones con el commando

```bash
kafka-topics.bat --bootstrap-server localhost:9092 --create --topic hola50 --partitions 50
```

## Agregar más servidores de Kafka

* Copiar la carpeta `/kafka` en nuevas carpetas con el nombre `/kafka2` y
  `/kafka3`

* Borrar las carpetas de datos de `kafka2/data` y `kafka3/data`

  > No borrar la carpeta de datos originales del primer Kafka, solo de las copias

* Editar los archivos `kafka2/config/server.properties` y
  `kafka3/config.server.properties` para que los valores `log.dirs` apunten a
  las nuevas rutas de datos que tendra cada nuevo broker de Kafka

* Adicionalmente, en los mismos archivos de configuración cambiar los puertos
  de inicio de Kafka con los siguientes valores:

  Para `/kafka2/config/server.properties` usar el valor `listeners=PLAINTEXT://:9095`

  Para `/kafka3/config/server.properties` usar el valor `listeners=PLAINTEXT://:9097`

  > Asegurarse las nuevas entradas de puertos quedan descomentadas (el valor
    original por defecto está en un comentario)

* En los mismos archivos de `server.properties` de los tres servidores de Kafka
  asegurarse que el primer servidor en `/kafka` tiene el valor de `broker.id`
  en `0`, el segudo servidor en `/kafka2` el valor de `broker.id` en `1` y el
  tercer servidor de Kafka de `/kafka3` tiene el valor de `broker.id` en `2`

> Se pueden poner IDs que empiecen en `1` o incluso que no tengan secuencia
  alguna con valores como `5`, `20` y `15`, la única restricción es que sean
  numeros enteros positivos diferentes y sobre todo, una vez que ya se generaron
  datos de una encarnación de broker de Kafka, no se puede cambiar el ID a
  posteriori a menos que se reconstruya todos los datos

## Iniciar los nuevos servidores de Kafka

* Revisando que los nuevos valores de `server.properties` de los tres servidores
  de Kafka tengan diferentes IDs, diferentes puertos y diferentes carpetas de
  datos procedemos a inciarlos con los comandos:

```bash
cd /kafka2
bin/windows/kafka-server-start.bat config/server.properties
```

```bash
cd /kafka3
bin/windows/kafka-server-start.bat config/server.properties
```

  > No es necesario levantar nuevos servidores de Zookeeper, reciclaremos el
    servicio de Zookeeper del primer servidor (en production con datos reales
    deberíamos tener al menos 3 servidores de Zookeeper en réplica y se
    recomienda tener 5 o 7 para servicios críticos importantes)

* Con los nuevos servidores de Kafka podemos volver a inspeccionar el contenido
  de Zookeeper y dentro de `/brokers/ids` deberíamos verlos nuevos servidores

## Crear un nuevo tópico usando los tres servidores

* Ahora que tenemos un cluster de Kafka en local con tres brokers podremos crear
  topicos con replicas usando comandos como por ejemplo:

```bash
cd /kafka/bin/windows
kafka-topics.bat --bootstrap-server localhost:9092 --create --topic hola5bis --partitions 5 --replication-factor 3
```

* Podemos inspeccionar este nuevo tópico para ver las nuevas particiones con sus
  respectivas replicas con el comando:

```bash
cd /kafka3/bin/windows
kafka-topics.bat --bootstrap-server localhost:9095 --describe --topic hola5bis
```

> Con este comando estamos usando el cliente Kafka desde los archivos del tercer
  servidor y conectándonos al segundo broker y es posible hacerlo porque todo
  funciona en red entre varias máquinas virtuales de JVM

## Grabar nuevos datos en el cluster de Kaka

* Ejecutar un productor de consola con el comando:

```bash
cd /kafka/bin/windows
kafka-console-producer.bat --bootstrap-server localhost:9095 --topic hola5bis
```

> Al ejecutar el producer podremos poner datos en formato texto uno por linea
  y para salir precionar `Ctrl-C`

## Leer los nuevos datos del cluster de Kaka

* Ejecutar un consumidor de consola con el comando:

```bash
cd /kafka/bin/windows
kafka-console-consumer.bat --bootstrap-server localhost:9095 --topic hola5bis
```

* Este comando no presentará nada porque por defecto los consumidores presentan
  solo nuevos mensajes así que podemos presionar `Ctrl-C` y ejecutar este
  comando:

```bash
cd /kafka/bin/windows
kafka-console-consumer.bat --bootstrap-server localhost:9095 --topic hola5bis --from-beginning
```

> Ahora sí deberíamos ver los datos desde el principio
