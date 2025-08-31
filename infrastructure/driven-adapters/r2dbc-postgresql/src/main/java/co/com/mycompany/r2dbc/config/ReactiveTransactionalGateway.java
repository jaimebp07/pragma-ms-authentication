package co.com.mycompany.r2dbc.config;

import org.springframework.stereotype.Component;
import org.springframework.transaction.reactive.TransactionalOperator;

import co.com.mycompany.model.gateways.TransactionalGateway;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class ReactiveTransactionalGateway implements TransactionalGateway {

    private final TransactionalOperator transactionalOperator;

    @Override
    public <T> Mono<T> execute(Mono<T> publisher) {
        return publisher.as(transactionalOperator::transactional);
    }
}