package com.manuelr.banking.digital.onboarding.service.api

import com.manuelr.banking.digital.onboarding.model.dto.Credentials
import com.manuelr.banking.digital.onboarding.model.dto.UserInformation
import com.manuelr.banking.digital.onboarding.model.request.LoginRefreshTokenRequest
import com.manuelr.banking.digital.onboarding.model.request.LoginRequest
import org.keycloak.representations.AccessTokenResponse
import org.springframework.http.ResponseEntity

interface AuthService {
    fun login(loginRequest: LoginRequest): ResponseEntity<AccessTokenResponse>
    fun refreshToken(loginRequest: LoginRefreshTokenRequest): ResponseEntity<AccessTokenResponse>
    fun createUser(userInformation: UserInformation, credentials: Credentials): String
}