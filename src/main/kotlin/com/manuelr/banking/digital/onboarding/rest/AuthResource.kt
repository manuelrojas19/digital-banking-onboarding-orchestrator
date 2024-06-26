package com.manuelr.banking.digital.onboarding.rest

import com.manuelr.banking.digital.onboarding.model.request.LoginRefreshTokenRequest
import com.manuelr.banking.digital.onboarding.model.request.LoginRequest
import com.manuelr.banking.digital.onboarding.service.api.AuthService
import com.manuelr.banking.digital.onboarding.util.AUTH_BASE_URI
import com.manuelr.banking.digital.onboarding.util.AUTH_LOGIN_URI
import com.manuelr.banking.digital.onboarding.util.AUTH_REFRESH_TOKEN_URI
import org.keycloak.representations.AccessTokenResponse
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(AUTH_BASE_URI)
class AuthResource(private val authService: AuthService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @PostMapping(AUTH_LOGIN_URI)
    fun login(@RequestBody request: LoginRequest): ResponseEntity<AccessTokenResponse> {
        log.info("Login endpoint request")
        return authService.login(request)
    }

    @PostMapping(AUTH_REFRESH_TOKEN_URI)
    fun refreshToken(@RequestBody request: LoginRefreshTokenRequest): ResponseEntity<AccessTokenResponse> {
        log.info("Refresh token endpoint request")
        return authService.refreshToken(request)
    }
}