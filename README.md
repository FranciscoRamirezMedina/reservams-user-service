\# ReservaMS - User Service



\## Descripcion



Este microservicio se encarga de administrar los perfiles de usuario del sistema ReservaMS.



El auth-service maneja las credenciales y este servicio guarda los datos personales del usuario.



\## Responsabilidades



\- Crear perfiles de usuario.

\- Listar usuarios.

\- Buscar usuarios por ID.

\- Buscar usuarios por authUserId.

\- Actualizar datos de perfil.

\- Eliminar perfiles.



\## Puerto



8082



\## Base de datos



reservams\_user\_db



\## Endpoints principales



\- GET /api/v1/users

\- GET /api/v1/users/{id}

\- GET /api/v1/users/auth/{authUserId}

\- POST /api/v1/users

\- PUT /api/v1/users/{id}

\- DELETE /api/v1/users/{id}



\## Ejecucion



1\. Crear la base de datos reservams\_user\_db.

2\. Ejecutar el script SQL ubicado en la carpeta database.

3\. Levantar Eureka Server.

4\. Ejecutar el user-service.

5\. Probar los endpoints desde Postman o desde el API Gateway.



