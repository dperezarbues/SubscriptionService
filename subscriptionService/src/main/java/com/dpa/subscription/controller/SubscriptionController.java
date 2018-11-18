package com.dpa.subscription.controller;

import com.dpa.subscription.domain.Subscription;
import com.dpa.subscription.exception.CampaignNotFoundException;
import com.dpa.subscription.service.SubscriptionService;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Slf4j
@Controller
@RequestMapping("/v1/subscriptions")
@Api(value = "Subscription REST Endpoint", description = "Handles subscriptions of new users")
public class SubscriptionController {

	private final SubscriptionService subscriptionService;

    @Autowired
    SubscriptionController(SubscriptionService subscriptionService) {
        this.subscriptionService = subscriptionService;
    }

    @GetMapping("/form")
    public String Form(Model model, @RequestParam("campaignId") Long campaignId) {
        model.addAttribute("", new Subscription(campaignId));
        return "";
    }

    @PostMapping(path = "/", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    @ResponseBody
    public ResponseEntity<String> handleSubscriptionForm(@ModelAttribute @Valid Subscription subscription, Errors errors){
        if (errors.hasErrors()) {
            return new ResponseEntity(errors.toString(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(subscription.toString(), HttpStatus.OK);
    }

	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody
	ResponseEntity<Resource<Subscription>> createSubscription(@RequestBody @Valid Subscription subscription) {
		log.info("Creating subscription {}", subscription);

		try {
            subscription = subscriptionService.createSubscription(subscription).orElse(null);
		} catch (CampaignNotFoundException ce) {
			return new ResponseEntity<>(buildSubscriptionResource(subscription), HttpStatus.BAD_REQUEST);
		}

		return new ResponseEntity<>(buildSubscriptionResource(subscription), HttpStatus.CREATED);

	}

	@GetMapping(path = "/{subscriptionId}")
	public @ResponseBody
	Resource<Subscription> getSubscription(@PathVariable Long subscriptionId) {
		log.info("Retrieving subscription for id {}", subscriptionId);

		return buildSubscriptionResource(subscriptionService.getSubscription(subscriptionId).orElse(null));
	}

    @DeleteMapping(path = "/{subscriptionId}")
    public @ResponseBody
    Resource<Subscription> deleteSubscription(@PathVariable Long subscriptionId) {
        log.info("Deleting subscription for id {}", subscriptionId);

        return buildSubscriptionResource(subscriptionService.deleteSubscription(subscriptionId).orElse(null));
    }

    @PutMapping(path = "/{subscriptionId}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public @ResponseBody
    Resource<Subscription> updateSubscription(@RequestBody @Valid Subscription subscription) {
        log.info("Updating subscription for email {}", subscription.getEmail());

        return buildSubscriptionResource(subscriptionService.updateSubscription(subscription).orElse(null));
    }

	@GetMapping
	public @ResponseBody
    List<Resource<Subscription>> getSubscriptions() {
		List<Resource<Subscription>> resources = new ArrayList<>();
		for (Subscription subscription : this.subscriptionService.getSubscription()) {
			resources.add(buildSubscriptionResource(subscription));
		}
		return resources;
	}

	private Resource<Subscription> buildSubscriptionResource(Subscription subscription) {
		Resource<Subscription> resource = new Resource<>(subscription);
		resource.add(linkTo(methodOn(SubscriptionController.class).getSubscription(subscription.getSubscriptionId())).withSelfRel());
		return resource;
	}
}
