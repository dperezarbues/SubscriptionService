package com.dpa.subscription.service;

import com.dpa.subscription.domain.Subscription;

import java.util.Optional;

public interface SubscriptionService {

    Optional<Subscription> createSubscription(Subscription subscription);

    Optional<Subscription> getSubscription(Long subscriptionId);

    Iterable<Subscription> getSubscription();

    Optional<Subscription> deleteSubscription(Long subscriptionId);

    Optional<Subscription> updateSubscription(Subscription subscription);
}
