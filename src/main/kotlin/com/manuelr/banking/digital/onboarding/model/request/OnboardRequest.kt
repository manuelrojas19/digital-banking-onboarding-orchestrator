package com.manuelr.banking.digital.onboarding.model.request

import com.manuelr.banking.digital.onboarding.model.dto.Credentials
import com.manuelr.banking.digital.onboarding.model.dto.UserInformation
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class OnboardRequest(

        @field:NotNull
        @field:NotBlank
        val deviceId: String,

        // Global web
        @field:NotNull
        @field:NotBlank
        val origin: String,

        // UUID
        @field:NotNull
        @field:NotBlank
        val flowId: String,

        @field:NotNull
        @field:NotBlank
        val userInformation: UserInformation,

        @field:NotNull
        @field:NotBlank
        val credentials: Credentials
)
