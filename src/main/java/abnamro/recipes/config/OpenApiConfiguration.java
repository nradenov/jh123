package abnamro.recipes.config;

import org.springdoc.core.GroupedOpenApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("api-docs")
public class OpenApiConfiguration {

    public static final String API_FIRST_PACKAGE = "com.mycompany.myapp.web.api";

    @Bean
    @ConditionalOnMissingBean(name = "apiFirstGroupedOpenAPI")
    public GroupedOpenApi apiFirstGroupedOpenAPI(
    ) {
         return GroupedOpenApi
            .builder()
            .group("openapi")
            .packagesToScan(API_FIRST_PACKAGE)
            .build();
    }
}
