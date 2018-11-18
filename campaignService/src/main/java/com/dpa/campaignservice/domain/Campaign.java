package com.dpa.campaignservice.domain;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
@Data
public class Campaign {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long campaignId;

    private String name;
    private String description;
    private Boolean active;
}

