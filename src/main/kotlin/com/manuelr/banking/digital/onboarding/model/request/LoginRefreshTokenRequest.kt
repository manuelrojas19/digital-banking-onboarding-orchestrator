package com.manuelr.banking.digital.onboarding.model.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class LoginRefreshTokenRequest(
        @field:NotNull
        @field:NotBlank
        val refreshToken: String
)