package com.sakai.ecommerce.shared.infra;

import com.sakai.ecommerce.shared.core.AggregateRoot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DomainEventPublisher {
    private final ApplicationEventPublisher publisher;

    public void publish(AggregateRoot<?> aggregate) {
        aggregate.getDomainEvents()
                .forEach(publisher::publishEvent);

        aggregate.clearEvents();
    }
}