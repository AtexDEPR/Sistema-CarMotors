# CarMotorsProject - Automotive Workshop Management System

**English** | [Español](#español)

## English

### Overview

The CarMotorsProject is a comprehensive software solution designed to streamline the daily operations of an automotive workshop, specifically tailored for "CarMotors Workshop." The system aims to enhance efficiency in inventory management, maintenance processes, customer service, invoicing, and supplier relationships. Built using Java with a Maven-based MVC architecture, the project is organized into modular components, ensuring scalability and maintainability.

### Objectives

The primary goal is to provide an integrated platform that:

- Manages spare parts inventory with real-time tracking and reordering.
- Optimizes maintenance workflows from vehicle reception to delivery.
- Enhances customer engagement through service history and loyalty programs.
- Generates electronic invoices compliant with Colombia's DIAN regulations.
- Facilitates supplier management and performance evaluation.
- Supports special activities like maintenance campaigns and technical inspections.

### Modules and Functionalities

#### 1. Spare Parts Inventory Management

**1.1. Spare Parts Registration**

- Record spare parts with details:
  - Name and type (Mechanical, Electrical, Body, Consumable).
  - Compatible vehicle brand and model.
  - Associated supplier (name, contact, supply frequency).
  - Stock quantity, minimum stock alerts, entry date, and estimated lifespan.
  - Status (Available, Reserved, Out of Service).

**1.2. Lot Management and Traceability**

- Unique lot identification linked to entry date and supplier.
- Track spare part usage per job for full traceability.
- Automated alerts for nearing expiration or stock depletion.

**1.3. Replenishment Control**

- Generate purchase orders automatically based on minimum stock levels.
- Track pending supplier orders.

**1.4. Inventory Reports**

- Detailed lists by type, brand, or status.
- Consumption analysis over specific periods.
- Alerts for parts exceeding their lifespan.

#### 2. Maintenance Services Management

**2.1. Service Registration**

- Log services offered:
  - Type (Preventive or Corrective).
  - Vehicle details (brand, model, license plate, type).
  - Job details (description, parts used, estimated time).
  - Labor costs (by job type or hourly rate).
  - Service status (Pending, In Progress, Completed, Delivered).

**2.2. Workflow Management**

- **Vehicle Reception**: Register customer and vehicle data, perform initial inspection.
- **Maintenance Execution**: Assign tasks to specialized technicians, track progress in real-time.
- **Vehicle Delivery**: Verify completed work, generate signed delivery order.

**2.3. Maintenance Reports**

- Most requested services by vehicle type.
- Technician productivity (jobs completed, time spent).
- Detailed service history per customer or vehicle.

#### 3. Customer Management

**3.1. Customer Registration**

- Store customer data (name, ID, phone, email).
- Maintain service history (vehicles serviced, jobs performed, invoices).
- Send automated reminders for preventive maintenance or service renewals.

**3.2. Loyalty and Promotions**

- Track discounts based on service volume.
- Implement rewards program for frequent customers.

#### 4. Electronic Invoicing

- Generate visual invoices (PDF/PNG) compliant with DIAN Resolution 042/2020 (Colombia).
- **Invoice Elements**:
  - Workshop details (name, NIT, address, contact).
  - Customer details (name, ID, address).
  - Service details (description, parts used, labor costs, subtotals, total).
  - Invoice number, issue date, CUFE, QR code, and digital signature.
- **Features**:
  - Auto-populate data from inventory and maintenance modules.
  - Calculate taxes per Colombian regulations.
  - Downloadable or emailed invoices.

#### 5. Supplier Management

**5.1. Supplier Registration**

- Store supplier details (name, NIT, contact, visit frequency).
- Log supplied products (part types, quantities, dates).

**5.2. Supplier Evaluation**

- Rate suppliers based on punctuality, product quality, and costs.
- Generate performance reports for negotiation optimization.

#### 6. Special Activities Management

**6.1. Preventive Maintenance Campaigns**

- Register seasonal promotions (e.g., oil change discounts).
- Auto-schedule appointments for frequent customers.
- Evaluate campaign success via reports.

**6.2. Specialized Technical Inspections**

- Log inspections (e.g., pre-technical-mechanical review).
- Record results (approved, repairs needed, rejected).
- Schedule future inspections automatically.

### System Architecture

- **Technology Stack**: Java, Maven, MySQL, Swing (UI).
- **Architecture**: MVC (Model-View-Controller) with modular packages per component (e.g., `parts`, `services`, `customers`).
- **UI Design**: User-friendly interface with menus for:
  - Inventory Management
  - Maintenance and Repairs
  - Customers and Invoicing
  - Suppliers and Purchases
  - Reports and Statistics
- **Mobile Optimization**: Responsive design for mobile access by technicians and administrators.

### Project Structure

```
CarMotorsProject/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com.carmotorsproject/
│   │   │   │   ├── config/            # Database and app configurations
│   │   │   │   ├── parts/             # Spare parts inventory module
│   │   │   │   ├── services/          # Maintenance services module
│   │   │   │   ├── customers/         # Customer management module
│   │   │   │   ├── invoices/          # Electronic invoicing module
│   │   │   │   ├── campaigns/         # Special activities module
│   │   │   │   ├── ui/                # UI components and themes
│   │   │   │   └── main/              # Application entry point
│   │   ├── resources/
│   │   │   ├── icons/                 # UI icons
│   │   │   └── sql/                   # Database scripts
├── pom.xml                            # Maven configuration
├── README.md                          # Project documentation
├── docs/
│   ├── database_diagram.er            # ER diagram
│   ├── class_diagram.uml              # Class diagram
│   ├── table_component_mapping.md     # Table-to-component mapping
│   └── class_component_mapping.md     # Class-to-component mapping
```

### Deliverables

1. **Source Code**: Hosted on GitHub with commits in English, following MVC architecture.
2. **Database Diagram**: Complete ER diagram for all tables (in `docs/database_diagram.er`).
3. **Class Diagram**: Comprehensive UML diagram for all classes (in `docs/class_diagram.uml`).
4. **SQL Scripts**: MySQL scripts to create and populate the database (in `src/main/resources/sql/`).
5. **Table-to-Component Mapping**: List of database tables used by each component (in `docs/table_component_mapping.md`).
6. **Class-to-Component Mapping**: List of classes used by each component (in `docs/class_component_mapping.md`).

### Setup Instructions

1. **Prerequisites**:

   - Java 17 or higher
   - MySQL 8.0 or higher
   - Maven 3.8 or higher

2. **Clone Repository**:

   ```bash
   git clone https://github.com/username/CarMotorsProject.git
   ```

3. **Database Setup**:

   - Create a MySQL database:

     ```sql
     CREATE DATABASE carmotors_db;
     ```

   - Run SQL scripts:

     ```bash
     mysql -u <username> -p carmotors_db < src/main/resources/sql/schema.sql
     mysql -u <username> -p carmotors_db < src/main/resources/sql/data.sql
     ```

4. **Build Project**:

   ```bash
   cd CarMotorsProject
   mvn clean install
   ```

5. **Run Application**:

   ```bash
   java -jar target/CarMotorsProject-1.0-SNAPSHOT.jar
   ```

6. **Access UI**: Navigate through the Swing-based interface to manage workshop operations.

### Contributing

- Follow the GitHub Flow model for contributions.
- Write clear commit messages in English (e.g., `Add supplier evaluation feature to SupplierDAO`).
- Ensure code adheres to MVC structure and includes unit tests.

### License

This project is licensed under the MIT License. See `LICENSE` for details.

---

## Español

### Resumen

El CarMotorsProject es una solución integral de software diseñada para optimizar las operaciones diarias del taller automotriz "CarMotors." El sistema busca mejorar la eficiencia en la gestión de inventarios, procesos de mantenimiento, atención al cliente, facturación y relaciones con proveedores. Desarrollado en Java con arquitectura MVC basada en Maven, el proyecto está organizado en componentes modulares, garantizando escalabilidad y mantenibilidad.

### Objetivos

El objetivo principal es proporcionar una plataforma integrada que:

- Gestione el inventario de repuestos con seguimiento en tiempo real y reabastecimiento.
- Optimice los flujos de trabajo de mantenimiento desde la recepción hasta la entrega del vehículo.
- Mejore la interacción con clientes mediante historiales de servicio y programas de fidelización.
- Genere facturas electrónicas cumpliendo con la normativa de la DIAN en Colombia.
- Facilite la gestión y evaluación de proveedores.
- Soporte actividades especiales como campañas de mantenimiento e inspecciones técnicas.

### Módulos y Funcionalidades

#### 1. Gestión de Inventarios de Repuestos

**1.1. Registro de Repuestos**

- Registrar repuestos con detalles:
  - Nombre y tipo (Mecánico, Eléctrico, Carrocería, Consumible).
  - Marca y modelo de vehículo compatible.
  - Proveedor asociado (nombre, contacto, frecuencia de suministro).
  - Cantidad en stock, alertas de stock mínimo, fecha de ingreso y vida útil.
  - Estado (Disponible, Reservado, Fuera de Servicio).

**1.2. Gestión de Lotes y Trazabilidad**

- Identificación única de lotes vinculada a fecha de ingreso y proveedor.
- Seguimiento del uso de repuestos por trabajo para trazabilidad.
- Alertas automáticas para productos cercanos a caducidad o agotamiento.

**1.3. Control de Reabastecimiento**

- Generar órdenes de compra automáticas según niveles mínimos de stock.
- Seguimiento de pedidos pendientes con proveedores.

**1.4. Reportes de Inventario**

- Listados detallados por tipo, marca o estado.
- Análisis de consumo por períodos.
- Alertas para repuestos que exceden su vida útil.

#### 2. Gestión de Servicios de Mantenimiento

**2.1. Registro de Servicios**

- Registrar servicios ofrecidos:
  - Tipo (Preventivo o Correctivo).
  - Datos del vehículo (marca, modelo, placa, tipo).
  - Detalles del trabajo (descripción, repuestos usados, tiempo estimado).
  - Costos de mano de obra (por tipo de trabajo o tarifa horaria).
  - Estado del servicio (Pendiente, En Proceso, Completado, Entregado).

**2.2. Flujos de Trabajo**

- **Recepción del Vehículo**: Registrar cliente y vehículo, realizar inspección inicial.
- **Ejecución del Mantenimiento**: Asignar tareas a técnicos especializados, seguimiento en tiempo real.
- **Entrega del Vehículo**: Verificar trabajo completado, generar orden de entrega firmada.

**2.3. Reportes de Mantenimiento**

- Servicios más solicitados por tipo de vehículo.
- Productividad de técnicos (trabajos completados, tiempo empleado).
- Historial detallado de servicios por cliente o vehículo.

#### 3. Gestión de Clientes

**3.1. Registro de Clientes**

- Almacenar datos del cliente (nombre, identificación, teléfono, correo).
- Mantener historial de servicios (vehículos atendidos, trabajos realizados, facturas).
- Enviar recordatorios automáticos para mantenimientos preventivos o renovaciones.

**3.2. Fidelización y Promociones**

- Registrar descuentos por volumen de servicios.
- Implementar programa de recompensas para clientes frecuentes.

#### 4. Facturación Electrónica

- Generar facturas visuales (PDF/PNG) cumpliendo con la Resolución 042/2020 de la DIAN.
- **Elementos de la Factura**:
  - Datos del taller (razón social, NIT, dirección, contacto).
  - Datos del cliente (nombre, documento, dirección).
  - Detalles del servicio (descripción, repuestos usados, costos de mano de obra, subtotales, total).
  - Número de factura, fecha de emisión, CUFE, código QR y firma digital.
- **Funcionalidades**:
  - Autocompletar datos desde módulos de inventario y mantenimiento.
  - Calcular impuestos según normativa colombiana.
  - Facturas descargables o enviadas por correo electrónico.

#### 5. Gestión de Proveedores

**5.1. Registro de Proveedores**

- Almacenar datos completos (nombre, NIT, contacto, frecuencia de visitas).
- Registrar productos suministrados (tipos de repuestos, cantidades, fechas).

**5.2. Evaluación de Proveedores**

- Calificar proveedores según puntualidad, calidad y costos.
- Generar reportes de desempeño para optimizar negociaciones.

#### 6. Gestión de Actividades Especiales

**6.1. Campañas de Mantenimiento Preventivo**

- Registrar promociones estacionales (ej., descuentos por cambio de aceite).
- Programar citas automáticas para clientes frecuentes.
- Evaluar el éxito de campañas mediante reportes.

**6.2. Inspecciones Técnicas Especializadas**

- Registrar inspecciones específicas (ej., pre-revisión técnico-mecánica).
- Registrar resultados (aprobado, reparaciones necesarias, rechazado).
- Programar revisiones futuras automáticamente.

### Arquitectura del Sistema

- **Tecnologías**: Java, Maven, MySQL, Swing (interfaz gráfica).
- **Arquitectura**: MVC (Modelo-Vista-Controlador) con paquetes modulares por componente (ej., `parts`, `services`, `customers`).
- **Diseño de Interfaz**: Interfaz amigable con menús para:
  - Gestión de Inventarios
  - Mantenimiento y Reparaciones
  - Clientes y Facturación
  - Proveedores y Compras
  - Reportes y Estadísticas
- **Optimización Móvil**: Diseño responsivo para acceso desde dispositivos móviles.

### Estructura del Proyecto

```
CarMotorsProject/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   ├── com.carmotorsproject/
│   │   │   │   ├── config/            # Configuraciones de base de datos y aplicación
│   │   │   │   ├── parts/             # Módulo de inventario de repuestos
│   │   │   │   ├── services/          # Módulo de servicios de mantenimiento
│   │   │   │   ├── customers/         # Módulo de gestión de clientes
│   │   │   │   ├── invoices/          # Módulo de facturación electrónica
│   │   │   │   ├── campaigns/         # Módulo de actividades especiales
│   │   │   │   ├── ui/                # Componentes de interfaz gráfica
│   │   │   │   └── main/              # Punto de entrada de la aplicación
│   │   ├── resources/
│   │   │   ├── icons/                 # Iconos de la interfaz
│   │   │   └── sql/                   # Scripts de base de datos
├── pom.xml                            # Configuración de Maven
├── README.md                          # Documentación del proyecto
├── docs/
│   ├── database_diagram.er            # Diagrama ER
│   ├── class_diagram.uml              # Diagrama de clases
│   ├── table_component_mapping.md     # Mapeo de tablas a componentes
│   └── class_component_mapping.md     # Mapeo de clases a componentes
```

### Entregables

1. **Código Fuente**: Hospedado en GitHub con commits en inglés, siguiendo arquitectura MVC.
2. **Diagrama de Base de Datos**: Diagrama ER completo (en `docs/database_diagram.er`).
3. **Diagrama de Clases**: Diagrama UML completo (en `docs/class_diagram.uml`).
4. **Scripts SQL**: Scripts MySQL para crear y poblar la base de datos (en `src/main/resources/sql/`).
5. **Mapeo de Tablas por Componente**: Lista de tablas usadas por cada componente (en `docs/table_component_mapping.md`).
6. **Mapeo de Clases por Componente**: Lista de clases usadas por cada componente (en `docs/class_component_mapping.md`).

### Instrucciones de Configuración

1. **Prerrequisitos**:

   - Java 17 o superior
   - MySQL 8.0 o superior
   - Maven 3.8 o superior

2. **Clonar Repositorio**:

   ```bash
   git clone https://github.com/username/CarMotorsProject.git
   ```

3. **Configuración de Base de Datos**:

   - Crear base de datos MySQL:

     ```sql
     CREATE DATABASE carmotors_db;
     ```

   - Ejecutar scripts SQL:

     ```bash
     mysql -u <username> -p carmotors_db < src/main/resources/sql/schema.sql
     mysql -u <username> -p carmotors_db < src/main/resources/sql/data.sql
     ```

4. **Compilar Proyecto**:

   ```bash
   cd CarMotorsProject
   mvn clean install
   ```

5. **Ejecutar Aplicación**:

   ```bash
   java -jar target/CarMotorsProject-1.0-SNAPSHOT.jar
   ```

6. **Acceder a la Interfaz**: Navegar por la interfaz basada en Swing para gestionar las operaciones del taller.

### Contribuciones

- Seguir el modelo GitHub Flow para contribuciones.
- Escribir mensajes de commit claros en inglés (ej., `Add supplier evaluation feature to SupplierDAO`).
- Asegurar que el código siga la estructura MVC e incluya pruebas unitarias.

### Licencia

Este proyecto está licenciado bajo la Licencia MIT. Ver `LICENSE` para más detalles.
