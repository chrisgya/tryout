package com.chrisgya.tryout.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@OpenAPIDefinition(
        info = @Info(
                title = "Demo Services",
                description = "Demonstrating I can code",
                contact = @Contact(
                        name = "Demo",
                        url = "https://www.demo.com",
                        email = "support@demo.com"
                ),
                license = @License(
                        name = "MIT Licence",
                        url = "http://www.apache.org/licenses/LICENSE-2.0")),
        servers = @Server(url = "http://localhost:8080/demo")
)
public class SwaggerConfig {
}
