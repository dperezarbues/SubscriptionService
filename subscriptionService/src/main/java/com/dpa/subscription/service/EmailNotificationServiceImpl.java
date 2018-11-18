package com.dpa.subscription.service;

import com.dpa.subscription.domain.EmailNotification;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Random;

@Slf4j
@Service
public class EmailNotificationServiceImpl implements EmailNotificationService {

    private static final Random generator = new Random();

    @Override
    public void sendEmailNotification(EmailNotification email) throws InterruptedException {

        log.debug("Sending Email notification for Email:" + email);

        //Simulating response time of approx. 2seconds on average
        Thread.sleep(generator.nextInt(4000));

    }
}
