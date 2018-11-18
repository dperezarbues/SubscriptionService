package com.dpa.subscription.service;

import com.dpa.subscription.domain.Subscription;

public interface EventNotificationService {
    public void sendEventNotification(Subscription subscription);
}
