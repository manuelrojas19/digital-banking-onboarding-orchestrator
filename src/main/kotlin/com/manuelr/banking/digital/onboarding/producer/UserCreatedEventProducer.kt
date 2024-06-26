package com.manuelr.banking.digital.onboarding.producer

import com.manuelr.banking.digital.onboarding.model.event.UserCreatedEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class UserCreatedEventProducer(
        @Value("\${app.config.kafka.topics.user-created}") private val topicName: String,
        kafkaTemplate: KafkaTemplate<String, UserCreatedEvent>
) : AbstractKafkaProducer<UserCreatedEvent>(topicName, kafkaTemplate)

