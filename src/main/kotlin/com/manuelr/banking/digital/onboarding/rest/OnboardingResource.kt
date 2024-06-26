package com.manuelr.banking.digital.onboarding.rest

import com.manuelr.banking.digital.onboarding.model.request.OnboardRequest
import com.manuelr.banking.digital.onboarding.service.api.OnboardingService
import com.manuelr.banking.digital.onboarding.util.AUTH_BASE_URI
import com.manuelr.banking.digital.onboarding.util.AUTH_SIGNUP_URI
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping(AUTH_BASE_URI)
class OnboardingResource(private val onboardingService: OnboardingService) {

    private val log = LoggerFactory.getLogger(this::class.java)

    @PostMapping(AUTH_SIGNUP_URI)
    fun createUser(@RequestBody request: OnboardRequest): ResponseEntity<Void> {
        log.info("Onboard endpoint request")
        return onboardingService.onboardUser(request)
    }
}