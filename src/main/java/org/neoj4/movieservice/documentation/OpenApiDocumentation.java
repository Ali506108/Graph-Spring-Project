package org.neoj4.movieservice.documentation;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiDocumentation {

    @Bean
    public OpenAPI openApi() {
        return new OpenAPI().info(new Info()
                .title("Movie service for Graph Db")
                .description("that application for working with graphdb")
                .version("0.1")
                .contact(new Contact()
                        .name("Neo4j-application")
                        .url("https://github.com/Ali506108/Graph-Spring-Project")
                        .email("aliduisen77@gmail.com")
                ).license(new License()
                        .url("https://www.apache.org/licenses/LICENSE-2.0.html")
                        .name("Apache 2.0")
                )
        );
    }
}
