trabajoFinalJava
API REST desarrollada con Spring Boot para gestión de [poné acá lo que maneja tu proyecto, ej: usuarios, productos, operaciones bancarias, etc.].

🚀 Tecnologías utilizadas
Java 21

Spring Boot 3.2.5

Spring Web

Spring Security

Spring Data JPA

Springdoc OpenAPI 2.5.0 (Swagger)

MySQL

Maven

⚙️ Configuración del proyecto
Clonar el repositorio:

bash
Copiar
Editar
git clone https://github.com/ridermanriquecueto/trabajoFinalJava.git
cd trabajoFinalJava
Configurar las variables de entorno (por ejemplo en application.properties o application.yml):

properties
Copiar
Editar
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
Construir el proyecto:

bash
Copiar
Editar
./mvnw clean install
Ejecutar la aplicación:

bash
Copiar
Editar
./mvnw spring-boot:run
La API estará disponible en:

arduino
Copiar
Editar
http://localhost:8080
📑 Documentación Swagger / OpenAPI
Accedé a la UI interactiva de Swagger en:

bash
Copiar
Editar
http://localhost:8080/swagger-ui/index.html
La definición OpenAPI en JSON está disponible en:

bash
Copiar
Editar
http://localhost:8080/v3/api-docs
🔑 Seguridad
La API está protegida con Spring Security y puede requerir autenticación. La configuración de usuarios y roles está en application.properties o en la configuración de seguridad del proyecto.

📂 Estructura del proyecto
rust
Copiar
Editar
src/main/java/com/martinps/trabajoFinalJava
├── controller      -> Controladores REST
├── service         -> Lógica de negocio
├── repository      -> Acceso a base de datos
├── entity          -> Modelos de datos (JPA)
└── config          -> Configuración (Swagger, Security, etc.)
