package az.texnoera.library_management_system.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;

import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

// Swaggerde test etmək üçün JWT-ye görə confiqurasiyadır.
@Configuration
@OpenAPIDefinition(info = @Info(title = "REST API", version = "1.0",
        description = "Hello World",
        contact = @Contact(name = "action")),
        servers = @Server(url = "/", description = "Default Server Url"),
        security = {@SecurityRequirement(name = "bearerToken")})
@SecuritySchemes({
        @SecurityScheme(name = "bearerToken", type = SecuritySchemeType.HTTP,
                scheme = "bearer", bearerFormat = "JWT")})
public class SwaggerConfig {
}