package it.riccardoforzan.transactionaloutbox;

import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@Slf4j
public class OutboxPublisher {

    private final OutboxRepository outboxRepository;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OutboxPublisher(OutboxRepository outboxRepository, KafkaTemplate<String, String> kafkaTemplate) {
        this.outboxRepository = outboxRepository;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Scheduled(fixedDelayString = "${kafka.outbox-polling-ms}")
    public void processOutbox() {
        List<OutboxMessage> messages = outboxRepository.findAll();
        messages.forEach(this::processMessage);
    }

    private void processMessage(OutboxMessage message) {
        kafkaTemplate.send(message.topic(), message.id(), message.payload()).whenComplete((result, ex) -> {
            if (ex == null) {
                outboxRepository.delete(message);
            } else {
                log.error(ex.getMessage());
            }
        });
    }
}
