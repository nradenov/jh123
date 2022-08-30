package abnamro.recipes.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Properties specific to Jh 123.
 * <p>
 * Properties are configured in the {@code application.yml} file.
 */
@ConfigurationProperties(prefix = "application", ignoreUnknownFields = false)
public class ApplicationProperties {}
