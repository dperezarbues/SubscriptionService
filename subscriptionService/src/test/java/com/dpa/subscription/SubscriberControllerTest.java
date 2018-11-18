package com.dpa.subscription;

import com.dpa.subscription.domain.EmailNotification;
import com.dpa.subscription.domain.Subscription;
import com.dpa.subscription.service.SubscriptionService;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.web.context.WebApplicationContext;

import java.math.BigDecimal;
import java.util.List;
import java.util.concurrent.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

@RunWith(SpringRunner.class)
@SpringBootTest
@WebAppConfiguration
public class SubscriberControllerTest {

  private MockMvc mockMvc;

  @Autowired
  private WebApplicationContext webApplicationContext;

  @Before
  public void prepareMockMvc() throws Exception {
    this.mockMvc = webAppContextSetup(this.webApplicationContext).build();

    // Create two campaigns for transferring funds between them
    this.mockMvc.perform(post("/campaign").contentType(MediaType.APPLICATION_JSON)
      .content("{\"name\": \"FirstCampaign\", \"description\": \"description for first Campaign\", \"active\": true}"));

    this.mockMvc.perform(post("/campaign").contentType(MediaType.APPLICATION_JSON)
      .content("{\"name\": \"SecondCampaign\", \"description\": \"description for second Campaign\", \"active\": false}"));

  }

  @Test
  public void createSubscription() throws Exception {
    MvcResult result = this.mockMvc.perform(post("/v1/subscriptions").contentType(MediaType.APPLICATION_JSON)
      .content("{\"name\":\"test\", \"surname\":\"surname\", \"gender\":\"gender\", \"email\":\"email@gmail.com\" , \"dateOfBirth\":\"2018-01-23\", \"consent\":true, \"newsLetterId\":1}"))
      .andExpect(status().isCreated()).andReturn();

    ObjectMapper om = new ObjectMapper();
    //Added ignoring to fail on unknown properties just in case HATEOAS is used
    om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    Subscription subscription = om.readValue(result.getResponse().getContentAsString(), Subscription.class);

    assertThat(subscription.getName()).isEqualTo("test");
    assertThat(subscription.getSurname()).isEqualTo("surname");
    assertThat(subscription.getGender()).isEqualTo("gender");
    assertThat(subscription.getEmail()).isEqualTo("email@gmail.com");
  }

}
