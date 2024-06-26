package com.manuelr.banking.digital.onboarding.model.event

import com.manuelr.banking.digital.onboarding.model.dto.UserInformation
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class SendEmailEvent(

    @field:NotNull
    @field:NotBlank
    val flowId: String,

    @field:NotNull
    @field:NotBlank
    val userInformation: UserInformation,


    )
