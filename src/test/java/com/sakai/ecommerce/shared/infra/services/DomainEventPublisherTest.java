package com.sakai.ecommerce.shared.infra.services;

import com.sakai.ecommerce.shared.domain.core.AggregateRoot;
import com.sakai.ecommerce.shared.domain.core.DomainEvent;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationEventPublisher;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DomainEventPublisherTest {

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private DomainEventPublisher domainEventPublisher;

    @Test
    void shouldPublishEvents() {
        var aggregate = mock(AggregateRoot.class);
        var event = mock(DomainEvent.class);

        when(aggregate.getDomainEvents()).thenReturn(java.util.List.of(event));

        domainEventPublisher.publish(aggregate);

        verify(applicationEventPublisher).publishEvent(event);
        verify(aggregate).clearEvents();
    }

    @Test
    void shouldClearEventsAfterPublishing() {
        var aggregate = mock(AggregateRoot.class);

        when(aggregate.getDomainEvents()).thenReturn(java.util.List.of());

        domainEventPublisher.publish(aggregate);

        verify(aggregate).clearEvents();
    }
}
