package com.example.coa.order.application

import com.example.coa.order.domain.Order
import com.example.coa.order.domain.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class GetOrdersService(
    private val orderRepository: OrderRepository,
) {
    @Transactional(readOnly = true)
    fun execute(): List<Order> = orderRepository.findAll()
}
