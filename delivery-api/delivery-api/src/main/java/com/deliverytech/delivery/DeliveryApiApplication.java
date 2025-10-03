package com.deliverytech.delivery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DeliveryApiApplication {

    public static void main(String[] args) {
        System.out.println("Iniciando Delivery Tech API...");
        System.out.println(" Desenvolvedor: Danylo Miranda");
        System.out.println("Versão: 1.0.0");
        System.out.println("Java: " + System.getProperty("java.version"));
        
        SpringApplication.run(DeliveryApiApplication.class, args);
        
        System.out.println("Aplicação iniciada com sucesso!");
        System.out.println("Swagger UI: http://localhost:8080/swagger-ui.html");
        System.out.println("H2 Console: http://localhost:8080/h2-console");
        System.out.println("Health Check: http://localhost:8080/health");
    }
}
