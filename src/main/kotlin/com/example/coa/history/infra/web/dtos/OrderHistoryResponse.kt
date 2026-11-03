package com.example.coa.history.infra.web.dtos

import com.example.coa.commons.OrderStatus
import com.example.coa.history.domain.OrderHistory
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class OrderHistoryResponse(
    val orderId: UUID,
    val customerId: String,
    val totalAmount: BigDecimal,
    val status: OrderStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
) {
    companion object {
        fun fromDomain(history: OrderHistory): OrderHistoryResponse =
            OrderHistoryResponse(
                orderId = history.orderId,
                customerId = history.customerId,
                totalAmount = history.totalAmount,
                status = history.orderStatus,
                createdAt = history.createdAt,
                updatedAt = history.updatedAt,
            )
    }
}
