package com.alibou.security.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class OpenApiConfig {
    @Value("${bezkoder.openapi.dev-url}")
    private String devUrl;

    @Value("${bezkoder.openapi.prod-url}")
    private String prodUrl;

    @Bean
    OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("contact@pasanabeysekara.com");
        contact.setName("Pasan Abeysekara");
        contact.setUrl("https://www.pasanabeysekara.com");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Demo Service API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage demo.").termsOfService("https://www.pasanabeysekara.com")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }

    @Bean
    GroupedOpenApi authApis() { // group all APIs with `auth` in the path
        return GroupedOpenApi.builder().group("auth").pathsToMatch("/api/v1/auth/**").build();
    }

    @Bean
    GroupedOpenApi bookApis() { // group all APIs with `auth` in the path
        return GroupedOpenApi.builder().group("books").pathsToMatch("/api/v1/books/**").build();
    }

    @Bean
    GroupedOpenApi adminApis() { // group all APIs with `admin` in the path
        return GroupedOpenApi.builder().group("admin").pathsToMatch("/api/v1/admin**", "/api/v1/management", "/api/v1/demo-controller").build();
    }
}
