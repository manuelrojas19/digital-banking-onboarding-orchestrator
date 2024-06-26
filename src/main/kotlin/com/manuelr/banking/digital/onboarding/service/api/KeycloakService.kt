package com.manuelr.banking.digital.onboarding.service.api

import org.keycloak.representations.AccessTokenResponse
import org.keycloak.representations.idm.UserRepresentation

interface KeycloakService {
    fun obtainAccessToken(email: String, password: String): AccessTokenResponse
    fun obtainAccessToken(refreshToken: String): AccessTokenResponse
    fun createUser(user: UserRepresentation)
    fun existByEmail(email: String): Boolean
    fun existByUsername(username: String): Boolean
    fun findByUsername(username: String): UserRepresentation?
}