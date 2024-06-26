package com.manuelr.banking.digital.onboarding.producer

interface KafkaProducer<T> {
    suspend fun produce(message: T);
}