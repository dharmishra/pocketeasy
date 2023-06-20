package com.pocketeasy.pocketeasy.model;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SubscriptionDTO {

    private UUID id;

    @NotNull
    @Size(max = 255)
    private String subscriptionName;

    @NotNull
    @Size(max = 255)
    private String subscriptionDetails;

    private Boolean active;

}
