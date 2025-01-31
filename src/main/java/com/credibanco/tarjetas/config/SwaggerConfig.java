package com.credibanco.tarjetas.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "API de Tarjetas Bank Inc",
                version = "1.0",
                description = "Documentación de la API para la gestión de Tarjetas de Crédito o Débito"
        )
)
public class SwaggerConfig {
}
