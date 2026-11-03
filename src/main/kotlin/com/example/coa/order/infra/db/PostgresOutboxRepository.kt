package com.example.coa.order.infra.db

import com.example.coa.order.domain.outbox.OutboxEvent
import com.example.coa.order.domain.outbox.OutboxRepository
import org.springframework.data.repository.CrudRepository
import java.util.UUID

interface PostgresOutboxRepository :
    CrudRepository<OutboxEvent, UUID>,
    OutboxRepository
