package com.manuelr.banking.digital.onboarding.service.api

import com.manuelr.banking.digital.onboarding.model.request.OnboardRequest
import org.springframework.http.ResponseEntity

interface OnboardingService {
    fun onboardUser(onboardRequest: OnboardRequest): ResponseEntity<Void>
}