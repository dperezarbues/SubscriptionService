package com.dpa.subscription;

import com.dpa.subscription.consumer.EmailNotificationConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import reactor.bus.EventBus;
import reactor.Environment;
import reactor.core.config.DispatcherType;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;

import static reactor.bus.selector.Selectors.$;

@SpringBootApplication
@EnableFeignClients(basePackages = {"com.dpa.subscription", "com.dpa.campaignservice"})
public class SubscriptionServiceApplication implements CommandLineRunner {

    /*===============================
    / INIT OF REACTOR PART FOR EMAIL
    /================================*/
    @Autowired
    private EventBus eventBus;

    @Autowired
    private EmailNotificationConsumer emailNotificationConsumer;

    @Bean
    Environment env() {
        return Environment.initializeIfEmpty().assignErrorJournal();
    }

    @Bean
    EventBus createEventBus(Environment env) {
        EventBus eventBus = EventBus.create(env, Environment.newDispatcher(Runtime.getRuntime().availableProcessors(), Runtime.getRuntime().availableProcessors(), DispatcherType.THREAD_POOL_EXECUTOR));
        return eventBus;
    }

    @Override
    public void run(String... args) throws Exception {
        eventBus.on($("emailNotificationConsumer"), emailNotificationConsumer);
    }

	public static void main(String[] args) {
		SpringApplication.run(SubscriptionServiceApplication.class, args);
	}
}
