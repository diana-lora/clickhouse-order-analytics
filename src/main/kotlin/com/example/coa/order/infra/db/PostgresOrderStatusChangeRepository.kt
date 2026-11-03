package com.example.coa.order.infra.db

import com.example.coa.order.domain.OrderStatusChange
import com.example.coa.order.domain.OrderStatusChangeRepository
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface PostgresOrderStatusChangeRepository :
    CrudRepository<OrderStatusChange, UUID>,
    OrderStatusChangeRepository
