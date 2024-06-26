package com.manuelr.banking.digital.onboarding.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationProperties(prefix = "app.config.keycloak")
class KeycloakConfigProperties {
    lateinit var url: String
    lateinit var realm: String
    lateinit var clientId: String
    lateinit var clientSecret: String
}