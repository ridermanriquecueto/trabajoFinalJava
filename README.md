# trabajoFinalJava - API REST de Gestión de Pedidos y Productos

API REST desarrollada con Spring Boot para la gestión de pedidos y productos. Permite administrar la información de productos y el ciclo de vida de los pedidos, incluyendo autenticación para proteger los recursos.

🚀 **Tecnologías utilizadas**
* **Java 21**: Lenguaje de programación.
* **Spring Boot 3.2.5**: Framework para construir aplicaciones Java robustas y escalables.
* **Spring Web**: Módulo de Spring para el desarrollo de aplicaciones web y RESTful.
* **Spring Security**: Framework para la autenticación y autorización.
* **Spring Data JPA**: Abstracción para el acceso a datos y mapeo objeto-relacional.
* **Springdoc OpenAPI 2.5.0 (Swagger)**: Para la generación y visualización interactiva de la documentación de la API.
* **MySQL**: Base de datos relacional.
* **Maven**: Herramienta de automatización de construcción de proyectos.
* **MapStruct**: Para el mapeo de objetos (DTOs).
* **HTML, CSS, JavaScript**: Tecnologías base para la interfaz de usuario (frontend).

⚙️ **Configuración del proyecto**
Sigue estos pasos para poner en marcha la aplicación en tu entorno local.

1.  **Clonar el repositorio:**
    ```bash
    git clone [https://github.com/ridermanriquecueto/trabajoFinalJava.git](https://github.com/ridermanriquecueto/trabajoFinalJava.git)
    cd trabajoFinalJava
    ```

2.  **Configurar la base de datos MySQL:**
    Asegúrate de tener un servidor MySQL corriendo.
    Crea una base de datos con el nombre `trabajoFinalJava_db`.
    Actualiza el archivo `src/main/resources/application.properties` (o `application.yml`) con tus credenciales de base de datos. Si no tienes contraseña para root, la configuración actual es correcta.

    ```properties
    spring.application.name=trabajoFinalJava
    spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
    spring.datasource.url=jdbc:mysql://localhost:3306/trabajoFinalJava_db?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires&allowPublicKeyRetrieval=true
    spring.datasource.username=root
    spring.datasource.password=
    spring.jpa.generate-ddl=false
    spring.jpa.hibernate.ddl-auto=update
    spring.jpa.show-sql=true
    server.port=8080
    spring.jpa.open-in-view=false
    springdoc.api-docs.path=/v3/api-docs
    springdoc.swagger-ui.path=/swagger-ui.html
    ```
    *Nota*: `spring.jpa.hibernate.ddl-auto=update` creará automáticamente las tablas necesarias si no existen.

3.  **Configurar Java Development Kit (JDK):**
    Asegúrate de tener JDK 21 instalado en tu sistema.
    Configura tu variable de entorno `JAVA_HOME` para que apunte a la ruta de tu instalación de JDK 21.
    Asegúrate de que `%JAVA_HOME%\bin` esté al principio de tu variable de entorno `Path` de Windows para que Maven utilice la versión correcta de Java.

4.  **Construir el proyecto:**
    ```bash
    ./mvnw clean install
    ```
    Este comando compilará el código, ejecutará los tests y generará el archivo `.jar` empaquetable en la carpeta `target/`.

5.  **Ejecutar la aplicación:**
    ```bash
    ./mvnw spring-boot:run
    ```
    La aplicación se iniciará y estará disponible en tu máquina local.

📑 **Documentación Swagger / OpenAPI**
Una vez que la aplicación esté corriendo, puedes acceder a la documentación interactiva de la API para explorar y probar los endpoints:

* **UI interactiva de Swagger**:
    `http://localhost:8080/swagger-ui.html`
    Aquí podrás ver y probar cada endpoint (GET, POST, PUT, DELETE) directamente desde el navegador.

* **Definición OpenAPI en JSON**:
    `http://localhost:8080/v3/api-docs`
    Esta URL proporciona la definición completa de la API en formato JSON, útil para herramientas como Postman o Insomnia.

🖥️ **Interfaz de Usuario (Frontend)**
Este proyecto incluye una interfaz de usuario básica desarrollada con HTML, CSS y JavaScript, que se sirve directamente desde el backend de Spring Boot.

* **Acceso a la Interfaz de Usuario**:
    Una vez que la aplicación Spring Boot esté en ejecución, puedes acceder al frontend abriendo tu navegador y navegando a:
    `http://localhost:8080/`
    Desde aquí, podrás interactuar con la aplicación, realizar pedidos y ver el historial.

🔑 **Seguridad**
La API utiliza Spring Security y requerirá autenticación para acceder a la mayoría de los recursos (excepto quizás los endpoints de autenticación y Swagger UI, si así se configuran).

* **Endpoint de autenticación**: Para iniciar sesión y obtener un token JWT, usa el endpoint `POST /api/auth/login` en Swagger UI o con tu cliente REST favorito.
* **Usuarios y roles**: La configuración de usuarios y roles, así como las reglas de acceso, se definen en las clases de configuración de seguridad del proyecto (ej. `SecurityConfig.java`) y, potencialmente, en tu base de datos o en `application.properties`. Para desarrollo, se ha configurado un usuario en memoria (`adminuser` / `1234`).

📂 **Estructura del proyecto**

src/main/java/com/miTrabajo/  # O 'com.martinps' si lo prefieres así
├── controller              -> Controladores REST (manejan las solicitudes HTTP)
├── service                 -> Lógica de negocio principal
├── repository              -> Acceso a base de datos (interfaces JPA)
├── model                   -> Modelos de datos (entidades JPA, enums)
├── dto                     -> Objetos de transferencia de datos (para entrada/salida de la API)
├── exception               -> Clases personalizadas para manejo de errores
├── config                  -> Configuraciones generales (Swagger, seguridad, MapStruct mappers)
└── security                -> Clases relacionadas con la seguridad (JWT, etc.)