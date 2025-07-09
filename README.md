trabajoFinalJava - API REST de Gestión de Pedidos y Productos
API REST desarrollada con Spring Boot para la gestión de pedidos y productos. Permite administrar la información de productos y el ciclo de vida de los pedidos, incluyendo autenticación para proteger los recursos.

🚀 Tecnologías utilizadas
Java 21: Lenguaje de programación.

Spring Boot 3.2.5: Framework para construir aplicaciones Java robustas y escalables.

Spring Web: Módulo de Spring para el desarrollo de aplicaciones web y RESTful.

Spring Security: Framework para la autenticación y autorización.

Spring Data JPA: Abstracción para el acceso a datos y mapeo objeto-relacional.

Springdoc OpenAPI 2.5.0 (Swagger): Para la generación y visualización interactiva de la documentación de la API.

MySQL: Base de datos relacional.

Maven: Herramienta de automatización de construcción de proyectos.

MapStruct: Para el mapeo de objetos (DTOs).

⚙️ Configuración del proyecto
Sigue estos pasos para poner en marcha la aplicación en tu entorno local.

Clonar el repositorio:

Bash

git clone https://github.com/ridermanriquecueto/trabajoFinalJava.git
cd trabajoFinalJava
Configurar la base de datos MySQL:

Asegúrate de tener un servidor MySQL corriendo.

Crea una base de datos con el nombre trabajoFinalJava_db.

Actualiza el archivo src/main/resources/application.properties (o application.yml) con tus credenciales de base de datos. Si no tienes contraseña para root, la configuración actual es correcta.

Properties

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
Nota: spring.jpa.hibernate.ddl-auto=update creará automáticamente las tablas necesarias si no existen.

Configurar Java Development Kit (JDK):

Asegúrate de tener JDK 21 instalado en tu sistema.

Configura tu variable de entorno JAVA_HOME para que apunte a la ruta de tu instalación de JDK 21.

Asegúrate de que %JAVA_HOME%\bin esté al principio de tu variable de entorno Path de Windows para que Maven utilice la versión correcta de Java.

Construir el proyecto:

Bash

./mvnw clean install
Este comando compilará el código, ejecutará los tests y generará el archivo .jar empaquetable en la carpeta target/.

Ejecutar la aplicación:

Bash

./mvnw spring-boot:run
La aplicación se iniciará y estará disponible en tu máquina local.

📑 Documentación Swagger / OpenAPI
Una vez que la aplicación esté corriendo, puedes acceder a la documentación interactiva de la API para explorar y probar los endpoints:

UI interactiva de Swagger:

http://localhost:8080/swagger-ui.html
Aquí podrás ver y probar cada endpoint (GET, POST, PUT, DELETE) directamente desde el navegador.

Definición OpenAPI en JSON:

http://localhost:8080/v3/api-docs
Esta URL proporciona la definición completa de la API en formato JSON, útil para herramientas como Postman o Insomnia.

🔑 Seguridad
La API utiliza Spring Security y requerirá autenticación para acceder a la mayoría de los recursos (excepto quizás los endpoints de autenticación y Swagger UI, si así se configuran).

Endpoint de autenticación: Para iniciar sesión y obtener un token JWT, usa el endpoint POST /api/auth/login en Swagger UI o con tu cliente REST favorito.

Usuarios y roles: La configuración de usuarios y roles, así como las reglas de acceso, se definen en las clases de configuración de seguridad del proyecto (ej. SecurityConfig.java) y, potencialmente, en tu base de datos o en application.properties.

📂 Estructura del proyecto
src/main/java/com/miTrabajo/  # O 'com.martinps' si lo prefieres así
├── controller              -> Controladores REST (manejan las solicitudes HTTP)
├── service                 -> Lógica de negocio principal
├── repository              -> Acceso a base de datos (interfaces JPA)
├── model                   -> Modelos de datos (entidades JPA, enums)
├── dto                     -> Objetos de transferencia de datos (para entrada/salida de la API)
├── exception               -> Clases personalizadas para manejo de errores
├── config                  -> Configuraciones generales (Swagger, seguridad, MapStruct mappers)
└── security                -> Clases relacionadas con la seguridad (JWT, etc.)
📌 Consideraciones adicionales
Asegúrate de que tu instancia de MySQL esté funcionando y sea accesible desde tu aplicación.

Verifica que los paquetes en tu código Java (especialmente los de test) sean consistentes. Por ejemplo, si tu aplicación principal está en com.miTrabajo, tus tests deberían estar en com.miTrabajo o configurados para encontrar la clase principal (@SpringBootTest(classes = ProjectFinalApplication.class)).

El proyecto utiliza MapStruct para el mapeo entre entidades y DTOs, lo que simplifica la conversión de datos.

Para pruebas unitarias y de integración se utiliza spring-boot-starter-test.