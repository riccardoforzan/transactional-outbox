package it.riccardoforzan.transactionaloutbox;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface OutboxRepository extends MongoRepository<OutboxMessage, String> {
}
