package com.manuelr.banking.digital.onboarding.util

import com.manuelr.banking.digital.onboarding.model.dto.Credentials
import com.manuelr.banking.digital.onboarding.model.dto.UserInformation
import com.manuelr.banking.digital.onboarding.model.event.SendEmailEvent
import com.manuelr.banking.digital.onboarding.model.event.UserCreatedEvent
import org.keycloak.representations.idm.CredentialRepresentation
import org.keycloak.representations.idm.UserRepresentation

fun toUserRepresentation(
    userInformation: UserInformation,
    credential: CredentialRepresentation,
): UserRepresentation {
    val user = UserRepresentation()
    user.username = userInformation.email
    user.firstName = userInformation.name
    user.lastName = userInformation.lastName
    user.email = userInformation.email
    user.isEmailVerified = true
    user.isEnabled = true
    user.credentials = listOf(credential)
    return user
}

fun toSendEmailEvent(flowId: String, userInformation: UserInformation) =
    SendEmailEvent(
        flowId = flowId,
        userInformation = userInformation
    )

fun toCredentialRepresentation(credentials: Credentials): CredentialRepresentation {
    val credential = CredentialRepresentation()
    credential.type = CredentialRepresentation.PASSWORD
    credential.value = credentials.password
    credential.isTemporary = false
    return credential
}

fun toUserCreatedEvent(keycloakId: String, flowId: String, userInformation: UserInformation): UserCreatedEvent {
    return UserCreatedEvent(
        keycloakId = keycloakId,
        flowId = flowId,
        userInformation = userInformation
    )
}