package com.capgemini.hms.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(new Components()
                        .addSecuritySchemes(securitySchemeName,
                                new SecurityScheme()
                                        .name(securitySchemeName)
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("Capgemini HMS API (v1)")
                        .version("1.1")
                        .description("Professional Hospital Management System API - Modernized v1 Architecture. " +
                                     "Features include: Standardized ApiResponse wrapper, Patient Self-Service Portal (ROLE_PATIENT), " +
                                     "Scoped data access, Pageable support, and soft-delete preservation. " +
                                     "Use the 'Authorize' button with a JWT token obtained from the Auth Sign-in endpoint.")
                        .contact(new Contact()
                                .name("Capgemini HMS Team")
                                .email("support@capgemini-hms.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")));
    }
}
