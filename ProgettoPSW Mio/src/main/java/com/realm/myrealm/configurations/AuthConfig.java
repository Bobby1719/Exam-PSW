package com.realm.myrealm.configurations;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "auth-properties")
@EnableConfigurationProperties
public class AuthConfig {
    private String clientId;
    private String clientSecret;
    private String adminUsername;
    private String adminPassword;
    private String role;
}