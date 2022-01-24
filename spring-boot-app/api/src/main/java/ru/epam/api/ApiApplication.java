package ru.epam.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.reactive.ReactiveOAuth2ClientAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import ru.epam.api.config.data.R2DBCConfigurationProperties;
import ru.epam.auth.config.WebSecurityConfig;

@SpringBootApplication
@EnableConfigurationProperties(value = {R2DBCConfigurationProperties.class})
public class ApiApplication {

    private static Class<?>[] configClasses = {WebSecurityConfig.class, ApiApplication.class};

    public static void main(String[] args) {
        SpringApplication.run(configClasses, args);
    }

}
