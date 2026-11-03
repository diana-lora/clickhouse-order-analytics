package com.example.coa.order.application

import com.example.coa.commons.OrderCreatedEvent
import com.example.coa.order.application.commands.PlaceOrderCommand
import com.example.coa.order.domain.Order
import com.example.coa.order.domain.OrderRepository
import com.example.coa.order.domain.OrderStatusChange
import com.example.coa.order.domain.OrderStatusChangeRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class CreateOrderService(
    private val orderRepository: OrderRepository,
    private val orderStatusChangeRepository: OrderStatusChangeRepository,
    private val outboxEventService: OutboxEventService,
) {
    @Transactional
    fun execute(command: PlaceOrderCommand): Order {
        val order =
            Order(
                customerId = command.customerId,
                totalAmount = command.totalAmount,
            )
        val savedOrder = orderRepository.save(order)

        orderStatusChangeRepository.save(
            OrderStatusChange(orderId = savedOrder.id, status = savedOrder.status, createdAt = savedOrder.createdAt),
        )

        val orderCreatedEvent =
            OrderCreatedEvent(
                orderId = savedOrder.id.toString(),
                customerId = savedOrder.customerId,
                totalAmount = savedOrder.totalAmount,
                status = savedOrder.status,
                createdAt = savedOrder.createdAt,
                updatedAt = savedOrder.createdAt,
            )
        outboxEventService.save(savedOrder, orderCreatedEvent)

        return savedOrder
    }
}
