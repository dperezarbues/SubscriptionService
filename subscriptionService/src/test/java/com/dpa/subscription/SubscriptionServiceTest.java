package com.dpa.subscription;

import com.dpa.subscription.domain.EmailNotification;
import com.dpa.subscription.domain.Subscription;
import com.dpa.subscription.repository.SubscriptionRepository;
import com.dpa.subscription.service.EventNotificationService;
import com.dpa.subscription.service.SubscriptionService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.util.ReflectionTestUtils;
import reactor.bus.Event;
import reactor.bus.EventBus;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
@SpringBootTest
public class SubscriptionServiceTest {

    @Mock
    private EventNotificationService eventNotificationService;

    @Captor
    private ArgumentCaptor<Subscription> eventNotificationRcvrCaptor;

    @Mock
    private EventBus eventBus;

    @Captor
    private ArgumentCaptor<String> eventRcvrStringCaptor;

    @Captor
    private ArgumentCaptor<Event<EmailNotification>> eventRcvrEventCaptor;

    @Autowired
    private SubscriptionService subscriptionService;

    @Mock
    private SubscriptionRepository subscriptionRepository;

    List<Subscription> subscriptions = new ArrayList<>();

    @Before
    public void setUp() {


        ReflectionTestUtils.setField(subscriptionService, "eventNotificationService", eventNotificationService);
        ReflectionTestUtils.setField(subscriptionService, "eventBus", eventBus);

        LocalDate yesterday = LocalDate.now().minusDays(1);
        List<String> names = Arrays.asList("subscriber1", "subscriber2", "subscriber3", "subscriber4");
        List<String> surnames = Arrays.asList("subscriber1", "subscriber2", "subscriber3", "subscriber4");
        List<String> genders = Arrays.asList("male", "female", "male", "female");
        List<String> emails = Arrays.asList("test1@test.com", "test2@test.com", "test3@test.com", "test4@test.com");
        List<LocalDate> datesOfBirth = Arrays.asList(yesterday, yesterday, yesterday, yesterday);
        List<Boolean> consents = Arrays.asList(true, true, false, false);
        List<Long> newsLetterIds = Arrays.asList(0L, 1L, 2L, 3L);

        IntStream.range(0, names.size()-1).forEach(key -> {
            subscriptions.add(
                new Subscription(names.get(key), surnames.get(key), genders.get(key), emails.get(key),
                datesOfBirth.get(key), consents.get(key), newsLetterIds.get(key))
            );
            Mockito.when(subscriptionRepository.findById((long) key))
                    .thenReturn(Optional.ofNullable(subscriptions.get(key)));
        });

        Mockito.when(subscriptionRepository.findAll())
                .thenReturn(subscriptions);
    }

    @Test
    public void addSubscription() throws Exception {

        this.subscriptionService.createSubscription(subscriptions.get(1));

        assertThat(this.subscriptionService.getSubscription(1L)).isEqualTo(subscriptions.get(1));

        verify(eventNotificationService, times(1)).sendEventNotification(eventNotificationRcvrCaptor.capture());
        verify(eventBus, times(1)).notify(eventRcvrStringCaptor.capture(), eventRcvrEventCaptor.capture());

        List<Subscription> capturedEvents = eventNotificationRcvrCaptor.getAllValues();;

        assertThat(capturedEvents.get(0).getName()).isEqualTo(subscriptions.get(1).getName());
        assertThat(capturedEvents.get(0).getSurname()).isEqualTo(subscriptions.get(1).getSurname());
        assertThat(capturedEvents.get(0).getEmail()).isEqualTo(subscriptions.get(1).getEmail());
        assertThat(capturedEvents.get(0).getGender()).isEqualTo(subscriptions.get(1).getGender());
        assertThat(capturedEvents.get(0).getConsent()).isEqualTo(subscriptions.get(1).getConsent());
        assertThat(capturedEvents.get(0).getDateOfBirth()).isEqualTo(subscriptions.get(1).getDateOfBirth());
        assertThat(capturedEvents.get(0).getNewsLetterId()).isEqualTo(subscriptions.get(1).getNewsLetterId());

        List<String> capturedStrings = eventRcvrStringCaptor.getAllValues();
        List<Event<EmailNotification>> capturedEmailEvents = eventRcvrEventCaptor.getAllValues();
        assertThat(capturedStrings.get(0)).isEqualTo("emailNotificationConsumer");
        assertThat(capturedEmailEvents.get(0).getData().getEmail()).isEqualTo(subscriptions.get(1).getName());
        assertThat(capturedEmailEvents.get(0).getData().getSurname()).isEqualTo(subscriptions.get(1).getSurname());
        assertThat(capturedEmailEvents.get(0).getData().getEmail()).isEqualTo(subscriptions.get(1).getEmail());

    }

    @Test
    public void addMultipleSubscription() throws Exception {

        this.subscriptionService.createSubscription(subscriptions.get(1));
        assertThat(this.subscriptionService.getSubscription(1L)).isEqualTo(subscriptions.get(1));
        this.subscriptionService.createSubscription(subscriptions.get(2));
        assertThat(this.subscriptionService.getSubscription(2L)).isEqualTo(subscriptions.get(2));
    }

}
