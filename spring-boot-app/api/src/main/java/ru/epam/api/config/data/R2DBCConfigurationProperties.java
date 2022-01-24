package ru.epam.api.config.data;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@RequiredArgsConstructor
@ConfigurationProperties(prefix = "database")
public class R2DBCConfigurationProperties {

    private String host;
    private Integer port;
    private String db;
    private String username;
    private String password;
    private String driver;

}


