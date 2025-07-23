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

HTML, CSS, JavaScript: Tecnologías base para la interfaz de usuario (frontend).

⚙️ Configuración del proyecto
Sigue estos pasos para poner en marcha la aplicación en tu entorno local.

Clonar el repositorio:

Bash

git clone https://github.com/ridermanriquecueto/trabajoFinalJava.git
cd trabajoFinalJava
Configurar la base de datos MySQL:
Asegúrate de tener un servidor MySQL corriendo.
Crea una base de datos con el nombre projectfinal_db.
Actualiza el archivo src/main/resources/application.properties (o application.yml) con tus credenciales de base de datos. Si no tienes contraseña para root, la configuración actual es correcta.

Properties

spring.application.name=trabajoFinalJava
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/projectfinal_db?useSSL=false&serverTimezone=America/Argentina/Buenos_Aires&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=
spring.jpa.generate-ddl=false
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
server.port=8080
spring.jpa.open-in-view=false
springdoc.api-docs.path=/v3/api-docs
springdoc.swagger-ui.path=/swagger-ui.html
Nota: spring.jpa.hibernate.ddl-auto=update creará automáticamente las tablas necesarias si no existen (como pedido, linea_pedido, producto, users, roles, etc.).

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

Endpoints CRUD Principales
La API proporciona las siguientes operaciones CRUD (Crear, Leer, Actualizar, Borrar) para la gestión de recursos:

Productos (/api/productos):

GET /api/productos: Leer todos los productos.

GET /api/productos/{id}: Leer un producto específico por su ID.

POST /api/productos: Crear un nuevo producto.

PUT /api/productos/{id}: Actualizar un producto existente por su ID.

DELETE /api/productos/{id}: Borrar un producto por su ID.

Pedidos (/api/pedidos):

GET /api/pedidos: Leer todos los pedidos.

GET /api/pedidos/{id}: Leer un pedido específico por su ID.

POST /api/pedidos: Crear un nuevo pedido.

PUT /api/pedidos/{id}: Actualizar el estado o detalles de un pedido existente por su ID.

DELETE /api/pedidos/{id}: Borrar un pedido por su ID.

Verificación Manual de la Base de Datos (Opcional)
Para verificar directamente la interacción con la base de datos y cómo se registran los pedidos, puedes insertar un pedido de prueba y sus líneas de detalle utilizando el cliente de MySQL. Asegúrate de que tu aplicación Spring Boot esté detenida antes de realizar estas operaciones manuales.

Conéctate a tu base de datos:

Bash

mysql -u root -p
# Ingresa tu contraseña
USE projectfinal_db;
Inserta un pedido principal:

SQL

INSERT INTO pedido (fecha, estado) VALUES (NOW(), 'PENDIENTE');
Anote el ID del pedido recién creado (puedes obtenerlo con SELECT LAST_INSERT_ID();). Para el siguiente ejemplo, asumiremos que el ID resultante es 2.

Inserta las líneas de detalle del pedido:
Usaremos los IDs de producto 15 (gfdfd), 16 (gfdfd) y 14 (dsd) de tu tabla producto para un pedido de ejemplo. Reemplaza 2 con el ID real de tu pedido si obtuviste uno diferente en el paso anterior.

SQL

-- Línea para el producto ID 15 ("gfdfd" de 455111.00 c/u)
INSERT INTO linea_pedido (pedido_id, producto_id, cantidad, precio_unitario)
VALUES (2, 15, 9, 455111.00);

-- Línea para el producto ID 16 ("gfdfd" de 455.00 c/u)
INSERT INTO linea_pedido (pedido_id, producto_id, cantidad, precio_unitario)
VALUES (2, 16, 9, 455.00);

-- Línea para el producto ID 14 ("dsd" de 14.00 c/u)
INSERT INTO linea_pedido (pedido_id, producto_id, cantidad, precio_unitario)
VALUES (2, 14, 5, 14.00);
Verifica los resultados en la base de datos:
Puedes comprobar que el pedido y sus líneas se han registrado correctamente con las siguientes consultas, que mostrarán datos similares a los que obtendrías en una operación real:

SQL

SELECT * FROM pedido;
Salida de ejemplo para pedido (tu ID de pedido podría ser diferente):

+----+-----------+----------------------------+
| id | estado    | fecha                      |
+----+-----------+----------------------------+
|  1 | PENDIENTE | 2025-07-23 14:15:57.000000 |
|  2 | PENDIENTE | 2025-07-23 14:17:57.000000 |
+----+-----------+----------------------------+
SQL

SELECT * FROM linea_pedido;
Salida de ejemplo para linea_pedido (con las líneas insertadas):

+----+----------+-----------+-------------+-----------------+
| id | cantidad | pedido_id | producto_id | precio_unitario |
+----+----------+-----------+-------------+-----------------+
|  3 |        9 |         2 |          15 |          455111 |
|  4 |        9 |         2 |          16 |             455 |
|  5 |        5 |         2 |          14 |              14 |
+----+----------+-----------+-------------+-----------------+
Esta verificación confirma que la estructura de la base de datos está configurada correctamente y lista para almacenar la información de pedidos y sus líneas.

🖥️ Interfaz de Usuario (Frontend)
Este proyecto incluye una interfaz de usuario básica desarrollada con HTML, CSS y JavaScript, que se sirve directamente desde el backend de Spring Boot.

Acceso a la Interfaz de Usuario:
Una vez que la aplicación Spring Boot esté en ejecución, puedes acceder al frontend abriendo tu navegador y navegando a:
http://localhost:8080/
Desde aquí, podrás interactuar con la aplicación, realizar pedidos y ver el historial.

Importante sobre la Interfaz de Usuario (CORS):
Si encuentras problemas al realizar operaciones (ej. "Realizar Pedido") desde el navegador debido a políticas de CORS (Cross-Origin Resource Sharing), verifica la configuración de seguridad en SecurityConfig.java. El frontend se sirve desde http://127.0.0.1:5500/ si lo ejecutas con Live Server, o desde http://localhost:8080/ si lo sirve Spring Boot directamente. La configuración de CORS en el backend (SecurityConfig.java) debe permitir explícitamente el origen de tu frontend (ej. http://127.0.0.1:5500 si usas Live Server para el desarrollo del frontend).

🔑 Seguridad
La API utiliza Spring Security y requerirá autenticación para acceder a la mayoría de los recursos (excepto quizás los endpoints de autenticación y Swagger UI, si así se configuran).

Endpoint de autenticación: Para iniciar sesión y obtener un token JWT, usa el endpoint POST /api/auth/login en Swagger UI o con tu cliente REST favorito.

Usuarios y roles: La configuración de usuarios y roles, así como las reglas de acceso, se definen en las clases de configuración de seguridad del proyecto (ej. SecurityConfig.java) y, potencialmente, en tu base de datos o en application.properties. Para desarrollo, se ha configurado un usuario en memoria (adminuser / 1234).

📂 Estructura del proyecto

src/main/java/com/miTrabajo/  # O 'com.martinps' si lo prefieres así
├── controller              -> Controladores REST (manejan las solicitudes HTTP)
├── service                 -> Lógica de negocio principal
├── repository              -> Acceso a base de datos (interfaces JPA)
├── model                   -> Modelos de datos (entidades JPA, enums)
├── dto                     -> Objetos de transferencia de datos (para entrada/salida de la API)
├── exception               -> Clases personalizadas para manejo de errores
├── config                  -> Configuraciones generales (Swagger, seguridad, MapStruct mappers)
└── security                -> Clases relacionadas con la seguridad (JWT, etc.)