package it.riccardoforzan.transactionaloutbox;

import com.mongodb.client.model.changestream.ChangeStreamDocument;
import org.bson.Document;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.ChangeStreamOptions;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.messaging.ChangeStreamRequest;
import org.springframework.data.mongodb.core.messaging.DefaultMessageListenerContainer;
import org.springframework.data.mongodb.core.messaging.MessageListener;
import org.springframework.data.mongodb.core.messaging.MessageListenerContainer;

@Configuration(proxyBeanMethods = false)
public class MongoChangeStreamListener {

    @Bean
    public MessageListenerContainer messageListenerContainer(MongoTemplate mongoTemplate) {
        MessageListenerContainer container = new DefaultMessageListenerContainer(mongoTemplate);

        MessageListener<ChangeStreamDocument<Document>, Document> listener = event -> {
            Document message = event.getBody();
            System.out.println("Received message: " + message);
        };

        ChangeStreamRequest.ChangeStreamRequestOptions options = new ChangeStreamRequest.ChangeStreamRequestOptions(mongoTemplate.getDb().getName(), OutboxMessage.class.getName(), ChangeStreamOptions.empty());

        container.register(new ChangeStreamRequest<>(listener, options), Document.class);
        container.start();

        return container;
    }
}
