package com.manuelr.banking.digital.onboarding.producer

import com.manuelr.banking.digital.onboarding.model.event.SendEmailEvent
import org.springframework.beans.factory.annotation.Value
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
class SendEmailEventProducer(
        @Value("\${app.config.kafka.topics.send-email}") private val topicName: String,
        kafkaTemplate: KafkaTemplate<String, SendEmailEvent>
) : AbstractKafkaProducer<SendEmailEvent>(topicName, kafkaTemplate)