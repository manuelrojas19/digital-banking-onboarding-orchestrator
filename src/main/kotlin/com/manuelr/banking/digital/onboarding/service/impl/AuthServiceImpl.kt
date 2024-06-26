package com.manuelr.banking.digital.onboarding.service.impl

import com.manuelr.banking.digital.onboarding.model.dto.Credentials
import com.manuelr.banking.digital.onboarding.model.dto.UserInformation
import com.manuelr.banking.digital.onboarding.model.request.LoginRefreshTokenRequest
import com.manuelr.banking.digital.onboarding.model.request.LoginRequest
import com.manuelr.banking.digital.onboarding.service.api.AuthService
import com.manuelr.banking.digital.onboarding.service.api.KeycloakService
import com.manuelr.banking.digital.onboarding.util.toCredentialRepresentation
import com.manuelr.banking.digital.onboarding.util.toUserRepresentation
import org.keycloak.representations.AccessTokenResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

/**
 * Service class for user-related operations.
 *
 * @property keycloakService The service for interacting with Keycloak.
 */
@Service
class AuthServiceImpl(
    private val keycloakService: KeycloakService
) : AuthService {

    private val log = LoggerFactory.getLogger(this::class.java)

    /**
     * Performs user login and returns the access token.
     *
     * @param loginRequest The login request containing user credentials.
     * @return ResponseEntity containing the access token if authentication is successful.
     * @throws ResponseStatusException if authentication fails.
     */
    override fun login(loginRequest: LoginRequest): ResponseEntity<AccessTokenResponse> {
        log.info("Trying to authenticate user with email: ${loginRequest.email}")
        try {
            return retrieveAccessToken(loginRequest.email, loginRequest.password)
        } catch (e: Exception) {
            log.error("Error during login: ${e.message}", e)
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error during login: ${e.message}", e)
        }
    }

    /**
     * Performs refresh token and returns the access token.
     *
     * @param loginRequest The login request containing the access token.
     * @return ResponseEntity containing the access token if authentication is successful.
     * @throws ResponseStatusException if authentication fails.
     */
    override fun refreshToken(loginRequest: LoginRefreshTokenRequest): ResponseEntity<AccessTokenResponse> {
        log.info("Trying to refresh access token")
        try {
            return retrieveAccessToken(loginRequest.refreshToken)
        } catch (e: Exception) {
            log.error("Error during token refresh: ${e.message}", e)
            throw ResponseStatusException(HttpStatus.UNAUTHORIZED, "Error during token refresh: ${e.message}", e)
        }
    }

    /**
     * Creates a new user.
     *
     * @param userInformation The request containing user details.
     * @return ResponseEntity containing the details of the created user if successful.
     * @throws ResponseStatusException if user creation fails.
     */
    override fun createUser(userInformation: UserInformation, credentials: Credentials): String {

        val username = credentials.username

        log.info("Trying to onboard user with email $username on keycloak")

        validateUserExists(username, username)

        val credential = toCredentialRepresentation(credentials)
        val user = toUserRepresentation(userInformation, credential)

        try {
            log.info("Trying to onboard user with email $username on keycloak")
            keycloakService.createUser(user)
            log.info("User with email $username was created successfully ")
        } catch (e: Exception) {
            log.error("Error trying to creating user", e)
        }

        val createdUser = keycloakService.findByUsername(username)
            ?: throw ResponseStatusException(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unable to retrieve the created user with email $username"
            )

        log.info("User with email $user was successfully created")

        return createdUser.id
    }

    private fun retrieveAccessToken(email: String, password: String): ResponseEntity<AccessTokenResponse> {
        log.info("Trying to retrieve access token for user with email: $email")
        val accessToken = keycloakService.obtainAccessToken(email, password)
        log.info("Access token from credentials retrieved successfully")
        return ResponseEntity.ok(accessToken)
    }

    private fun retrieveAccessToken(refreshToken: String): ResponseEntity<AccessTokenResponse> {
        log.info("Trying to retrieve access token using refresh token")
        val accessToken = keycloakService.obtainAccessToken(refreshToken)
        log.info("Access token from refreshToken retrieved successfully")
        return ResponseEntity.ok(accessToken)
    }

    private fun validateUserExists(email: String, username: String) {
        if (keycloakService.existByEmail(email))
            throw ResponseStatusException(HttpStatus.CONFLICT, "User with email $email already exists")
        if (keycloakService.existByUsername(username))
            throw ResponseStatusException(HttpStatus.CONFLICT, "User with username $username already exists")
    }
}