package com.dpa.subscription.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Optional;

@Data
public class EmailNotification implements Serializable {

    private static final long serialVersionUID = -7885914881155827501L;

    String name;
    String surname;
    String email;
    String campaignName;
    String campaignDescription;

    public EmailNotification(){super();}

    public EmailNotification(Subscription subscription, Optional<Campaign> campaign) {
        this.name = subscription.getName();
        this.surname = subscription.getSurname();
        this.email = subscription.getEmail();
        campaign.ifPresent((c) -> {
            this.campaignName = c.getName();
            this.campaignDescription = c.getDescription();
        });
    }

    @Override
    public String toString() {
        return "EmailNotification{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", campaignName='" + campaignName + '\'' +
                ", campaignDescription='" + campaignDescription + '\'' +
                '}';
    }
}
