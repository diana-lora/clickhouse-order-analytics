package com.example.coa.order.application

import com.example.coa.commons.OrderCreatedEvent
import com.example.coa.commons.OrderEvent
import com.example.coa.commons.OrderEventType.ORDER_CREATED
import com.example.coa.commons.OrderEventType.ORDER_REPAIR
import com.example.coa.commons.OrderEventType.ORDER_STATUS_UPDATED
import com.example.coa.commons.OrderEventType.ORDER_TOTAL_AMOUNT_REPAIR
import com.example.coa.commons.OrderRepairEvent
import com.example.coa.commons.OrderStatusUpdatedEvent
import com.example.coa.commons.OrderTotalAmountRepairEvent
import com.example.coa.config.KafkaConfig.Companion.ORDER_EVENTS_TOPIC
import com.example.coa.order.domain.Order
import com.example.coa.order.domain.outbox.OutboxEvent
import com.example.coa.order.domain.outbox.OutboxRepository
import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class OutboxEventService(
    private val outboxRepository: OutboxRepository,
    private val objectMapper: ObjectMapper,
) {
    // Mandatory: the event must be committed in the same transaction as the order change it describes
    @Transactional(propagation = Propagation.MANDATORY)
    fun save(
        order: Order,
        event: OrderEvent,
    ): OutboxEvent {
        val jsonPayload = objectMapper.writerFor(OrderEvent::class.java).writeValueAsString(event)

        val outboxEvent =
            OutboxEvent(
                aggregateType = "Order",
                aggregateId = order.id.toString(),
                aggregateKey = order.customerId,
                eventType = eventTypeOf(event),
                payload = jsonPayload,
                targetTopic = ORDER_EVENTS_TOPIC,
            )
        return outboxRepository.save(outboxEvent)
    }

    private fun eventTypeOf(event: OrderEvent): String =
        when (event) {
            is OrderCreatedEvent -> ORDER_CREATED
            is OrderStatusUpdatedEvent -> ORDER_STATUS_UPDATED
            is OrderTotalAmountRepairEvent -> ORDER_TOTAL_AMOUNT_REPAIR
            is OrderRepairEvent -> ORDER_REPAIR
        }
}
