# khipu-project
Demo de una integracion de la API de pagos de khipu en una pagina web (No local)

# Khipu API Integration - Java/Spring Boot

Java Version: 17

Spring Boot: 3.1

License DOU

Integración completa con la API de pagos de Khipu para aplicaciones Java/Spring Boot, desarrollada por Derly Ortiz Ubiera.

# Características Principales:

✅ Integración completa con Khipu API v3

✅ Configuración para entorno Sandbox y Producción

✅ Manejo de pagos y notificaciones webhook

✅ Sistema de reintentos automáticos

✅ Notificaciones por email

✅ Panel de administración básico

✅ Seguridad configurada

✅ Base de datos integrada (H2)

# Requisitos Técnicos
Java 17+

Maven 3.6+

Spring Boot 3.1+

Base de datos H2 (embebida por defecto)

# Configuración Inicial

1.Clonar el repositorio:

    git clone https://github.com/turepositorio/khipu-integration.git
    cd khipu-integration

2.Configurar las variables en application.yml:

    khipu:
      api-url: https://sandbox.khipu.com/api/3.0
      api-key: tu-api-key-de-sandbox
      receiver-id: tu-receiver-id
      return-url: http://tudominio.com/retorno
      cancel-url: http://tudominio.com/cancelado
      webhook-url: http://tudominio.com/api/khipu/webhook

    spring:
      datasource:
        url: jdbc:h2:mem:khipudb
        username: sa
        password:

3.Ejecutar la aplicación:      
              
    mvn spring-boot:run

# Endpoints Principales
API REST

    Método	     Endpoint     	                     Descripción
    POST	    /api/payments	                    Crear nuevo pago
    GET	    /api/payments/{id}/status	        Consultar estado de pago
    GET	    /api/payments/{id}/redirect	          Redirección a Khipu
    POST	    /api/khipu/webhook	             Webhook para notificaciones

Interfaz Web

    Página      	              Descripción
     /	                     Listado de pagos
     /create-payment	     Formulario de creación
     /payment/{id}	            Detalles y estado de pago

Estructura del Proyecto

      src/
      ├── main/
      │   ├── java/com/DerlyKhipu/Khipu/
      │   │   ├── config/        # Configuraciones
      │   │   ├── controller/    # Controladores
      │   │   ├── dto/           # Objetos de transferencia
      │   │   ├── entity/        # Entidades JPA
      │   │   ├── exception/     # Manejo de errores
      │   │   ├── repository/    # Repositorios
      │   │   ├── security/      # Config seguridad
      │   │   ├── service/       # Lógica de negocio
      │   │   └── KhipuApplication.java
      │   └── resources/
      │       ├── static/        # Assets estáticos
      │       ├── templates/     # Vistas Thymeleaf
      │       └── application.yml

# Ejemplo de Uso

## Crear pago mediante API


    @RestController
    @RequiredArgsConstructor
    public class MiController {
    
        private final KhipuService khipuService;
    
        @PostMapping("/crear-pago")
        public PaymentResponse crearPago(@RequestBody PaymentRequest request) {
            return khipuService.createPayment(request);
        }
    }

# Procesar notificaciones

## El sistema automáticamente:

1.Valida la firma del webhook

2.Actualiza el estado en base de datos

3.Envía notificaciones por email si corresponde

# Configuración Avanzada

## Base de Datos

Para usar MySQL/PostgreSQL en lugar de H2:

      spring:
        datasource:
          url: jdbc:mysql://localhost:3306/khipu
          username: usuario
          password: contraseña
          driver-class-name: com.mysql.cj.jdbc.Driver
     jpa:
        hibernate:
          ddl-auto: update

# Configuración SMTP

## Para habilitar notificaciones por email:

    spring:
      mail:
        host: smtp.tuservidor.com
        port: 587
        username: tu@email.com
        password: tupassword
        properties:
          mail:
            smtp:
              auth: true
              starttls.enable: true

# Contribuciones

Las contribuciones son bienvenidas:

1.Haz fork del proyecto

2.Crea tu branch (git checkout -b feature/nueva-funcionalidad)

3.Haz commit de tus cambios (git commit -am 'Agrega nueva funcionalidad')

4.Haz push al branch (git push origin feature/nueva-funcionalidad)

5.Abre un Pull Request

# Licencia

Este proyecto está bajo licencia DOU. 

# Contacto
Desarrollador: Derly Ortiz Ubiera

Email: ortizubieraderly@gmail.com

Documentación Khipu: https://khipu.com/page/api-3-0
