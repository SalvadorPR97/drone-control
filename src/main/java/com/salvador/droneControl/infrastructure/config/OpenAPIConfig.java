package com.salvador.droneControl.infrastructure.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        info = @Info(
                title = "API de Drones",
                version = "1.0.0",
                description = "API para el vuelo  de drones en matrices",
                contact = @Contact(name = "Soporte", email = "spranchal@indra.es")
        ),
        servers = {
                @Server(url = "http://localhost:8080", description = "Servidor Local")
        }
)
@Configuration
public class OpenAPIConfig {
}
