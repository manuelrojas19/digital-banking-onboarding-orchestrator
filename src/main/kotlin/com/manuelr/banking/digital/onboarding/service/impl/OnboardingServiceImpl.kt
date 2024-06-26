package com.manuelr.banking.digital.onboarding.service.impl

import com.manuelr.banking.digital.onboarding.model.event.SendEmailEvent
import com.manuelr.banking.digital.onboarding.model.event.UserCreatedEvent
import com.manuelr.banking.digital.onboarding.model.request.OnboardRequest
import com.manuelr.banking.digital.onboarding.producer.KafkaProducer
import com.manuelr.banking.digital.onboarding.service.api.AuthService
import com.manuelr.banking.digital.onboarding.service.api.OnboardingService
import com.manuelr.banking.digital.onboarding.util.USERS_PATH
import com.manuelr.banking.digital.onboarding.util.launchAsync
import com.manuelr.banking.digital.onboarding.util.toSendEmailEvent
import com.manuelr.banking.digital.onboarding.util.toUserCreatedEvent
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import java.net.URI

@Service
class OnboardingServiceImpl(
    private val authServiceImpl: AuthService,
    private val userCreatedEventProducer: KafkaProducer<UserCreatedEvent>,
    private val sendEmailEventProducer: KafkaProducer<SendEmailEvent>
) : OnboardingService {

    override fun onboardUser(onboardRequest: OnboardRequest): ResponseEntity<Void> {

        val createdUserKeycloakId =
            authServiceImpl.createUser(onboardRequest.userInformation, onboardRequest.credentials)

        launchAsync {
            userCreatedEventProducer.produce(
                toUserCreatedEvent(
                    createdUserKeycloakId,
                    onboardRequest.flowId,
                    onboardRequest.userInformation
                )
            )
        }

        launchAsync {
            sendEmailEventProducer.produce(
                toSendEmailEvent(
                    onboardRequest.flowId,
                    onboardRequest.userInformation
                )
            )
        }

        return ResponseEntity
            .created(URI.create(USERS_PATH.plus(createdUserKeycloakId)))
            .build()
    }
}