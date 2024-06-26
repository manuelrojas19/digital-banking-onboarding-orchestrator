package com.manuelr.banking.digital.onboarding.config

import com.manuelr.banking.digital.onboarding.properties.KeycloakConfigProperties
import com.manuelr.banking.digital.onboarding.util.GRANT_TYPE
import com.manuelr.banking.digital.onboarding.util.PROVIDER
import com.manuelr.banking.digital.onboarding.util.SECRET
import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder
import org.keycloak.OAuth2Constants.CLIENT_CREDENTIALS
import org.keycloak.OAuth2Constants.PASSWORD
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.KeycloakBuilder
import org.keycloak.authorization.client.AuthzClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class KeycloakClientConfig(private val keycloakProperties: KeycloakConfigProperties) {

    @Bean
    fun keycloakAdminClient(configuration: org.keycloak.authorization.client.Configuration): Keycloak {
        return KeycloakBuilder.builder()
                .serverUrl(keycloakProperties.url)
                .realm(keycloakProperties.realm)
                .clientId(keycloakProperties.clientId)
                .clientSecret(keycloakProperties.clientSecret)
                .grantType(CLIENT_CREDENTIALS)
                .resteasyClient(ResteasyClientBuilder.newClient())
                .build();
    }

    @Bean
    fun keycloakAuthClient(): AuthzClient {
        return AuthzClient.create(authConfiguration())
    }

    @Bean
    fun authConfiguration(): org.keycloak.authorization.client.Configuration {
        return org.keycloak.authorization.client.Configuration(
                keycloakProperties.url,
                keycloakProperties.realm,
                keycloakProperties.clientId,
                credentials(),
                null
        )
    }

    private fun credentials(): MutableMap<String, Any?> {
        val clientCredentials: MutableMap<String, Any?> = HashMap()
        clientCredentials[SECRET] = keycloakProperties.clientSecret
        clientCredentials[GRANT_TYPE] = PASSWORD
        clientCredentials[PROVIDER] = SECRET
        return clientCredentials
    }


}