package com.example.coa.order.domain.outbox

interface OutboxRepository {
    fun save(event: OutboxEvent): OutboxEvent
}
