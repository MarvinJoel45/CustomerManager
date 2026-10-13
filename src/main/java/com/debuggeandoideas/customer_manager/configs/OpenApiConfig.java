package com.debuggeandoideas.customer_manager.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    public OpenAPI provideOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title(API_TITLE)
                        .version(API_VERSION)
                        .description(API_DESCRIPTION)
                        .contact(new Contact()
                                .name(CONTACT_NAME)
                                .email(CONTACT_EMAIL))
                        .license(new License()
                                .name(LICENSE_NAME)
                                .url(LICENSE_URL)));
    }

    private static final String API_TITLE = "Customer Manager API";
    private static final String API_VERSION = "1.0.0";
    private static final String API_DESCRIPTION = "API for customer management";
    private static final String CONTACT_NAME = "Alejandro Calderon";
    private static final String CONTACT_EMAIL = "debuggeandoideas@gmail.com";
    private static final String LICENSE_NAME = "Apache 2.0";
    private static final String LICENSE_URL = "http://springdoc.org";
}
