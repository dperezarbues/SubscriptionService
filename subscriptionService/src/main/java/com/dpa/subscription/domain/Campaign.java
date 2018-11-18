package com.dpa.subscription.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


@Data
public class Campaign {

    private long campaignId;
    private String name;
    private String description;
    private Boolean active;
}

