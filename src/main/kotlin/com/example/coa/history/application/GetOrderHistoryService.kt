package com.example.coa.history.application

import com.example.coa.history.application.queries.GetOrderHistoryQuery
import com.example.coa.history.domain.OrderHistory
import com.example.coa.history.domain.OrderHistoryRepository
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.stereotype.Service

@Service
class GetOrderHistoryService(
    @Qualifier("clickhouseOrderHistoryRepository") private val orderHistoryRepository: OrderHistoryRepository,
) {
    fun execute(query: GetOrderHistoryQuery): List<OrderHistory> = orderHistoryRepository.findBy(query)
}
