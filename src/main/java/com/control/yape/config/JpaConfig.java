package com.control.yape.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

/**
 * Configuración especial para JPA en Railway.
 * Las propiedades se cargan desde application-prod.properties
 */
@Configuration
@Profile("prod")
public class JpaConfig {
    // Las configuraciones se manejan vía application-prod.properties
}
