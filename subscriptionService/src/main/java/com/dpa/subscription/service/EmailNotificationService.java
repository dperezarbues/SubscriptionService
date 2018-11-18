package com.dpa.subscription.service;

import com.dpa.subscription.domain.EmailNotification;

public interface EmailNotificationService  {
    void sendEmailNotification(EmailNotification email) throws InterruptedException;
}
