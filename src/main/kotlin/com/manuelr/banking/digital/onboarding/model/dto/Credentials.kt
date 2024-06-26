package com.manuelr.banking.digital.onboarding.model.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class Credentials(

        @field:NotNull
        @field:NotBlank
        val username: String,

        @field:NotNull
        @field:NotBlank
        val password: String
)
