package com.manuelr.banking.digital.onboarding.producer

import org.slf4j.LoggerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import java.util.*

abstract class AbstractKafkaProducer<T>(
        private val topic: String,
        private val kafkaTemplate: KafkaTemplate<String, T>,
) : KafkaProducer<T> {

    private val log = LoggerFactory.getLogger(this::class.java)

    override suspend fun produce(message: T) {
        log.info("Sending message $message to topic $topic")
        kafkaTemplate.send(topic, message).handleAsync(logCallback(message))
    }

    private fun logCallback(message: T): (t: SendResult<String, T>, u: Throwable) -> Unit = { _, e ->
        if (Objects.nonNull(e)) {
            log.error("Error trying to send message $message to topic $topic")
        } else {
            log.info("Message $message send successfully to topic $topic")
        }
    }
}