# Proyecto Base Implementando Clean Architecture

## Antes de Iniciar

Empezaremos por explicar los diferentes componentes del proyectos y partiremos de los componentes externos, continuando con los componentes core de negocio (dominio) y por último el inicio y configuración de la aplicación.

Lee el artículo [Clean Architecture — Aislando los detalles](https://medium.com/bancolombia-tech/clean-architecture-aislando-los-detalles-4f9530f35d7a)

# Arquitectura

![Clean Architecture](https://miro.medium.com/max/1400/1*ZdlHz8B0-qu9Y-QO3AXR_w.png)

## Domain

Es el módulo más interno de la arquitectura, pertenece a la capa del dominio y encapsula la lógica y reglas del negocio mediante modelos y entidades del dominio.

## Usecases

Este módulo gradle perteneciente a la capa del dominio, implementa los casos de uso del sistema, define lógica de aplicación y reacciona a las invocaciones desde el módulo de entry points, orquestando los flujos hacia el módulo de entities.

## Infrastructure

### Helpers

En el apartado de helpers tendremos utilidades generales para los Driven Adapters y Entry Points.

Estas utilidades no están arraigadas a objetos concretos, se realiza el uso de generics para modelar comportamientos
genéricos de los diferentes objetos de persistencia que puedan existir, este tipo de implementaciones se realizan
basadas en el patrón de diseño [Unit of Work y Repository](https://medium.com/@krzychukosobudzki/repository-design-pattern-bc490b256006)

Estas clases no puede existir solas y debe heredarse su compartimiento en los **Driven Adapters**

### Driven Adapters

Los driven adapter representan implementaciones externas a nuestro sistema, como lo son conexiones a servicios rest,
soap, bases de datos, lectura de archivos planos, y en concreto cualquier origen y fuente de datos con la que debamos
interactuar.

### Entry Points

Los entry points representan los puntos de entrada de la aplicación o el inicio de los flujos de negocio.

## Application

Este módulo es el más externo de la arquitectura, es el encargado de ensamblar los distintos módulos, resolver las dependencias y crear los beans de los casos de use (UseCases) de forma automática, inyectando en éstos instancias concretas de las dependencias declaradas. Además inicia la aplicación (es el único módulo del proyecto donde encontraremos la función “public static void main(String[] args)”.

**Los beans de los casos de uso se disponibilizan automaticamente gracias a un '@ComponentScan' ubicado en esta capa.**

## Requerimientos del Microservicio

El microservicio se encarga de gestionar los usuarios exponiend servicios a traves de REST para la creacion, consulta, y filtrado de usuarios.

### Stack Tecnológico

*  **Herramienta de compilación:** Gradle - version 8.4
*  **Lenguaje de compilación:** Java - version 17
* Spring WebFlux.
* JUnit5

### Inicialización del Microservicio

* Iniciar las instancias de redis, localStack y la base de datos postgres, para obtener los datos de conexion.

Para que la aplicación funcione correctamente es necesario:

* Crear las variables de entorno:
  * DB_HOST host de la base de datos, por defecto localhost.
  * DB_PORT puerto de la base de datos, por defecto 5432.
  * DB_DATABASE nombre de la base de datos.
  * DB_SCHEMA nombre del esquema de la base de datos.
  * DB_USERNAME nombre de usuario de la base de datos.
  * DB_USER_PASSWORD contraseña del usuario de la base de datos.
  * USER_WEB_CLIENT_URL url del servicio externo para consultar la informacion de usuarios.
  * REDIS_HOST host del servicio de redis, por defecto localhost.
  * REDIS_PORT puerto del servicio de redis, por defecto 6379.
  * AWS_REGION  Región de AWS, por defecto us-east-1.
  * AWS_SQS_URL URL de la cola SQS a donde se notificara la creacion del usuario.
  * AWS_SQS_ENDPOINT Endpoint de la cola SQS para localStack


* Crear en la base de datos el esquema y las tablas necesarias, para ello se debe ejecutar el script SQL `DB_QUERY.SQL`.

* Crear la cola SQS localStack.
    
    ```bash
    aws sqs create-queue --queue-name user-create-queue --profile localstack --endpoint-url=http://localhost:4566
    ```

* Ejecutar la aplicación desde la clase `MainApplication.java` ubicada en el módulo `app-service`.

* Acceder a la documentación de la API en [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html).


