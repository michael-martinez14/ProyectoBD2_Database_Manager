#  Database Manager Tool (Java + MySQL & PostgreSQL)

Este proyecto es una **herramienta de administración de bases de datos** desarrollada en **Java**, diseñada para conectarse a **MySQL** y permitir al usuario explorar, administrar y sincronizar objetos de bases de datos.  

---

## Funcionalidades principales

### 🔑 Pantalla de inicio (Conexión)
En la pantalla inicial, el usuario debe ingresar los parámetros necesarios para conectarse a la base de datos:
- **Usuario (User)**
- **Contraseña (Password)**
- **Base de datos (Database)**
- **Puerto (Port)**
- **Host**

Además, la interfaz incluye:
- **Botón Guardar conexión** → Permite guardar una conexión válida en un archivo `conexiones.txt`.  
- **Botón Conectar** → Realiza la conexión si todos los campos son correctos.  
- **ComboBox de conexiones** → Lista las conexiones previamente guardadas.

---

### 🖥️ Pantalla principal
Una vez establecida la conexión, se accede a la pantalla principal, que ofrece las siguientes funciones:

- **Visualización de objetos soportados por MySQL**  
  El usuario puede explorar:
  - Tablas  
  - Vistas  
  - Índices  
  - Triggers  
  - Usuarios  
  - Funciones  

- **Gestión de objetos**  
  - Botón para **ver y generar el DDL** de un objeto seleccionado.  
  - Botón para **modificar** el SQL y aplicar cambios.  
  - Botones para **crear:**
    - **Nuevas tablas**, definiendo los campos requeridos.  
    - **Nuevas vistas**, a partir de una sentencia SQL proporcionada por el usuario.  

- **Previsualización SQL**  
  El SQL generado se muestra en un área de texto antes de ser ejecutado en la base de datos.

- **Visualización de Tablas y Vistas**  
  Se pueden consultar los datos de una tabla o vista previamente seleccionada.

---

### 📄 Generación y modificación de DDL
El sistema permite:
- Visualizar el DDL del objeto seleccionado.  
- Editar el SQL y aplicar modificaciones directamente.  

---

### ✍️ Ejecución de sentencias SQL
El usuario puede redactar sus propias sentencias SQL y ejecutarlas en el DBMS mediante el área de comandos.

---

### 🆕 Nuevas funcionalidades añadidas
- **Botón Diagrama Relacional** → Genera un diagrama visual de la base de datos, mostrando sus tablas, llaves primarias y llaves foráneas.  
- **Botón Sincronización a PostgreSQL** → Permite sincronizar tablas y vistas desde MySQL hacia PostgreSQL, facilitando la interoperabilidad entre ambos sistemas.  

---