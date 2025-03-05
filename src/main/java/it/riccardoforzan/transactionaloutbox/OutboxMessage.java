package it.riccardoforzan.transactionaloutbox;

import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document
record OutboxMessage(
        String id,
        String topic,
        String payload,
        Instant createdAt
) {
}
