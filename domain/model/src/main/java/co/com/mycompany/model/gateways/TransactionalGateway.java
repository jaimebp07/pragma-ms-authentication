package co.com.mycompany.model.gateways;

import reactor.core.publisher.Mono;

public interface TransactionalGateway {
    <T> Mono<T> execute(Mono<T> publisher);
}
