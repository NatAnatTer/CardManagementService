package ru.project.CardManagementService.swagger;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;

@OpenAPIDefinition(
        info = @Info(
                title = "CardManagementService",
                description = "API системы управления картами", version = "1.0.0",
                contact = @Contact(
                        name = "Natalia",
                        email = "natinho@yandex.ru"
                )
        )
)
public class OpenApiConfig {
}
