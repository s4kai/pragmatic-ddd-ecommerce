package com.sakai.ecommerce.shared.infra.services;

import com.sakai.ecommerce.shared.application.services.EventPublisher;
import com.sakai.ecommerce.shared.domain.core.AggregateRoot;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class DomainEventPublisher implements EventPublisher {
    private final ApplicationEventPublisher publisher;

    public void publish(AggregateRoot<?> aggregate) {
        aggregate.getDomainEvents()
                .forEach(publisher::publishEvent);

        aggregate.clearEvents();
    }
}