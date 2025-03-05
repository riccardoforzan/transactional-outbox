package it.riccardoforzan.transactionaloutbox;

public record Order(
        String id,
        String product
) {
}
