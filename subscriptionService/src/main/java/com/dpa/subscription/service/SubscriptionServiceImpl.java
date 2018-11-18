package com.dpa.subscription.service;

import com.dpa.subscription.client.CampaignClient;
import com.dpa.subscription.domain.Campaign;
import com.dpa.subscription.domain.EmailNotification;
import com.dpa.subscription.domain.Subscription;
import com.dpa.subscription.exception.CampaignNotFoundException;
import com.dpa.subscription.repository.SubscriptionRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.util.Optional;

@Slf4j
@Service
public class SubscriptionServiceImpl implements SubscriptionService{

    private final EventNotificationService eventNotificationService;
    private final SubscriptionRepository subscriptionsRepository;

    @Autowired
    private CampaignClient campaignClient;

    @Autowired
    private EventBus eventBus;

    @Autowired
    SubscriptionServiceImpl(SubscriptionRepository subscriptionsRepository,
                            EmailNotificationService emailNotificationService,
                            EventNotificationService eventNotificationService) {
        this.subscriptionsRepository = subscriptionsRepository;
        this.eventNotificationService = eventNotificationService;
    }

    @Override
    public Optional<Subscription> createSubscription(Subscription subscription) {

        Optional<Campaign> campaign = campaignClient.getCampaignById(subscription.getNewsLetterId());
        EmailNotification email = new EmailNotification(subscription, campaign);

        Boolean campaignActive = campaign.map(c -> c.getActive()).orElse(false);
        try {
            subscriptionsRepository.save(subscription);

            if (campaignActive) {
                //Using AMQP for sending events to subscribers
                eventNotificationService.sendEventNotification(subscription);

                //Using Spring Reactor to handle email notification
                eventBus.notify("emailNotificationConsumer", Event.wrap(email));
            } else {
                throw new CampaignNotFoundException("Campaign was not found or is not active");
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return Optional.of(subscription);
    }

    @Override
    public Optional<Subscription> getSubscription(Long subscriptionId) {
        return subscriptionsRepository.findById(subscriptionId);
    }

    @Override
    public Optional<Subscription> deleteSubscription(Long subscriptionId) {
        Optional<Subscription> subscription  = subscriptionsRepository.findById(subscriptionId);
        subscriptionsRepository.deleteById(subscriptionId);
        return subscription;
    }

    @Override
    public Optional<Subscription> updateSubscription(Subscription subscription) {
        subscriptionsRepository.save(subscription);
        return Optional.of(subscription);
    }

    public Iterable<Subscription> getSubscription() {
        return subscriptionsRepository.findAll();
    }


}
