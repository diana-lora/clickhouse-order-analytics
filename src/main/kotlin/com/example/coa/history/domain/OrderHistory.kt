package com.example.coa.history.domain

import com.example.coa.commons.OrderStatus
import java.math.BigDecimal
import java.time.Instant
import java.util.UUID

data class OrderHistory(
    val orderId: UUID,
    val customerId: String,
    val totalAmount: BigDecimal,
    val orderStatus: OrderStatus,
    val createdAt: Instant,
    val updatedAt: Instant,
    val version: Long = 0L,
)
