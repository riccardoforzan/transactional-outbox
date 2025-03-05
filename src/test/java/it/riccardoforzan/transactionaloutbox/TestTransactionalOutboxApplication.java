package it.riccardoforzan.transactionaloutbox;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.testcontainers.context.ImportTestcontainers;

@ImportTestcontainers(TestcontainersConfiguration.class)
public class TestTransactionalOutboxApplication {

    public static void main(String[] args) {
        SpringApplication.from(TransactionalOutboxApplication::main).with(TestcontainersConfiguration.class).run(args);
    }

}
