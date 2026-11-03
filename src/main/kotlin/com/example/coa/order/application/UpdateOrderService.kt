package com.example.coa.order.application

import com.example.coa.commons.OrderStatusUpdatedEvent
import com.example.coa.commons.OrderTotalAmountRepairEvent
import com.example.coa.commons.nowInMillis
import com.example.coa.order.application.commands.UpdateOrderStatusCommand
import com.example.coa.order.application.commands.UpdateOrderTotalAmountCommand
import com.example.coa.order.domain.Order
import com.example.coa.order.domain.OrderRepository
import com.example.coa.order.domain.OrderStatusChange
import com.example.coa.order.domain.OrderStatusChangeRepository
import com.example.coa.order.domain.exceptions.OrderNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class UpdateOrderService(
    private val orderRepository: OrderRepository,
    private val orderStatusChangeRepository: OrderStatusChangeRepository,
    private val outboxEventService: OutboxEventService,
) {
    @Transactional
    fun execute(command: UpdateOrderStatusCommand): Order {
        val order = findOrder(command.orderId)

        order.status = command.newStatus
        val savedOrder = orderRepository.save(order)

        // Shared by the timeline and the event, so a history repair reproduces exactly this version
        val updatedAt = nowInMillis()
        orderStatusChangeRepository.save(
            OrderStatusChange(orderId = savedOrder.id, status = savedOrder.status, createdAt = updatedAt),
        )

        val updatedEvent =
            OrderStatusUpdatedEvent(
                orderId = savedOrder.id.toString(),
                customerId = savedOrder.customerId,
                totalAmount = savedOrder.totalAmount,
                newStatus = savedOrder.status,
                createdAt = savedOrder.createdAt,
                updatedAt = updatedAt,
            )
        outboxEventService.save(savedOrder, updatedEvent)

        return savedOrder
    }

    @Transactional
    fun execute(command: UpdateOrderTotalAmountCommand): Order {
        val order = findOrder(command.orderId)

        order.totalAmount = command.newTotalAmount
        val savedOrder = orderRepository.save(order)

        val updatedEvent =
            OrderTotalAmountRepairEvent(
                orderId = savedOrder.id.toString(),
                customerId = savedOrder.customerId,
                newTotalAmount = savedOrder.totalAmount,
                createdAt = savedOrder.createdAt,
            )
        outboxEventService.save(savedOrder, updatedEvent)

        return savedOrder
    }

    private fun findOrder(orderId: UUID): Order =
        orderRepository.findOrderById(orderId)
            ?: throw OrderNotFoundException("Order not found (ID=$orderId)")
}
