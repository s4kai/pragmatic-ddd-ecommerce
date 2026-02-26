package com.sakai.ecommerce.shared.application.services;

import com.sakai.ecommerce.shared.domain.core.AggregateRoot;

public interface EventPublisher{
    void publish(AggregateRoot<?> aggregate);
}
