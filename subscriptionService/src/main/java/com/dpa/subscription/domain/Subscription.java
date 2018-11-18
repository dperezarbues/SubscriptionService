package com.dpa.subscription.domain;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.ResourceSupport;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Data
@Slf4j
public class Subscription extends ResourceSupport implements Serializable {

    private static final long serialVersionUID = 2289985397493823061L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long subscriptionId;

    @ApiModelProperty(notes = "Name of the User", required = false)
    private String name;
    private String surname;

    @ApiModelProperty(notes = "Gender of the User", allowableValues = "man,woman")
    private String gender;

    @NotBlank(message = "You need to provide a valid email")
    @Email(message = "You need to provide a valid email")
    private String email;

    @NotNull(message = "You need to provide a valid birthday date")
    @Past(message = "You need to provide a valid birthday date")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;

    @AssertTrue(message = "You need to accept conditions to be able to subscribe")
    private Boolean consent;

    @NotNull(message = "Campaign ID can't be null")
    @Positive(message = "Campaign ID has to be positive")
    private Long newsLetterId;

    public Subscription(){
        super();
    }

    public Subscription(Long newsLetterId){
        this.newsLetterId = newsLetterId;
    }

    @JsonCreator
    public Subscription(@JsonProperty("name") String name,
                        @JsonProperty("surname") String surname,
                        @JsonProperty("gender") String gender,
                        @JsonProperty("email") String email,
                        @JsonProperty("dateOfBirth") LocalDate dateOfBirth,
                        @JsonProperty("consent") Boolean consent,
                        @JsonProperty("newsLetterId") Long newsLetterId
                        ) {
        this.name = name;
        this.surname = surname;
        this.gender = gender;
        this.email = email;
        this.dateOfBirth = dateOfBirth;
        this.consent = consent;
        this.newsLetterId = newsLetterId;
    }

    @Override
    public String toString(){
        JSONObject json = new JSONObject();

        try {
            json.put("name", this.name);
            json.put("surname", this.surname);
            json.put("gender", this.gender);
            json.put("email", this.email);
            json.put("dateOfBirth", this.dateOfBirth);
            json.put("consent", this.consent);
            json.put("newsLetterId", this.newsLetterId);

        } catch (JSONException e) {
            log.error(e.getMessage());
        }
        return json.toString();
    }
}
