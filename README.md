# Proyecto: API de Gestión de Productos (Arquitectura Hexagonal)

Este es un proyecto de ejemplo que implementa una API RESTful para la gestión de productos, utilizando **Arquitectura Hexagonal** (también conocida como Puertos y Adaptadores) con Java y Spring Boot. El objetivo principal es demostrar una estructura de aplicación desacoplada, mantenible y fácilmente testeable.

## Descripción del Proyecto

La aplicación permite realizar operaciones CRUD (Crear, Leer, Actualizar, Eliminar) sobre una entidad de "Producto". Está diseñada siguiendo los principios de la Arquitectura Hexagonal para separar claramente la lógica de negocio (dominio) de las preocupaciones de infraestructura (frameworks, bases de datos, APIs externas, etc.).

## Tecnologías Utilizadas

* **Lenguaje:** Java 21 (o la versión que estés usando, ej: 17)
* **Framework Principal:** Spring Boot 3.x (o la versión que estés usando)
* **Módulos de Spring:**
    * Spring Web (para la API RESTful)
    * Spring Data JPA (para la persistencia)
* **Base de Datos (Ejemplo Inicial):** H2 Database (en memoria)
* **Herramienta de Construcción:** Apache Maven (o Gradle, si lo prefieres)
* **Control de Versiones:** Git
* **Arquitectura:** Hexagonal (Puertos y Adaptadores)
* **Pruebas:** JUnit 5, Mockito (se añadirán más adelante)
* **Contenerización (Futuro):** Docker

## Prerrequisitos

Antes de comenzar, asegúrate de tener instalado lo siguiente:

* JDK (Java Development Kit) - Versión 17 o superior (recomendado 21)
* Apache Maven - Versión 3.6.x o superior (o Gradle si se usa)
* Git

## Cómo Empezar

Sigue estos pasos para poner en marcha el proyecto en tu entorno local:

1.  **Clonar el repositorio:**
    ```bash
    git clone <URL_DEL_REPOSITORIO_REMOTO>
    # Ejemplo: git clone [https://github.com/tu-usuario/mi-aplicacion-hexagonal.git](https://github.com/tu-usuario/mi-aplicacion-hexagonal.git)
    ```

2.  **Navegar al directorio del proyecto:**
    ```bash
    cd mi-aplicacion-hexagonal
    ```

3.  **Construir el proyecto (con Maven):**
    Esto descargará las dependencias y compilará el código.
    ```bash
    mvn clean install
    ```
    *(Si usas Gradle, sería algo como `./gradlew build`)*

4.  **Ejecutar la aplicación (con Maven):**
    ```bash
    mvn spring-boot:run
    ```
    *(Si usas Gradle, sería `./gradlew bootRun`)*

    Alternativamente, puedes ejecutar el archivo JAR generado (después de `mvn clean install`):
    ```bash
    java -jar target/mi-aplicacion-0.0.1-SNAPSHOT.jar
    # El nombre del JAR puede variar según la configuración en tu pom.xml
    ```

Una vez iniciada, la API estará disponible (por defecto) en `http://localhost:8080`. Puedes consultar los endpoints disponibles en el código del controlador (`ProductoController.java`).

## Endpoints Principales (Ejemplo Inicial)

* `POST /api/v1/productos`: Crea un nuevo producto.
* `GET /api/v1/productos/{id}`: Obtiene un producto por su ID.
* `GET /api/v1/productos`: Lista todos los productos.
* `PUT /api/v1/productos/{id}`: Actualiza un producto existente.
* `DELETE /api/v1/productos/{id}`: Elimina un producto.

*(Estos endpoints se detallarán más a medida que se desarrollen).*

## Evolución del Proyecto

Este `README.md` se actualizará continuamente a medida que se añadan nuevas funcionalidades, se mejore la configuración y se documenten aspectos adicionales del proyecto como:

