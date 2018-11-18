package com.dpa.subscription.consumer;

import com.dpa.subscription.domain.EmailNotification;
import com.dpa.subscription.service.EmailNotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.fn.Consumer;


@Slf4j
@Service
public class EmailNotificationConsumer implements Consumer<Event<EmailNotification>> {

    @Autowired
    private EmailNotificationService emailNotificationService;

    @Override
    public void accept(Event<EmailNotification> emailNotificationEvent) {
        EmailNotification emailNotification = emailNotificationEvent.getData();

        try {
            emailNotificationService.sendEmailNotification(emailNotification);
        } catch (InterruptedException e) {
            // ignore
        }
    }
}
