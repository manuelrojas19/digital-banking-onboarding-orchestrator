package com.manuelr.banking.digital.onboarding.model.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class UserInformation(

        @field:NotNull
        @field:NotBlank
        val citizenId: String,

        @field:NotNull
        @field:NotBlank
        val taxId: String,

        @field:NotNull
        @field:NotBlank
        val name: String,

        @field:NotNull
        @field:NotBlank
        val lastName: String,

        @field:NotNull
        @field:NotBlank
        val birthday: LocalDate,

        @field:NotNull
        @field:NotBlank
        val email: String,

        @field:NotNull
        @field:NotBlank
        val genre: String,

        @field:NotNull
        @field:NotBlank
        val country: String,

        @field:NotNull
        @field:NotBlank
        val state: String,

        @field:NotNull
        @field:NotBlank
        val postCode: String,

        @field:NotNull
        @field:NotBlank
        val phoneNumber: String
)
