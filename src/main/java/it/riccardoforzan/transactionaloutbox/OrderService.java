package it.riccardoforzan.transactionaloutbox;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final String topic;
    private final ObjectMapper objectMapper;
    private final OutboxRepository outboxRepository;

    public OrderService(OrderRepository orderRepository, @Value("${kafka.outbox-topic}") String topic, ObjectMapper objectMapper, OutboxRepository outboxRepository) {
        this.orderRepository = orderRepository;
        this.topic = topic;
        this.objectMapper = objectMapper;
        this.outboxRepository = outboxRepository;
    }

    @Transactional
    public void createOrder(Order order) throws JsonProcessingException {
        orderRepository.save(order);
        OutboxMessage outboxMessage = new OutboxMessage(
                UUID.randomUUID().toString(),
                topic,
                objectMapper.writeValueAsString(order),
                Instant.now()
        );
        outboxRepository.save(outboxMessage);
    }

}