* Estructura detallada del proyecto.
* Guías de contribución.
* Configuración de la base de datos para entornos de desarrollo y producción.
* Estrategias de prueba.
* Instrucciones para la Dockerización.
* Documentación de la API (ej. Swagger/OpenAPI).

---

## Diagrama Alto Nivel

```mermaid
%% Diagrama de Arquitectura Hexagonal (Alto Nivel) - Versión Compatible
graph TD
    subgraph Mundo Exterior
        A[Cliente / Navegador]
    end

    subgraph "Capa de Infraestructura (Adaptadores)"
        B["Adaptador de Entrada\n(Driving Adapter)\nProductoController"]
        C["Adaptador de Salida\n(Driven Adapter)\nProductoRepositoryAdapter"]
    end

    subgraph "Core de la Aplicación (El Hexágono)"
        D["Puerto de Entrada\n(Input Port)\nProductoUseCase (Interfaz)"]
        E["Lógica de Aplicación y Dominio\nProductoUseCaseImpl\nProducto (Modelo)"]
        F["Puerto de Salida\n(Output Port)\nProductoRepositoryPort (Interfaz)"]
    end

    subgraph "Servicios Externos"
        G[Base de Datos]
    end

    %% --- Flujo de la Petición ---
    A -- Petición HTTP --> B
    B -- Llama al método del caso de uso --> D
    D -- Es implementado por --> E
    E -- Usa el puerto para persistir datos --> F
    F -- Es implementado por --> C
    C -- Realiza operación (SELECT, INSERT, etc.) --> G
```


## Diagrama de Secuencia

```mermaid
%% Diagrama de Secuencia para Crear un Producto (POST /api/v1/productos)
sequenceDiagram
    actor Cliente
    participant C as ProductoController
    participant M1 as ProductoApiMapper
    participant UC as ProductoUseCase
    participant RA as ProductoRepositoryAdapter
    participant M2 as ProductoPersistenceMapper
    participant SDR as ProductoSpringDataRepository
    participant DB as Base de Datos

    title Flujo de Creación de un Producto

    Cliente->>+C: POST /api/v1/productos<br/><i>(JSON con ProductoRequestDTO)</i>
    note right of C: El Controller recibe la petición HTTP.

    C->>M1: toDomain(requestDTO)
    note right of M1: Mapea el DTO de la API a un<br/>objeto de Dominio (Producto).
    M1-->>C: Devuelve objeto 'Producto' (sin id)

    C->>UC: crearProducto(nombre, precio)
    activate UC
    note right of UC: Se invoca el caso de uso (la lógica de la aplicación).
    
    UC->>RA: guardar(producto)
    activate RA
    note right of RA: El caso de uso llama al puerto de salida,<br/>invocando la implementación del adaptador.

    RA->>M2: toEntity(producto)
    note right of M2: El adaptador de persistencia mapea<br/>el objeto de Dominio a una Entidad JPA.
    M2-->>RA: Devuelve objeto 'ProductoEntity'

    RA->>SDR: save(productoEntity)
    activate SDR
    note right of SDR: El adaptador usa el repositorio de Spring Data<br/>para la operación de base de datos.
    
    SDR->>DB: INSERT INTO productos (...) VALUES (...)
    DB-->>SDR: Devuelve la fila guardada con el ID generado
    
    SDR-->>RA: Devuelve 'ProductoEntity' (¡ahora con id!)
    deactivate SDR
    
    RA->>M2: toDomain(productoEntityGuardado)
    M2-->>RA: Mapea la Entidad de vuelta a objeto de Dominio.
    RA-->>UC: Devuelve objeto 'Producto' (con id)
    deactivate RA
    
    UC-->>C: Devuelve 'Producto' guardado
    deactivate UC
    
    C->>M1: toResponseDTO(productoGuardado)
    note right of M1: Mapea el objeto de Dominio de vuelta<br/>a un DTO de respuesta para la API.
    M1-->>C: Devuelve 'ProductoResponseDTO'

    C-->>-Cliente: HTTP 201 Created<br/><i>(JSON con ProductoResponseDTO)</i>

```
