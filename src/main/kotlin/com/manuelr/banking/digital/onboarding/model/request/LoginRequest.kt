package com.manuelr.banking.digital.onboarding.model.request

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class LoginRequest(
        @field:NotNull
        @field:NotBlank
        @field:Email
        val email: String,

        @field:NotNull
        @field:NotBlank
        val password: String
)
