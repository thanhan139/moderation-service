package com.coreflows.moderation.api.security;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.ConstructorBinding;

@ConfigurationProperties(prefix = "app.security")
public record WebSecurityProperties(
        String jwkSetUri,
        String allowedOrigins) {

    @ConstructorBinding
    public WebSecurityProperties(String jwkSetUri, String allowedOrigins) {
        this.jwkSetUri = jwkSetUri != null ? jwkSetUri : "http://localhost:8081/.well-known/jwks.json";
        this.allowedOrigins = allowedOrigins != null ? allowedOrigins : "http://localhost:3000,http://localhost:5173";
    }
}