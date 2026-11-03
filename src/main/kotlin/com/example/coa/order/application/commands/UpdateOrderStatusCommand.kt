package com.example.coa.order.application.commands

import com.example.coa.commons.OrderStatus
import java.util.UUID

class UpdateOrderStatusCommand(
    val orderId: UUID,
    val newStatus: OrderStatus,
)
