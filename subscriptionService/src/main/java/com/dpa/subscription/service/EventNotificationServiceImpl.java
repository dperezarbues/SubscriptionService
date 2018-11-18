package com.dpa.subscription.service;

import com.dpa.subscription.domain.Subscription;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventNotificationServiceImpl implements EventNotificationService{

    @Autowired
    private AmqpTemplate amqpTemplate;

    @Value("${dpa.rabbitmq.exchange}")
    private String exchange;

    @Value("${dpa.rabbitmq.routingkey}")
    private String routingkey;

    public void sendEventNotification(Subscription subscription){
        amqpTemplate.convertAndSend(exchange, routingkey, subscription);
        log.debug("Send msg = " + subscription);
    }

}
