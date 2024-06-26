package com.manuelr.banking.digital.onboarding.service.impl

import com.manuelr.banking.digital.onboarding.properties.KeycloakConfigProperties
import com.manuelr.banking.digital.onboarding.service.api.KeycloakService
import jakarta.ws.rs.core.Response
import org.keycloak.OAuth2Constants.*
import org.keycloak.admin.client.Keycloak
import org.keycloak.admin.client.resource.UsersResource
import org.keycloak.authorization.client.AuthzClient
import org.keycloak.authorization.client.Configuration
import org.keycloak.authorization.client.util.Http
import org.keycloak.protocol.oidc.client.authentication.ClientIdAndSecretCredentialsProvider
import org.keycloak.representations.AccessTokenResponse
import org.keycloak.representations.idm.UserRepresentation
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Primary
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

@Service
@Primary
class KeycloakServiceApiClientImpl(
    @Value("\${spring.security.oauth2.client.provider.keycloak.token-uri}") private val tokenUrl: String,
    private val keycloakProperties: KeycloakConfigProperties,
    private val keycloakConfiguration: Configuration,
    private val keycloakAdminClient: Keycloak,
    private val keycloakAuthClient: AuthzClient
) : KeycloakService {

    override fun obtainAccessToken(email: String, password: String): AccessTokenResponse {
        val user = this.findByEmail(email)
                ?: throw throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "User associated with email $email does not exist")
        return keycloakAuthClient.obtainAccessToken(user.username, password)
    }

    override fun obtainAccessToken(refreshToken: String): AccessTokenResponse {
        return getAccessTokenFromRefreshToken(tokenUrl, refreshToken)
    }

    override fun createUser(user: UserRepresentation) {
        val response = usersResource().create(user)
        if (response.statusInfo !== Response.Status.CREATED)
            throw ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error trying to create user.")
    }

    override fun existByEmail(email: String): Boolean {
        return usersResource().searchByEmail(email, true).isNotEmpty()
    }

    override fun existByUsername(username: String): Boolean {
        return usersResource().searchByUsername(username, true).isNotEmpty()
    }

    override fun findByUsername(username: String): UserRepresentation? {
        return findByFunction(username) { _, u -> usersResource().searchByUsername(u, true) }
    }

    private fun findByEmail(email: String): UserRepresentation? {
        return findByFunction(email) { _, e -> usersResource().searchByEmail(e, true) }
    }

    private fun <T> findByFunction(argument: T,
                                   findUserFunction: (UsersResource, T) -> MutableList<UserRepresentation>): UserRepresentation? {
        return findUserFunction(usersResource(), argument).getOrNull(0)
    }

    private fun usersResource(): UsersResource {
        val realmResource = keycloakAdminClient.realm(keycloakProperties.realm)
        val usersResource = realmResource.users()
        return usersResource
    }

    private fun getAccessTokenFromRefreshToken(url: String, refreshToken: String): AccessTokenResponse {
        return Http(keycloakConfiguration, ClientIdAndSecretCredentialsProvider())
                .post<AccessTokenResponse>(url)
                .authentication()
                .client()
                .form()
                .param(GRANT_TYPE, REFRESH_TOKEN)
                .param(REFRESH_TOKEN, refreshToken)
                .param(CLIENT_ID, keycloakProperties.clientId)
                .param(CLIENT_SECRET, keycloakProperties.clientSecret)
                .response()
                .json(AccessTokenResponse::class.java)
                .execute()
    }
}