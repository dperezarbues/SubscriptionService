package com.dpa.subscription.client;

import com.dpa.subscription.domain.Campaign;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Optional;

@FeignClient("CampaignService")
public interface CampaignClient {
    @GetMapping(path = "/campaign/{campaignId}")
    Optional<Campaign> getCampaignById(@PathVariable Long campaignId);
}
