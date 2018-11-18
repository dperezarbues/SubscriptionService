package com.dpa.campaignservice.repository;

import com.dpa.campaignservice.domain.Campaign;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource(collectionResourceRel = "campaign", path = "campaign")
public interface CampaignRepository extends PagingAndSortingRepository<Campaign, Long> {
    List<Campaign> findByName(@Param("name") String name);
}